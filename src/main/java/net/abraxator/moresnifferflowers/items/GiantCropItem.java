package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.blocks.multiblock.MultiBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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

public class GiantCropItem extends BlockItem {
    public GiantCropItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        var level = pContext.getLevel();
        var clickPos = pContext.getClickedPos().relative(pContext.getClickedFace(), 1);
        var aabb = AABB.ofSize(clickPos.getCenter(), 2, 2, 2);
        MultiBlock multiBlock = (MultiBlock) pState.getBlock();
        multiBlock.fullBlockShape(clickPos, null).forEach(pos -> {
            level.setBlockAndUpdate(pos, this.getBlock().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(clickPos)));
            if (level.getBlockEntity(pos) instanceof MultiBlockEntity entity) {
                entity.setCenter(clickPos);
            }
        });

        return true;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        MultiBlock multiBlock = (MultiBlock) pState.getBlock();
        var aabb = AABB.ofSize(pContext.getClickedPos().relative(pContext.getClickedFace(), 1).getCenter(), 2, 2, 2);
        var ret = multiBlock.fullBlockShape(pos.relative(pContext.getClickedFace()), null).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced());

        return ret;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("CREATIVE ONLY").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    }
}