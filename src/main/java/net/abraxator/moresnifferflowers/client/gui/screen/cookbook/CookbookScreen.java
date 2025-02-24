package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CookbookScreen extends Screen {
    private static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/gui/cookbook.png");
    private final List<String> mods;
    private Page page;
    private String modid = "moresnifferflowers";
    private List<Nutrition> nutritions;
    
    public CookbookScreen() {
        super(Component.empty());
        this.mods = NutritionLoader.modNutritions.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = (this.width - 272) / 2;
        int y = (this.height - 180) / 2;
        this.renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, x, y, 0, 0, 272, 180);
        if(page == Page.CONTENTS) {
            this.renderContents(guiGraphics, mouseX, mouseY, x, y);
        } else {
            this.renderItems(guiGraphics, mouseX, mouseY, x, y);
        }
        this.renderWindow(guiGraphics, mouseX, mouseY, x, y);
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
        
        for (int i = 0; i < nutritions.size(); i++) {
            addRenderableWidget(new ItemWidget(xPos, yPos, 16, 16, Component.empty(), nutritions.get(i), this));

            if (i % 5 != 0) {
                xPos += 18;
            } else {
                yPos += 18;
            }
        }
    }

    private void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        for (int i = 1; i < mods.size() + 1; i++) {
            this.addRenderableWidget(Button.builder(Component.literal(mods.get(i - 1)), button -> {
                modid = button.getMessage().toString();
                this.page = Page.ITEMS;
                this.nutritions = NutritionLoader.modNutritions.get(modid);
            }).pos(x + 30, y + i * 10).size(50, 10).build());
        }
    }

    private void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {

        NutritionLoader.modNutritions.get(modid).forEach(nutrition -> {
            
        });
    }

    public enum Page {
        CONTENTS,
        ITEMS
    }
}
