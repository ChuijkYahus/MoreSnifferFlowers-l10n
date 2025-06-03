package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
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

import java.util.stream.Stream;

public class SaltemoneBlock extends Block implements ModEntityBlock, Corruptable, ModCropBlock, MultiBlock {
    public SaltemoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(ModStateProperties.CENTER, false).setValue(getAgeProperty(), 0).setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }
    protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HorizontalDirectionalBlock.FACING, ModStateProperties.CENTER, getAgeProperty());
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        placementHelper(level, pos, state, pPlacer, stack);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof SaltemoneBlockEntity entity) {
            var list = fullBlockShape(entity.center, blockState).filter(pos -> super.canSurvive(level.getBlockState(pos), level, pos)).toList();
            boolean isWaterBelow = fullBlockShape(entity.center.below(), blockState).allMatch(level::isWaterAt);

            return !level.isWaterAt(blockPos) && (list.size() == 4 || corruptionCheck(entity.getCenter(), level, blockState, ModBlocks.SOURLEMONE.get())) && isWaterBelow;
        }
        var list = fullBlockShape(blockPos, blockState).filter(pos -> super.canSurvive(level.getBlockState(pos), level, pos)).toList();
        return !level.isWaterAt(blockPos) && list.size() == 4;
    }

    @Override
    public boolean directional() {
        return true;
    }

    @Override
    public Stream<BlockPos> fullBlockShape(Direction direction, BlockPos center) {
        BlockPos relative = center.relative(direction).relative(direction.getClockWise());
        return BlockPos.betweenClosedStream(new AABB(center, relative));
    }

    @Override
    public BlockState updateShape(BlockState stateOriginal, Direction dir, BlockState stateNew, LevelAccessor level, BlockPos pCurrentPos, BlockPos pNewPos) {
        return updateShapeHelper(stateOriginal, level, pCurrentPos);
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
        growHelper(pLevel, pPos, pState);
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
                growHelper(level, pos, state);
            }
        }
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityinside) {
        corruptionHelper(state, level, pos, entityinside);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SaltemoneBlockEntity(blockPos, blockState);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABB;
    }

}
