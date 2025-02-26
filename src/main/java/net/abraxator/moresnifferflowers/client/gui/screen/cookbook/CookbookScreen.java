package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CookbookScreen extends Screen {
    private static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/gui/cookbook.png");
    private final int ROWS = 8;
    private final int COLUMNS = 5;
    private final int DISPLAYED_RECIPES = ROWS * COLUMNS;
    private final List<String> mods;
    private Page page = Page.CONTENTS;
    private String modid = "moresnifferflowers";
    private List<Nutrition> nutritions = new ArrayList<>();
    private float scrollOffs;
    private int startIndex;
    private boolean isScrolling;
    
    public CookbookScreen() {
        super(Component.empty());
        this.mods = new ArrayList<>(NutritionLoader.modNutritions.keySet());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        guiGraphics.pose().scale(2,1,1);
        this.renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, x/2, y, 0, 0, 272/2, 180);
        guiGraphics.pose().scale(0.5F,1,1);

        if(page == Page.CONTENTS) {
            this.renderContents(guiGraphics, mouseX, mouseY, x, y);
        } else {
            this.renderItems(guiGraphics, mouseX, mouseY, x, y);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void renderNutritionInfo(GuiGraphics guiGraphics, Nutrition nutrition) { 
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        int xPos = x + 150;
        int yPos = y + 20;
        ItemStack item = nutrition.getItem().getDefaultInstance();
        
        guiGraphics.drawString(font, item.getDisplayName().getString(), xPos, yPos, ChatFormatting.BLACK.getColor());
        for (NutritionEntry nutritionEntry : nutrition.getNutritionEntries()) {
            yPos += 10;
            guiGraphics.drawString(font, nutritionEntry.nutrition().name + ": " + nutritionEntry.weight(), xPos, yPos, nutritionEntry.nutrition().color);
        }
    }
    
    private void renderItems(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        int xPos = x + 19;
        int yPos = y + 16;
        
        for (int i = startIndex; i < startIndex + DISPLAYED_RECIPES && i < this.nutritions.size(); i++) {
            addRenderableWidget(new ItemWidget(xPos, yPos, 16, 16, Component.empty(), nutritions.get(i), this));

            if (i % COLUMNS != 0) {
                xPos += 18;
            } else {
                yPos += 18;
            }
        }
    }

    private void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        for (int i = 1; i < mods.size() + 1; i++) {
            this.addRenderableWidget(Button.builder(Component.literal(mods.get(i - 1)), button -> {
                modid = button.getMessage().getString().replaceAll("literal\\{|}", "");
                this.nutritions = NutritionLoader.modNutritions.get(modid);
                this.turnPage(Page.ITEMS);
            }).pos(x + 30, y + i * 10).size(50, 10).build());
        }
    }

    private void turnPage(Page page) {
        this.page = page;
        this.clearWidgets();
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        this.isScrolling = false;
        
        if(isMouseOver(mouseX, mouseY, x + 115, y + 15, 7, 72) && this.page == Page.ITEMS) {
            this.isScrolling = true;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(this.page == Page.ITEMS && this.isScrolling) {
            int y = (this.height - 180) / 2;
            int i = y + 15;
            int j = i + 72;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.startIndex) + 0.5D) * 5;
            return true;
        }
        
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(this.getTotalRowCount() > ROWS) {
            float f = (float)delta / (float)this.getTotalRowCount();
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * this.getTotalRowCount()) + 0.5) * COLUMNS;
        }
        
        return true;
    }
    
    private int getTotalRowCount() {
        return Mth.positiveCeilDiv(this.nutritions.size(), 5);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
    
    public enum Page {
        CONTENTS,
        ITEMS
    }
}
