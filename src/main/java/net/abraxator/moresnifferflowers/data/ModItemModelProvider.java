package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class ModItemModelProvider extends ModelProvider {
    public ModItemModelProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateSpawnEgg(ModItems.BOBLING_SPAWN_EGG.get(), 0x312f35, 0xa55f85);
        itemModels.generateFlatItem(ModItems.CORRUPTED_SIGN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CORRUPTED_HANGING_SIGN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.VIVICUS_SIGN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.VIVICUS_HANGING_SIGN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CORRUPTED_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CORRUPTED_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.VIVICUS_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.VIVICUS_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);

    }
}
