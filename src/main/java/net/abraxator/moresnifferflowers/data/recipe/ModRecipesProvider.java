package net.abraxator.moresnifferflowers.data.recipe;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.data.recipe.builder.CropressingRecipeBuilder;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.recipes.RebrewedTippedArrowRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModRecipesProvider extends RecipeProvider {
    private final HolderGetter<Item> items;
    protected ModRecipesProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    protected void buildRecipes() {
        createCropressing(output, ModItems.CROPRESSED_CARROT.get(), Items.CARROT);
        createCropressing(output, ModItems.CROPRESSED_POTATO.get(), Items.POTATO);
        createCropressing(output, ModItems.CROPRESSED_NETHERWART.get(), Items.NETHER_WART);
        createCropressing(output, ModItems.CROPRESSED_BEETROOT.get(), Items.BEETROOT);
        createCropressing(output, ModItems.CROPRESSED_WHEAT.get(), Items.WHEAT);

        MSFSmithingTrims().forEach(p_378952_ -> this.trimSmithing(p_378952_.template(), p_378952_.id()));

        trimCrafting(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.AMBER_SHARD.get());
        trimCrafting(ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.GARNET_SHARD.get());
        trimCrafting(ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_NETHERWART.get());
        trimCrafting(ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_CARROT.get());
        trimCrafting(ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_POTATO.get());
        trimCrafting(ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_WHEAT.get());
        trimCrafting(ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModItems.CROPRESSED_BEETROOT.get());

        shaped(RecipeCategory.MISC, ModItems.EXTRACTION_BOTTLE.get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.GLASS)
                .unlockedBy("has_amethyst", has(Items.AMETHYST_SHARD))
                .save(this.output);

        //threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_BLOCK.get(), ModItems.AMBER_SHARD.get());
        twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC.get(), ModItems.AMBER_SHARD.get());
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC_SLAB.get(), ModBlocks.AMBER_MOSAIC.get());
        stairBuilder(ModBlocks.AMBER_MOSAIC_STAIRS, Ingredient.of(ModBlocks.AMBER_MOSAIC))
                .unlockedBy("has_amber_mosaic", has(ModBlocks.AMBER_MOSAIC))
                .save(this.output);
        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.AMBER_MOSAIC_WALL.get(), ModBlocks.AMBER_MOSAIC.get());
        chiseled(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_AMBER.get(), ModBlocks.AMBER_MOSAIC_SLAB.get());
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_AMBER_SLAB.get(), ModBlocks.CHISELED_AMBER.get());
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.AMBER_MOSAIC.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRACKED_AMBER.get().asItem(), 0.1F, 200)
                .unlockedBy("has_amber_mosaic", has(ModBlocks.AMBER_MOSAIC))
                .save(this.output);

        //threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_BLOCK.get(), ModItems.GARNET_SHARD.get());
        twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC.get(), ModItems.GARNET_SHARD.get());
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC_SLAB.get(), ModBlocks.GARNET_MOSAIC.get());
        stairBuilder(ModBlocks.GARNET_MOSAIC_STAIRS, Ingredient.of(ModBlocks.GARNET_MOSAIC))
                .unlockedBy("has_garnet_mosaic", has(ModBlocks.GARNET_MOSAIC))
                .save(this.output);
        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GARNET_MOSAIC_WALL.get(), ModBlocks.GARNET_MOSAIC.get());
        chiseled(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GARNET.get(), ModBlocks.GARNET_MOSAIC_SLAB.get());
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GARNET_SLAB.get(), ModBlocks.CHISELED_GARNET.get());
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.GARNET_MOSAIC.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRACKED_GARNET.get().asItem(), 0.1F, 200)
                .unlockedBy("has_garnet_mosaic", has(ModBlocks.GARNET_MOSAIC))
                .save(this.output);


        shapeless(RecipeCategory.MISC, ModItems.CROPRESSOR.get())
                .requires(ModItems.TUBE_PIECE.get())
                .requires(ModItems.SCRAP_PIECE.get())
                .requires(ModItems.ENGINE_PIECE.get())
                .requires(ModItems.PRESS_PIECE.get())
                .requires(ModItems.BELT_PIECE.get())
                .unlockedBy("has_cropressor_piece", has(ModTags.ModItemTags.CROPRESSOR_PIECES))
                .save(this.output);

        shaped(RecipeCategory.MISC, ModItems.REBREWING_STAND.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern("BCB")
                .define('A', ModItems.CROPRESSED_NETHERWART.get())
                .define('B', ModItems.BROKEN_REBREWING_STAND.get())
                .define('C', ModItems.TUBE_PIECE.get())
                .unlockedBy("has_broken_rebrewing_stand", has(ModItems.BROKEN_REBREWING_STAND.get()))
                .save(this.output);

        partsRecycling(ModItems.BELT_PIECE.get(), Items.LEATHER, 8);
        partsRecycling(ModItems.SCRAP_PIECE.get(), Items.COPPER_INGOT, 8);
        partsRecycling(ModItems.ENGINE_PIECE.get(), Items.GOLD_INGOT, 8);
        partsRecycling(ModItems.TUBE_PIECE.get(), Items.IRON_INGOT, 8);
        partsRecycling(ModItems.PRESS_PIECE.get(), Items.NETHERITE_SCRAP, 1);
        partsRecycling(ModItems.BROKEN_REBREWING_STAND.get(), ModItems.CROPRESSED_NETHERWART.get(), 4);

        partsRecycling(ModItems.CROPRESSED_BEETROOT.get(), Items.BEETROOT, 16);
        partsRecycling(ModItems.CROPRESSED_CARROT.get(), Items.CARROT, 16);
        partsRecycling(ModItems.CROPRESSED_POTATO.get(), Items.POTATO, 16);
        partsRecycling(ModItems.CROPRESSED_WHEAT.get(), Items.WHEAT, 16);
        partsRecycling(ModItems.CROPRESSED_NETHERWART.get(), Items.NETHER_WART, 16);



        planksFromLogs(ModBlocks.CORRUPTED_PLANKS, ModTags.ModItemTags.CORRUPTED_LOGS, 4);
        woodFromLogs(ModBlocks.CORRUPTED_WOOD, ModBlocks.CORRUPTED_LOG);
        woodFromLogs(ModBlocks.STRIPPED_CORRUPTED_WOOD, ModBlocks.STRIPPED_CORRUPTED_LOG);
        stairBuilder(ModBlocks.CORRUPTED_STAIRS, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CORRUPTED_SLAB, ModBlocks.CORRUPTED_PLANKS);
        fenceBuilder(ModBlocks.CORRUPTED_FENCE, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        fenceGateBuilder(ModBlocks.CORRUPTED_FENCE_GATE, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        doorBuilder(ModBlocks.CORRUPTED_DOOR, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        trapdoorBuilder(ModBlocks.CORRUPTED_TRAPDOOR, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        pressurePlate(ModBlocks.CORRUPTED_PRESSURE_PLATE, ModBlocks.CORRUPTED_PLANKS);
        buttonBuilder(ModBlocks.CORRUPTED_BUTTON, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        woodenBoat(ModItems.CORRUPTED_BOAT.get(), ModBlocks.CORRUPTED_PLANKS.get());
        chestBoat(ModItems.CORRUPTED_CHEST_BOAT.get(), ModItems.CORRUPTED_BOAT.get());
        signBuilder(ModBlocks.CORRUPTED_SIGN, Ingredient.of(ModBlocks.CORRUPTED_PLANKS))
                .unlockedBy("has_corrupted_planks", has(ModBlocks.CORRUPTED_PLANKS))
                .save(this.output);
        hangingSign(ModItems.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_PLANKS.get());

        planksFromLogs(ModBlocks.VIVICUS_PLANKS, ModTags.ModItemTags.VIVICUS_LOGS, 4);
        woodFromLogs(ModBlocks.VIVICUS_WOOD, ModBlocks.VIVICUS_LOG);
        woodFromLogs(ModBlocks.STRIPPED_VIVICUS_WOOD, ModBlocks.STRIPPED_VIVICUS_LOG);
        stairBuilder(ModBlocks.VIVICUS_STAIRS, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.VIVICUS_SLAB, ModBlocks.VIVICUS_PLANKS);
        fenceBuilder(ModBlocks.VIVICUS_FENCE, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        fenceGateBuilder(ModBlocks.VIVICUS_FENCE_GATE, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        doorBuilder(ModBlocks.VIVICUS_DOOR, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        trapdoorBuilder(ModBlocks.VIVICUS_TRAPDOOR, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        pressurePlate(ModBlocks.VIVICUS_PRESSURE_PLATE, ModBlocks.VIVICUS_PLANKS);
        buttonBuilder(ModBlocks.VIVICUS_BUTTON, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_VIVICUS_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        woodenBoat(ModItems.VIVICUS_BOAT.get(), ModBlocks.VIVICUS_PLANKS.get());
        chestBoat(ModItems.VIVICUS_CHEST_BOAT.get(), ModItems.VIVICUS_BOAT.get());
        signBuilder(ModBlocks.VIVICUS_SIGN, Ingredient.of(ModBlocks.VIVICUS_PLANKS))
                .unlockedBy("has_vivicus_planks", has(ModBlocks.VIVICUS_PLANKS))
                .save(this.output);
        hangingSign(ModItems.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_PLANKS.get());
        
        shaped(RecipeCategory.MISC, ModItems.VIVICUS_ANTIDOTE, 1)
                        .pattern(" AB")
                        .pattern("ACA")
                        .pattern("DA ")
                        .define('A', Tags.Items.GLASS_BLOCKS_COLORLESS)
                        .define('B', ModItems.JAR_OF_ACID)
                        .define('C', ModItems.CORRUPTED_BOBLING_CORE)
                        .define('D', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_jar_of_acid", has(ModItems.JAR_OF_ACID))
                        .save(this.output);

        SpecialRecipeBuilder.special(RebrewedTippedArrowRecipe::new).save(this.output,"rebrewed_tipped_arrow");
        
    }

    private void trimCrafting(ItemLike trim, Item ingredient) {
        shaped(RecipeCategory.MISC, trim, 2)
                .pattern("ABA")
                .pattern("ACA")
                .pattern("AAA")
                .define('A', Items.DIAMOND)
                .define('B', trim)
                .define('C', ingredient)
                .unlockedBy("has_" + getItemName(trim) + "_trim_template", has(ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get()))
                .save(this.output);
    }

    private void partsRecycling(Item part, Item result, int count) {
        shapeless(RecipeCategory.MISC, result, count)
                .requires(part)
                .unlockedBy("has_" + getItemName(part), has(part))
                .save(this.output);
    }

    protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count) {
        return ShapedRecipeBuilder.shaped(this.items, category, result, count);
    }

    public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
        return ShapelessRecipeBuilder.shapeless(this.items, category, result);
    }

    public static Stream<VanillaRecipeProvider.TrimTemplate> MSFSmithingTrims() {
        return Stream.of(
                        ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE.get(),
                        ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE.get()
                )
                .map(p_378953_ -> new VanillaRecipeProvider.TrimTemplate(
                                p_378953_, ResourceKey.create(Registries.RECIPE, MoreSnifferFlowers.loc(getItemName(p_378953_) + "_smithing_trim")))
                );
    }

    public void createCropressing(RecipeOutput recipeOutput, ItemLike result, ItemLike crop) {
        new CropressingRecipeBuilder(result)
                .requiresCrop(crop.asItem())
                .unlockedBy("has_cropressor", has(ModBlocks.CROPRESSOR_OUT.get()))
                .save(recipeOutput, getItemName(result) + "_from_cropressing");
    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from the `GatherDataEvent`s.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipesProvider(provider, output);
        }

        @Override
        public String getName() {
            return MoreSnifferFlowers.MOD_ID;
        }
    }
}
