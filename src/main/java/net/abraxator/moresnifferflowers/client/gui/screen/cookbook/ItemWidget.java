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
    private final boolean unlocked;
    
    public ItemWidget(int x, int y, Component message, Nutrition nutrition, CookbookScreen screen) {
        super(x, y, 16, 16, message);
        this.nutrition = nutrition;
        this.screen = screen;
        this.unlocked = !nutrition.isEmpty();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (unlocked) {
            guiGraphics.renderItem(nutrition.getItem().getDefaultInstance(), this.getX(), this.getY());

            if (this.isHovered()) {
                screen.renderNutritionInfo(guiGraphics, nutrition);
            }
        } else {
            guiGraphics.blit(CookbookScreen.RENDERABLES, this.getX(), this.getY(), 156, 0, 16, 16);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
