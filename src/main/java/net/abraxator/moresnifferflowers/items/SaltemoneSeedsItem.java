package net.abraxator.moresnifferflowers.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SaltemoneSeedsItem extends PlaceOnWaterBlockItem {
    public SaltemoneSeedsItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatableWithFallback("tooltip.saltemone_seeds", "Plant on water!").withStyle(ChatFormatting.GOLD));
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos().above();
        var blockstate1 = level.getBlockState(blockPos);
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult blockhitresult1 = blockhitresult.withPosition(blockhitresult.getBlockPos().above());


        BlockPlaceContext context1 = new BlockPlaceContext(player, context.getHand(), itemStack, blockhitresult1);

        return super.place(context);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        var pos = pContext.getClickedPos();
        var level = pContext.getLevel();
        var state = level.getBlockState(pos);
        Direction direction = pContext.getHorizontalDirection();
        BlockPos relative = pos.relative(direction).relative(direction.getClockWise());
        boolean ret = BlockPos.betweenClosedStream(new AABB(pos, relative)).allMatch(blockPos -> level.getBlockState(blockPos).canBeReplaced() && level.getFluidState(blockPos).getType() == Fluids.EMPTY )
                && BlockPos.betweenClosedStream(new AABB(pos.below(), relative.below())).allMatch(blockPos -> level.getFluidState(blockPos).getType() == Fluids.WATER);
        return ret;
    }
}
