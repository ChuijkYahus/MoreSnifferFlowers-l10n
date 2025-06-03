package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface MultiBlock {

    Stream<BlockPos> fullBlockShape(@Nullable Direction direction, BlockPos center);
    boolean directional(); // True if the BlockState doesn't have directions

    default Stream<BlockPos> fullBlockShape(BlockPos center, @Nullable BlockState state){
        if (!directional() || state == null) return fullBlockShape(null, center);
        return fullBlockShape(state.getValue(HorizontalDirectionalBlock.FACING), center);
    }

    default void placementHelper(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack){
        fullBlockShape(pos, state).forEach(blockPos -> {
            blockPos = blockPos.immutable();
            level.setBlock(blockPos, state.setValue(ModStateProperties.CENTER, pos.equals(blockPos)), 3);
            if(level.getBlockEntity(blockPos) instanceof MultiBlockEntity entity) {
                entity.setCenter(pos);
            }
        });
    }

    default boolean canPlace(Level level, BlockPos pos, @Nullable Direction direction) {
        return fullBlockShape(direction, pos).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());
    }

    // Prevents the multiblock from destroying itself while being corrupted
    // The corrupted block probably doesn't have to be hardcoded but who cares
    default boolean corruptionCheck(BlockPos center, LevelReader level, BlockState state, Block corruptedBlock) {
            boolean anyMatch =  fullBlockShape(center, state).anyMatch(pos -> level.getBlockState(pos).is(corruptedBlock));
            boolean allMatch =  fullBlockShape(center, state).allMatch(pos -> level.getBlockState(pos).is(corruptedBlock));
            boolean anyMissing = fullBlockShape(center, state).anyMatch(pos -> level.getBlockState(pos).isAir());

            return (anyMatch && !allMatch) && !anyMissing;
    }

    default void destroyHelper(BlockPos center, Level level, BlockState state){
        fullBlockShape(center, state).forEach(pos ->{
            if (level.getBlockState(pos).is(state.getBlock())) {
                level.destroyBlock(pos, true);
            }
        });
    }

    default boolean allBlocksPresent(Level level, BlockPos pos, BlockState state, @Nullable Block corruptedBlock){
       BlockPos center = getCenter(level, pos);
       Block block = state.getBlock();
       if (corruptedBlock != null) return fullBlockShape(center, state).allMatch(blockPos -> level.getBlockState(blockPos).is(block) || corruptionCheck(center, level, state, corruptedBlock));

       return fullBlockShape(center, state).allMatch(blockPos -> level.getBlockState(blockPos).is(block));
    }

    default BlockState updateShapeHelper(BlockState state, LevelAccessor level, BlockPos pos){
        if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity){
            boolean canSurvive = state.getBlock().canSurvive(state, level, pos);
            if (!canSurvive){
                destroyHelper(entity.getCenter(), (Level) level, state);
                return Blocks.AIR.defaultBlockState();
            }
        }else {
            level.destroyBlock(pos, true);
            return Blocks.AIR.defaultBlockState();
        }

        return state;
    }

    default BlockPos getCenter(Level level, BlockPos pos){
        if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity){
            return entity.getCenter();
        }
        MoreSnifferFlowers.LOGGER.error("Couldn't get center for multi block");
        return pos;
    }

    default boolean isCenter(LevelReader level, BlockPos pos){
        if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity) {
            return entity.getCenter().equals(pos);
        }
        return false;
    }

    default void corruptionHelper(BlockState state, Level level, BlockPos pos, Entity entityInside){
        if(entityInside instanceof CorruptedProjectile corruptedProjectile && CorruptionRecipe.canBeCorrupted(state.getBlock(), level)) {
            if(level.getBlockEntity(pos) instanceof MultiBlockEntity entity) {
                corruptedProjectile.discard();
                BlockPos centrePos = entity.getCenter();
                BlockState centreState = level.getBlockState(centrePos);
                fullBlockShape(entity.getCenter(), state).forEach(pos1 -> {
                    afterCorruption(centrePos, level, pos1);
                });
            }
        }
    }

    default void afterCorruption(BlockPos centrePos, Level level, BlockPos pos){
        if (!CorruptionRecipe.canBeCorrupted(level.getBlockState(pos).getBlock(), level)) return;

        Block corruptedBlock = CorruptionRecipe.getCorruptedBlock(level.getBlockState(pos).getBlock(), level).get();
        level.setBlockAndUpdate(pos, corruptedBlock.withPropertiesOf(level.getBlockState(pos)));

        if(level.getBlockEntity(pos) instanceof MultiBlockEntity entity){
            entity.setCenter(centrePos);
        }
    }

    default void growHelper(Level level, BlockPos blockPos, BlockState blockState){
        Block block = blockState.getBlock();
        if (block instanceof ModCropBlock cropBlock) {
            if(level.getBlockEntity(blockPos) instanceof MultiBlockEntity entity) {
                fullBlockShape(entity.getCenter(), level.getBlockState(blockPos)).forEach(pos -> {
                    if(level.getBlockState(pos).is(block)) {
                        cropBlock.makeGrowOnBonemeal(level, pos, level.getBlockState(pos));
                    }else {
                        MoreSnifferFlowers.LOGGER.warn(block +" goofed up, centre = " + entity.getCenter().toString());
                        MoreSnifferFlowers.LOGGER.warn("If this happens often, you might wanna report it to the More Sniffer Flowers devs");
                        level.destroyBlock(pos, false);
                    }
                });
            } else level.destroyBlock(blockPos, true);
        }
    }

}
