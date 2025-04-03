package net.abraxator.moresnifferflowers.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BerootCauldronItem extends BlockItem {
    public BerootCauldronItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var state = level.getBlockState(pos);
        Direction direction = pContext.getHorizontalDirection();
        BlockPos relative = pos.relative(direction).relative(direction.getClockWise()).above();
        return BlockPos.betweenClosedStream(new AABB(pos, relative)).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());
    }
}
