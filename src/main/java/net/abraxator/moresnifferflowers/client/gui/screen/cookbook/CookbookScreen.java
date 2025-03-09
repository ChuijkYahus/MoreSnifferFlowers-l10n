package net.abraxator.moresnifferflowers.client.gui.screen.cookbook;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CookbookScreen extends Screen {
    private static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/gui/cookbook.png");
    protected static final ResourceLocation RENDERABLES = MoreSnifferFlowers.loc("textures/gui/cookbook_renderables.png");
    private final int ROWS = 8;
    private final int COLUMNS = 5;
    private final int PAGE_SIZE = ROWS * COLUMNS;
    private final int SCROLLBAR_HEIGHT = 142;
    private final int SCROLLER_HEIGHT = 15;
    private final List<String> mods;
    private final Set<Item> unlocked;
    private Page page = Page.CONTENTS;
    private NutritionType type;
    private List<Nutrition> nutritions = new ArrayList<>();
    private float scrollOffs;
    private int startIndex;
    private boolean isScrolling;
    
    public CookbookScreen(Set<Item> unlocked) {
        super(Component.empty());
        this.mods = new ArrayList<>(NutritionLoader.modNutritions.keySet());
        this.unlocked = unlocked;
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
        int xPos = 18;
        int yPos = 16;
        guiGraphics.blit(RENDERABLES, x + 17, y + 15, 25, 0, 111, 144);
        
        for (int i = startIndex + 1; i < startIndex + 1 + PAGE_SIZE && i < this.nutritions.size() + 1; i++) {
            Nutrition nutrition = nutritions.get(i - 1);
            boolean unlocked = this.unlocked.contains(nutrition.getItem());
            
            nutrition = unlocked ? nutrition : Nutrition.EMPTY;
            addRenderableWidget(new ItemWidget(x + xPos, y + yPos, Component.empty(), nutrition, this));

            if (i % COLUMNS != 0) {
                xPos += 18;
            } else {
                yPos += 18;
                xPos = 18;
            }
        }
        
        boolean scrollable = this.nutritions.size() > PAGE_SIZE;
        int scrollAmount = (int) ((SCROLLBAR_HEIGHT - SCROLLER_HEIGHT) * this.scrollOffs);
        guiGraphics.blit(RENDERABLES, x + 115, y + 16 + scrollAmount, scrollable ? 0 : 12, 0, 12, 15);
    }

    private void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        for (int i = 0; i < NutritionType.values().length; i++) {
            NutritionType type = NutritionType.byId(i);
            this.addRenderableWidget(new TypeWidget(x + 50, y + (18 * i + 5), type, 18, 18, Component.literal(type.name), this));
        }
    }
    
    public void pageToItems(NutritionType type) {
        this.type = type;
        this.nutritions = NutritionLoader.typeNutritions.get(this.type);
        this.nutritions.sort(Comparator.comparingInt(value -> this.unlocked.contains(value.getItem()) ? 0 : 1));
        this.turnPage(Page.ITEMS);
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
            int i = y + 16;
            int j = i + 157;
            int totalRows = Math.max(0, (this.nutritions.size() + 4) / 5 - ROWS);

            this.scrollOffs = ((float) mouseY - (float) i) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) (this.scrollOffs * totalRows) * 5;
            
            clearWidgets();
            return true;
        }
        
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int totalItems = this.nutritions.size();
        int maxRows = Math.max(1, (int) Math.ceil((double) totalItems / COLUMNS));
        int scrollableRows = Math.max(0, maxRows - ROWS);

        if (scrollableRows > 0) {
            float f = (float) delta / (float) scrollableRows;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            
            int maxStartIndex = (maxRows - ROWS) * COLUMNS;
            this.startIndex = Math.min((int)((this.scrollOffs * scrollableRows) * COLUMNS), maxStartIndex);

            clearWidgets();
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
