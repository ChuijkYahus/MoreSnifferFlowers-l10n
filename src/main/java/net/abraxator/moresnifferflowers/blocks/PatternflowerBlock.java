package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.abraxator.moresnifferflowers.init.ModStateProperties.*;

public class PatternflowerBlock extends CaulorflowerBlock implements BonemealableBlock, ModCropBlock {

    public PatternflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(FLIPPED, true)
                .setValue(getAgeProperty(), 0)
                .setValue(ModStateProperties.BLOCK_PATTERN, BlockPattern.EMPTY)
                .setValue(EMPTY, true)
                .setValue(SHEARED, false));

    }

    @Override
    public boolean canBeColored(BlockState blockState, Dye dye) {
        return false;
    }

    @Override
    public boolean isCorrupted() {
        return true;
    }

    @Override
    public void popResource(Level level, BlockPos pos, BlockState state) {
        popResource(level, pos, BlockPattern.fromState(state).getItem().getDefaultInstance());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SHEARED, FLIPPED, getAgeProperty(), ModStateProperties.BLOCK_PATTERN, EMPTY);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if(canSurvive(pState, pLevel, pCurrentPos)) {
            return pState.setValue(FLIPPED, pCurrentPos.getY() % 2 == 0).setValue(EMPTY, BlockPattern.isEmpty(pState));
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
        if(state.is(this)) {
            return state.setValue(FLIPPED, pContext.getClickedPos().getY() % 2 == 0);
        }
        return super.getStateForPlacement(pContext).setValue(FLIPPED, pContext.getClickedPos().getY() % 2 == 0).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockPos = pPos.below();
        BlockState blockState = pLevel.getBlockState(blockPos);
        BlockPos wallPos = pPos.relative(pState.getValue(FACING).getOpposite());
        BlockState wallState = pLevel.getBlockState(wallPos);
        return ((blockState.is(this) || blockState.is(ModBlocks.CAULORFLOWER.get())) && getAge(blockState) > 0) || blockState.isFaceSturdy(pLevel, blockPos, Direction.UP) || wallState.isFaceSturdy(pLevel, wallPos, pState.getValue(FACING));
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if(pRandom.nextFloat() < 0.15) {
            grow(pLevel, pPos, false);
        }
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return BlockPattern.fromState(pState).isBanner() ? pRandom.nextFloat() < 0.2 : pRandom.nextFloat() < 0.50;
    }

    @Override
    public boolean harvestable(BlockState blockState) {
        return isMaxAge(blockState) && !BlockPattern.isEmpty(blockState);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.is(this)) {
            return;
        }

        var stateBelow = level.getBlockState(pos.below());
        if(!stateBelow.is(this) && !stateBelow.is(Blocks.AIR)) {
            popResource(level, pos, new ItemStack(ModItems.PATTERNFLOWER_SEEDS.get()));
        }

        if(!BlockPattern.isEmpty(state) && isMaxAge(state)) {
            popResource(level, pos, BlockPattern.fromState(state).getItem().getDefaultInstance());
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_2;
    }
}
