package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.data.advancement.ModAdvancementProvider;
import net.abraxator.moresnifferflowers.data.datamaps.ModDataMapsProvider;
import net.abraxator.moresnifferflowers.data.loot.ModLootModifierProvider;
import net.abraxator.moresnifferflowers.data.loot.ModLoottableProvider;
import net.abraxator.moresnifferflowers.data.recipe.ModCustomRecipeProvider;
import net.abraxator.moresnifferflowers.data.recipe.ModRecipesProvider;
import net.abraxator.moresnifferflowers.data.tag.ModBannerPatternTagsProvider;
import net.abraxator.moresnifferflowers.data.tag.ModBlockTagsProvider;
import net.abraxator.moresnifferflowers.data.tag.ModItemTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var packOutput = generator.getPackOutput();
        var future = event.getLookupProvider();
        var datapackProvider = new RegistryDataGenerator(packOutput, event.getLookupProvider());

        //BLOCKMODELS
      //  event.createProvider(ModBlockStateGenerator::new);
        event.createProvider(ModItemModelProvider::new);
        
        //SOUNDS
        event.createProvider(ModSoundProvider::new);
        
        //DATAPACK REGISTRIES
        event.createProvider(RegistryDataGenerator::new);
        
        //DATA MAPS
        event.createProvider(ModDataMapsProvider::new);
        
        //LOOT
        event.createProvider(ModLootModifierProvider::new);

        event.createProvider((output, lookupProvider) -> ModLoottableProvider.create(packOutput, registries));

        //TAGS
        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModItemTagsProvider::new);

        // event.createProvider(ModPaintingTagsProvider::new);
        // event.createProvider(ModBannerPatternTagsProvider::new);
        // event.createProvider(ModBiomeTagProvider::new);
        event.createProvider(ModBannerPatternTagsProvider::new);
        
        //ADVANCEMENTS
        event.createProvider((output, lookupProvider) -> new  ModAdvancementProvider(packOutput, registries));

        //RECIPES
        event.createProvider(ModRecipesProvider.Runner::new);
        event.createProvider(ModCustomRecipeProvider.Runner::new);
        
        //LANG
        //event.createProvider(ModLangProvider::new);
    }
}
