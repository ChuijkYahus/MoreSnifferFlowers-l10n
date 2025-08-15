package net.abraxator.moresnifferflowers.compat.jei.corruption;

import com.google.common.collect.Maps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CorruptionCategory implements IRecipeCategory<CorruptionRecipe> {
    public static final RecipeType<CorruptionRecipe> CORRUPTING = RecipeType.create(MoreSnifferFlowers.MOD_ID, "corrupting", CorruptionRecipe.class);
    private final IDrawable icon;
    private final Component localizedName;
    
    public CorruptionCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableItemStack(ModItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.corrupting_category", "Corrupting");
    }

    @Override
    public RecipeType<CorruptionRecipe> getRecipeType() {
        return CORRUPTING;
    }


    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 40;
    }


    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptionRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 15).addItemStack(recipe.source());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 15).addItemStack(recipe.corrupted()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
            if (recipe.corrupted().isEmpty()) {
                tooltip.add(Component.literal("Air").withStyle(ChatFormatting.WHITE));
            }
        });
    }

    @Override
    public void draw(CorruptionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(MoreSnifferFlowers.loc("textures/gui/container/corrupting_jei.png"), 0,0, 0 ,0 ,getWidth() ,getHeight());

        Minecraft minecraft = Minecraft.getInstance();
        String text = "Chance: " + recipe.chance() + "%";
        int width = minecraft.font.width(text);
        int x = getWidth() - 30 - width;
        int y = 0;
        guiGraphics.drawString(minecraft.font, text, x, y, 0x714a5f, false);

    }
}
