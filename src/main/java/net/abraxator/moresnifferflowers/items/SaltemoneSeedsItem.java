package net.abraxator.moresnifferflowers.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

}
