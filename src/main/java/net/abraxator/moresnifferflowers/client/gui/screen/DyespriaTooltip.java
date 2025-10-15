package net.abraxator.moresnifferflowers.client.gui.screen;

import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class DyespriaTooltip implements TooltipComponent {
    public ItemStack stack;
    public boolean isPatternspria;
    public int dyespriaMode;

    public DyespriaTooltip(ItemStack stack, boolean isPatternspria, int dyespriaMode) {
        this.stack = stack;
        this.isPatternspria = isPatternspria;
        this.dyespriaMode = dyespriaMode;
    }
}
