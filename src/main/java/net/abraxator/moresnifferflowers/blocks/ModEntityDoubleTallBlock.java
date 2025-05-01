package net.abraxator.moresnifferflowers.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModEntityDoubleTallBlock extends Block implements IModEntityDoubleTallBlock {
    protected BlockPos ENTITY_POS;
    
    public ModEntityDoubleTallBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
    
    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if(!pLevel.isClientSide) {
            if(pPlayer.isCreative()) {
                preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
            } else {
                var blockEntity = isUpper(pState) ? pLevel.getBlockEntity(pPos) : null;
                dropResources(pState, pLevel, pPos, blockEntity, pPlayer, pPlayer.getMainHandItem());
            }
        }
/*
        if (isLower(pState) && pLevel.getBlockState(pPos.above()).is(getUpperBlock()))
            pLevel.destroyBlock(pPos.above(), true);
        if (isUpper(pState) && pLevel.getBlockState(pPos.below()).is(getLowerBlock()))
            pLevel.destroyBlock(pPos.below(), true);
*/

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return pState;
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pBlockEntity, pTool);
    }

    @Override
    public void onRemove(@NotNull BlockState pState, Level pLevel, BlockPos pPos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        if(isUpper(pState)) {
            Containers.dropContentsOnDestroy(pState, pNewState, pLevel, pPos);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
    
    @Override
    public BlockState updateShape(BlockState state , LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (!canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
        if (isLower(state) && direction == Direction.UP) {
           if (!neighborState.is(getUpperBlock())) return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (isLower(pState)) {

            return super.canSurvive(pState, pLevel, pPos);
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos.below());
            if (!isStateThis(pState)) return super.canSurvive(pState, pLevel, pPos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return isStateThis(blockstate) && isLower(blockstate);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockPos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        return blockPos.getY() < level.getMaxY() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    }
    
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlockAndUpdate(pPos.above(), getUpperBlock().defaultBlockState());
    }
    
    public void preventCreativeDropFromBottomPart(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (isUpper(pState)) {
            BlockPos blockPosBelow = pPos.below();
            BlockState blockStateBelow = pLevel.getBlockState(blockPosBelow);
            if (isStateThis(blockStateBelow) && isLower(blockStateBelow)) {
                BlockState blockStateForReplacement = blockStateBelow.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                pLevel.setBlock(blockPosBelow, blockStateForReplacement, 35);
                pLevel.levelEvent(pPlayer, 2001, blockPosBelow, Block.getId(blockStateBelow));
            }
        }
    }
}
