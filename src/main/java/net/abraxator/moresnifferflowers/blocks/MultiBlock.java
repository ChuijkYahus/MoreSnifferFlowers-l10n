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
import net.minecraft.world.item.context.BlockPlaceContext;
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

    /*
   How to use:
   implement fullBlockShape and make it return the whole shape
   directional = true if the fullBlockShape needs directions

   For placement:
   Override setPlacedBy - return place
   Override getStateForPlacement - return getStateForPlacementHelper

   For destroying:
   Override updateShape - return updateShapeHelper
   Override canSurvive - return canSurviveHelper
   Optionally Override extraSurviveRequirements

   growHelper - for bone meal and tick growth

   for Corruption - Override entityInside - return corruptionHelper

    */

    Stream<BlockPos> fullBlockShape(@Nullable Direction direction, BlockPos center);
    boolean directional(); // True if the BlockState doesn't have directions

    default Stream<BlockPos> fullBlockShape(BlockPos center, @Nullable BlockState state){
        if (!directional() || state == null) return fullBlockShape(null, center);
        return fullBlockShape(state.getValue(HorizontalDirectionalBlock.FACING), center);
    }


    default void place(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack){
        fullBlockShape(pos, state).forEach(blockPos -> {
            blockPos = blockPos.immutable();
            level.setBlock(blockPos, state.setValue(ModStateProperties.CENTER, pos.equals(blockPos)), 3);
            if(level.getBlockEntity(blockPos) instanceof MultiBlockEntity entity) {
                entity.setCenter(pos);
            }
        });
    }

    default BlockState getStateForPlacementHelper(BlockPlaceContext context, Block ts) {
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = ts.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection());

        return canPlace(level, pos, state) ? state : null;
    }

    default boolean canPlace(LevelReader level, BlockPos center, BlockState state) {
        return fullBlockShape(center, state).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced() && extraSurviveRequirements(level, blockPos, state));
    }

    default void destroy(BlockPos center, Level level, BlockState state){
        fullBlockShape(center, state).forEach(pos ->{
            if (level.getBlockState(pos).is(state.getBlock())) {
                level.destroyBlock(pos, true);
            }
        });
    }

    default boolean allBlocksPresent(LevelReader level, BlockPos pos, BlockState state, Block originalBlock, @Nullable Block corruptedBlock){
       BlockPos center = getCenter(level, pos);
       boolean ret;
       if (corruptedBlock != null) {
           ret = fullBlockShape(center, state).allMatch(blockPos -> level.getBlockState(blockPos).is(originalBlock) || level.getBlockState(blockPos).is(corruptedBlock));
       } else {
           ret = fullBlockShape(center, state).allMatch(blockPos -> level.getBlockState(blockPos).is(originalBlock));
       }

       if (ret && level.getBlockEntity(pos) instanceof MultiBlockEntity entity && !entity.isPlaced) {
           fullBlockShape(center, state).forEach(blockPos -> MultiBlockEntity.setPlaced(level, blockPos));
       }

       return ret;
    }

    default BlockState updateShapeHelper(BlockState state, LevelAccessor level, BlockPos pos){
        if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity){
            boolean canSurvive = state.getBlock().canSurvive(state, level, pos);
            if (!canSurvive){
                destroy(entity.getCenter(), (Level) level, state);
                return Blocks.AIR.defaultBlockState();
            }
        }else {
            level.destroyBlock(pos, true);
            return Blocks.AIR.defaultBlockState();
        }

        return state;
    }

    default boolean canSurviveHelper(BlockState state, LevelReader level, BlockPos pos, Block originalBlock, @Nullable Block corruptedBlock){
        if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity){
            //survive logic
            boolean extraSurvive = fullBlockShape(entity.getCenter(), state).allMatch(blockPos -> extraSurviveRequirements(level, blockPos, state));
            return (allBlocksPresent(level, pos, state, originalBlock, corruptedBlock) || !entity.isPlaced) && extraSurvive;
        } else {
            //placement logic
            return canPlace(level, pos, state);
        }
    }

    //Override this one to check for other blocks (like if bondripia can hang)
    //Runs for every single block
    default boolean extraSurviveRequirements(LevelReader level, BlockPos pos, BlockState state){
        return true;
    }

    default BlockPos getCenter(LevelReader level, BlockPos pos){
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
