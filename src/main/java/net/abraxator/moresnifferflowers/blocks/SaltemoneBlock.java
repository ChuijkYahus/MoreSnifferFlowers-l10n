package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class SaltemoneBlock extends Block implements ModEntityBlock, Corruptable, ModCropBlock {
    public SaltemoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(ModStateProperties.ENTITY, false).setValue(getAgeProperty(), 0).setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HorizontalDirectionalBlock.FACING, ModStateProperties.ENTITY, getAgeProperty());
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection());
    }

    public static Stream<BlockPos> blockPosStream(BlockPos pos, BlockState state){
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        BlockPos relative = pos.relative(direction).relative(direction.getClockWise());
        return BlockPos.betweenClosedStream(new AABB(pos, relative));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        blockPosStream(pos, state).forEach(blockPos -> {
            blockPos = blockPos.immutable();
            level.setBlock(blockPos, state.setValue(ModStateProperties.ENTITY, pos.equals(blockPos)), 3);
            if(level.getBlockEntity(blockPos) instanceof SaltemoneBlockEntity entity) {
                entity.center = pos;
            }
        });
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof SaltemoneBlockEntity entity) {
            var list = blockPosStream(entity.center, blockState).filter(pos -> super.canSurvive(level.getBlockState(pos), level, pos)).toList();
            boolean isWaterBelow = blockPosStream(entity.center.below(), blockState).allMatch(level::isWaterAt);

            return !level.isWaterAt(blockPos) && (list.size() == 4 || corruptionCheck(entity, level)) && isWaterBelow;
        }
        var list = blockPosStream(blockPos, blockState).filter(pos -> super.canSurvive(level.getBlockState(pos), level, pos)).toList();
        return !level.isWaterAt(blockPos) && list.size() == 4;
    }

    private boolean corruptionCheck(SaltemoneBlockEntity entity, LevelReader level){
        AtomicInteger i = new AtomicInteger();
        BlockPos.betweenClosedStream(new AABB(entity.center, entity.center.offset(1, 0, 1))).forEach(pos -> {
            if (level.getBlockState(pos).is(ModBlocks.SOURLEMONE.get()))
                i.getAndIncrement();
        });
        return !(i.get() == 0);
    }

    @Override
    public BlockState updateShape(BlockState stateOriginal, Direction dir, BlockState stateNew, LevelAccessor level, BlockPos pCurrentPos, BlockPos pNewPos) {
        if (level.getBlockEntity(pCurrentPos) instanceof SaltemoneBlockEntity entity){
            if (!canSurvive(stateOriginal, level, pCurrentPos)){
                blockPosStream(entity.center, stateOriginal).forEach(pos ->{
                    level.destroyBlock(pos, true);
                });
            }
        } else level.destroyBlock(pCurrentPos, true);

        return stateOriginal;
    }



        @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_2;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return !isMaxAge(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        grow(pLevel, pPos);
    }

    public boolean isCorrupted(){
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof SaltemoneBlockEntity entity && pos.equals(entity.center)) {
            if (isMaxAge(state)) {
                Vec3 vec3 = entity.center.getCenter().relative(state.getValue(HorizontalDirectionalBlock.FACING), 0.5D).relative(state.getValue(HorizontalDirectionalBlock.FACING).getClockWise(), 0.5D).relative(Direction.UP, 1);
                SaltBubbleProjectile projectile = new SaltBubbleProjectile(vec3.x, vec3.y, vec3.z, level);
                projectile.setCorrupted(isCorrupted());
                projectile.shoot(vec3.x, vec3.y, vec3.z, 0.5F, 0.1F);
                level.addFreshEntity(projectile);
            } else {
                grow(level, pos);
            }
        }
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityinside) {
        if(entityinside instanceof CorruptedProjectile corruptedProjectile && CorruptionRecipe.canBeCorrupted(state.getBlock(), level)) {
            if(level.getBlockEntity(pos) instanceof SaltemoneBlockEntity entity) {
                corruptedProjectile.discard();
                BlockPos centrePos = entity.center;
                BlockState centreState = level.getBlockState(centrePos);
                blockPosStream(entity.center, state).forEach(pos1 -> {
                    SaltemoneBlock.afterCorruption(centrePos, level, pos1);
                });
            }
        }
    }

    public static void afterCorruption(BlockPos centrePos, Level level, BlockPos pos){
        level.setBlockAndUpdate(pos, ModBlocks.SOURLEMONE.get().withPropertiesOf(level.getBlockState(pos)));
        if(level.getBlockEntity(pos) instanceof SaltemoneBlockEntity entity){
            entity.center = centrePos;
        }
    }

    public void grow(Level level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof SaltemoneBlockEntity entity) {
            blockPosStream(entity.center, level.getBlockState(blockPos)).forEach(pos -> {
                if(level.getBlockState(pos).is(this)) {
                    makeGrowOnBonemeal(level, pos, level.getBlockState(pos));
                }else {
                    MoreSnifferFlowers.LOGGER.warn("Saltemone or Sourlemon goofed up, centre = {}", entity.center.toString());
                    MoreSnifferFlowers.LOGGER.warn("If this happens often, you might wanna report it to the More Sniffer Flowers devs");
                    level.destroyBlock(pos, false);
                }
            });
        } else level.destroyBlock(blockPos, true);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SaltemoneBlockEntity(blockPos, blockState);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

}
