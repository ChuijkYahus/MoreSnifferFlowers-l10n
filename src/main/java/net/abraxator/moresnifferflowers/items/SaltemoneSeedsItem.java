package net.abraxator.moresnifferflowers.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SaltemoneSeedsItem extends BlockItem {
    public SaltemoneSeedsItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatableWithFallback("tooltip.saltemone_seeds", "Plant on water!").withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var state = level.getBlockState(pos);
        Direction direction = pContext.getHorizontalDirection();
        BlockPos relative = pos.relative(direction).relative(direction.getClockWise());
        return BlockPos.betweenClosedStream(new AABB(pos, relative)).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());
    }
}
