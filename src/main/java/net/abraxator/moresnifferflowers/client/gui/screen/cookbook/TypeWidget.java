package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class TypeWidget extends AbstractWidget {
    final NutritionType type;
    final private CookbookScreen screen;
    
    public TypeWidget(int x, int y, NutritionType type, int width, int height, Component message, CookbookScreen screen) {
        super(x, y, width, height, message);
        this.type = type;
        this.screen = screen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isMouseOver(mouseX,mouseY)){
            guiGraphics.blit(CookbookScreen.RENDERABLES, this.getX(), this.getY(), 200, this.type.ordinal() * 24, this.width, this.height);
        } else
            guiGraphics.blit(CookbookScreen.RENDERABLES, this.getX(), this.getY(), 176, this.type.ordinal() * 24, this.width, this.height);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.screen.pageToItems(this.type);
        screen.type =  this.type;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
