package net.abraxator.moresnifferflowers.data.recipe.builder;

import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CropressingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private Ingredient ingredient;
    private int count;
    protected String group;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public CropressingRecipeBuilder(ItemLike result) {
        this.result = result.asItem();
    }

    public RecipeBuilder requiresCrop(Item crop) {
        this.ingredient = Ingredient.of(new ItemStack(crop).getItem());
        this.count = 16;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        this.ensureValid(resourceKey);
        Advancement.Builder advancement = output.advancement()
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                        .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                        .requirements(AdvancementRequirements.Strategy.OR);
        CropressingRecipe cropressingRecipe = new CropressingRecipe(this.ingredient, this.count, this.result.getDefaultInstance());
        
        this.criteria.forEach(advancement::addCriterion);
        output.accept(resourceKey, cropressingRecipe, advancement.build(resourceKey.location().withPrefix("recipes/" + "misc" + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> recipe) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipe.location());
        }
    }
}
