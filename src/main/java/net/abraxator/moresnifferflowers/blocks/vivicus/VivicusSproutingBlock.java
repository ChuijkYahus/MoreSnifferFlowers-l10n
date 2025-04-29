package net.abraxator.moresnifferflowers.blocks.vivicus;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.blocks.ModCropBlock;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VivicusSproutingBlock extends VivicusLeavesBlock implements ModCropBlock, ColorableVivicusBlock {
    public VivicusSproutingBlock(Properties p_54422_) {
        super(p_54422_);
        this.registerDefaultState(defaultBlockState().setValue(ModStateProperties.VIVICUS_CURED, false));
    }
    private static final VoxelShape SHAPE0 = Block.box(3, 6,  3, 13, 16, 13);
    private static final VoxelShape SHAPE1 = Block.box(3, 2,  3, 13, 16, 13);
    private static final VoxelShape SHAPE2 = Block.box(3, 0,  3, 13, 16, 13);


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ModStateProperties.AGE_3);
        pBuilder.add(ModStateProperties.VIVICUS_CURED);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_3;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState pState) {
        return super.isRandomlyTicking(pState) || !isMaxAge(pState);
    }

    public void grow(BlockState pState, Level pLevel, BlockPos pPos) {
        makeGrowOnBonemeal(pLevel, pPos, pState);
        
        if(isMaxAge(pLevel.getBlockState(pPos))) {
            BoblingEntity boblingEntity = new BoblingEntity(pLevel, pState.getValue(ModStateProperties.VIVICUS_CURED));
            boblingEntity.setPos(pPos.getCenter());
            pLevel.addFreshEntity(boblingEntity);
            pLevel.removeBlock(pPos, false);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        net.neoforged.neoforge.common.util.TriState soilDecision = level.getBlockState(pos.above()).canSustainPlant(level, pos.above(), Direction.DOWN, state);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return level.getBlockState(pos.above()).is(ModBlocks.VIVICUS_LEAVES.get());
    }

    @Override
    public BlockState updateShape(BlockState state , LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.UP && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pRandom.nextDouble() <= 0.5D) {
            grow(pState, pLevel, pPos);
        }
        
        super.randomTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return !isMaxAge(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        grow(pState, pLevel, pPos);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(pos);
        return switch (state.getValue(ModStateProperties.AGE_3)) {
            case 1 -> SHAPE1.move(vec3.x, vec3.y, vec3.z);
            case 2 -> SHAPE2.move(vec3.x, vec3.y, vec3.z);
            default -> SHAPE0.move(vec3.x, vec3.y, vec3.z);
        };
    }
}
