package net.abraxator.moresnifferflowers.compat.jei.corruption;

import com.google.common.collect.Maps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CorruptionCategory implements IRecipeCategory<CorruptionRecipe> {
    public static final RecipeType<CorruptionRecipe> CORRUPTION = RecipeType.create(MoreSnifferFlowers.MOD_ID, "corruption", CorruptionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    
    public CorruptionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(MoreSnifferFlowers.loc("textures/gui/container/corrupting_jei.png"), 0, 0, 120, 40);
        this.icon = helper.createDrawableItemStack(ModItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.corrupting_category", "Corrupting");
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
    public RecipeType<CorruptionRecipe> getRecipeType() {
        return CORRUPTION;
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
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptionRecipe recipe, IFocusGroup focuses) {
        if(recipe.tagOrBlock()) {
            TagKey<Block> tagKey = recipe.inputTag().get();
            List<ItemStack> items = StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(tagKey).spliterator(), false)
                    .map(Holder::get)
                    .filter(block -> !block.defaultBlockState().is(ModTags.ModBlockTags.UNCORRUPTABLE))
                    .map(block -> block.asItem().getDefaultInstance())
                    .filter(itemStack -> !itemStack.isEmpty())
                    .toList();
            builder.addSlot(RecipeIngredientRole.INPUT, 10, 15).addItemStacks(items);
        } else {
            if (!recipe.inputBlock().get().defaultBlockState().is(ModTags.ModBlockTags.UNCORRUPTABLE)) builder.addSlot(RecipeIngredientRole.INPUT, 10, 15).addItemStack(recipe.inputBlock().get().asItem().getDefaultInstance());
        }
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 15)
                .addItemStacks(recipe.list().stream().map(entry -> entry.block().asItem().getDefaultInstance()).collect(Collectors.toList()))
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    Map<Item, Integer> map = Util.make(Maps.newHashMap(),o -> {
                            recipe.list()
                                    .stream()
                                    .map(entry -> Map.entry(entry.block().asItem().getDefaultInstance(), entry.weight()))
                                    .forEach(entry -> o.put(entry.getKey().getItem(), entry.getValue()));
                    });
                    float weight = 100;
                    int totalWeight = recipe.list().stream().mapToInt(CorruptionRecipe.Entry::weight).sum();
                    Optional<ItemStack> current = recipeSlotView.getDisplayedItemStack();
                    if(current.isPresent()) {
                        weight = map.getOrDefault(current.get().getItem(), -5);
                    }
                    float percentage = Math.round((weight / totalWeight) * 100);
                    tooltip.add(FormattedText.of("Chance: " + percentage + "%"));
                });
    }
}
