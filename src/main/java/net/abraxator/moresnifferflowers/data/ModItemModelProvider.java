package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModItemModelProvider extends ModelProvider {
    public ModItemModelProvider(PackOutput output) {
        super(output, MoreSnifferFlowers.MOD_ID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateSpawnEgg(ModItems.BOBLING_SPAWN_EGG.get(), 0x312f35, 0xa55f85);

        flatItemStream().forEach(itemHolder -> itemModels.generateFlatItem(itemHolder.value(), ModelTemplates.FLAT_ITEM));
    }

    protected Stream<? extends Holder<Item>> flatItemStream() {
        return Stream.of(ModItems.DAWNBERRY_VINE_SEEDS, ModItems.GLOOMBERRY_VINE_SEEDS, ModItems.DAWNBERRY, ModItems.GLOOMBERRY,
                ModItems.AMBUSH_SEEDS, ModItems.GARBUSH_SEEDS, ModItems.AMBUSH_BANNER_PATTERN, ModItems.EVIL_BANNER_PATTERN,
                ModItems.AMBER_SHARD, ModItems.GARNET_SHARD, ModItems.AROMA_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE,
                ModItems.BEAT_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.TATER_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE, ModItems.NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE,
                ModItems.DRAGONFLY, ModItems.DYESCRAPIA, ModItems.DYESPRIA_SEEDS,
                ModItems.BONMEELIA_SEEDS, ModItems.JAR_OF_BONMEEL, ModItems.BONWILTIA_SEEDS, ModItems.JAR_OF_ACID,
                ModItems.ACIDRIPIA_SEEDS, ModItems.BONDRIPIA_SEEDS, ModItems.PRESS_PIECE, ModItems.TUBE_PIECE,
                ModItems.SCRAP_PIECE, ModItems.ENGINE_PIECE, ModItems.BELT_PIECE, ModItems.REBREWING_STAND,
                ModItems.BROKEN_REBREWING_STAND, ModItems.VIVICUS_ANTIDOTE, ModItems.BOBLING_CORE, ModItems.CORRUPTED_BOBLING_CORE,
                ModItems.CORRUPTED_SLIME_BALL, ModItems.CORRUPTED_SIGN, ModItems.CORRUPTED_HANGING_SIGN, ModItems.CORRUPTED_BOAT,
                ModItems.CORRUPTED_CHEST_BOAT, ModItems.VIVICUS_SIGN, ModItems.VIVICUS_HANGING_SIGN, ModItems.VIVICUS_BOAT,
                ModItems.VIVICUS_CHEST_BOAT, ModItems.CAULORFLOWER_SEEDS, ModItems.WAND_OF_CUBING, ModItems.EXTRACTION_BOTTLE
        );
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.concat(flatItemStream(), Stream.of(
                ModItems.BOBLING_SPAWN_EGG

        ));
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of();
    }
}
