package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;


import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.security.auth.x500.X500Principal;

public class ItemWidget extends AbstractWidget {
    private final Nutrition nutrition;
    private final CookbookScreen screen;

    public ItemWidget(int x, int y, int width, int height, Component message, Nutrition nutrition, CookbookScreen screen) {
        super(x, y, width, height, message);
        this.nutrition = nutrition;
        this.screen = screen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.renderItem(nutrition.getItem().getDefaultInstance(), this.getX(), this.getY());
        
        if(this.isHovered()) {
            screen.renderNutritionInfo(guiGraphics, nutrition);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
