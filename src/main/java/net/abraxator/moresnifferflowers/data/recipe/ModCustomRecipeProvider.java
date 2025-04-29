package net.abraxator.moresnifferflowers.data.recipe;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.data.recipe.builder.CropressingRecipeBuilder;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModCustomRecipeProvider extends RecipeProvider {
    public ModCustomRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        createCropressing(output, ModItems.CROPRESSED_CARROT.get(), Items.CARROT);
        createCropressing(output, ModItems.CROPRESSED_POTATO.get(), Items.POTATO);
        createCropressing(output, ModItems.CROPRESSED_NETHERWART.get(), Items.NETHER_WART);
        createCropressing(output, ModItems.CROPRESSED_BEETROOT.get(), Items.BEETROOT);
        createCropressing(output, ModItems.CROPRESSED_WHEAT.get(), Items.WHEAT);

    }

    public void createCropressing(RecipeOutput recipeOutput, ItemLike result, ItemLike crop) {
        new CropressingRecipeBuilder(result).requiresCrop(crop.asItem()).unlockedBy("has_cropressor", has(ModBlocks.CROPRESSOR_OUT.get())).save(recipeOutput, getItemName(result) + "_from_cropressing");
    }

    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModCustomRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return MoreSnifferFlowers.MOD_ID;
        }
    }
}
