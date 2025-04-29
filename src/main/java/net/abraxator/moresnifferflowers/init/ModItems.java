package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MoreSnifferFlowers.MOD_ID);
    public static final DeferredItem<Item> DAWNBERRY_VINE_SEEDS = ITEMS.register("dawnberry_vine_seeds", () -> new BlockItem(ModBlocks.DAWNBERRY_VINE.get(), msfItemProperties("dawnberry_vine_seeds").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> GLOOMBERRY_VINE_SEEDS = ITEMS.register("gloomberry_vine_seeds", () -> new BlockItem(ModBlocks.GLOOMBERRY_VINE.get(), msfItemProperties("gloomberry_vine_seeds").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> DAWNBERRY = ITEMS.register("dawnberry", () -> new Item(msfItemProperties("dawnberry").food(ModFoods.DAWNBERRY, ModFoods.ModConsumables.FAST_CONSUMABLE)));
    public static final DeferredItem<Item> GLOOMBERRY = ITEMS.register("gloomberry", () -> new Item(msfItemProperties("gloomberry").food(ModFoods.GLOOMBERRY, ModFoods.ModConsumables.GLOOMBERRY)));

    public static final DeferredItem<Item> AMBUSH_SEEDS = ITEMS.register("ambush_seeds", () -> new BlockItem(ModBlocks.AMBUSH_BOTTOM.get(), msfItemProperties("ambush_seeds").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> GARBUSH_SEEDS = ITEMS.register("garbush_seeds", () -> new BlockItem(ModBlocks.GARBUSH_BOTTOM.get(), msfItemProperties("garbush_seeds").useItemDescriptionPrefix()));

    public static final DeferredItem<Item> AMBUSH_BANNER_PATTERN = ITEMS.register("ambush_banner_pattern", () -> new BannerPatternItem(ModTags.ModBannerPatternTags.AMBUSH_BANNER_PATTERN, msfItemProperties("ambush_banner_pattern").stacksTo(1)));
    public static final DeferredItem<Item> EVIL_BANNER_PATTERN = ITEMS.register("evil_banner_pattern", () -> new BannerPatternItem(ModTags.ModBannerPatternTags.EVIL_BANNER_PATTERN, msfItemProperties("evil_banner_pattern").stacksTo(1)));

    public static final DeferredItem<Item> AMBER_SHARD = ITEMS.register("amber_shard", () -> new TrimMaterialItem(msfItemProperties("")));
    public static final DeferredItem<Item> GARNET_SHARD = ITEMS.register("garnet_shard", () -> new TrimMaterialItem(msfItemProperties("")));

    public static final DeferredItem<Item> AROMA_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("aroma_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("aroma_armor_trim_smithing_template")));
    public static final DeferredItem<Item> CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carnage_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("carnage_armor_trim_smithing_template")));
    public static final DeferredItem<Item> DRAGONFLY = ITEMS.register("dragonfly", () -> new DragonflyItem(msfItemProperties("dragonfly")));
    public static final DeferredItem<Item> DYESPRIA = ITEMS.register("dyespria", () -> new DyespriaItem(msfItemProperties("dyespria").stacksTo(1)));
    public static final DeferredItem<Item> DYESCRAPIA = ITEMS.register("dyescrapia", () -> new DyescrapiaItem(msfItemProperties("dyescrapia").stacksTo(1)));
    public static final DeferredItem<Item> DYESPRIA_SEEDS = ITEMS.register("dyespria_seeds", () -> new BlockItem(ModBlocks.DYESPRIA_PLANT.get(), msfItemProperties("dyespria_seeds").useItemDescriptionPrefix()) {
        @Override
        public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.dyespria_seeds", "Shear to hide dye").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> BONMEELIA_SEEDS = ITEMS.register("bonmeelia_seeds", () -> new BlockItem(ModBlocks.BONMEELIA.get(), msfItemProperties("bonmeelia_seeds").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> JAR_OF_BONMEEL = ITEMS.register("jar_of_bonmeel", () -> new JarOfBonmeelItem(msfItemProperties("jar_of_bonmeel")));
    public static final DeferredItem<Item> BONDRIPIA_SEEDS = ITEMS.register("bondripia_seeds", () -> new BlockItem(ModBlocks.BONDRIPIA.get(), msfItemProperties("bondripia_seeds")) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.bondripia_seeds", "Plantable underneath an area of 5 blocks in a + shape").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> BONWILTIA_SEEDS = ITEMS.register("bonwiltia_seeds", () -> new BlockItem(ModBlocks.BONWILTIA.get(), msfItemProperties("bonwiltia_seeds").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> JAR_OF_ACID = ITEMS.register("jar_of_acid", () -> new JarOfAcidItem(msfItemProperties("jar_of_acid")) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.acid_jar", "Ungrows organic blocks").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> ACIDRIPIA_SEEDS = ITEMS.register("acidripia_seeds", () -> new BlockItem(ModBlocks.ACIDRIPIA.get(), msfItemProperties("acidripia_seeds").useItemDescriptionPrefix()));

    public static final DeferredItem<Item> CROPRESSOR = ITEMS.register("cropressor", () -> new CropressorItem(ModBlocks.CROPRESSOR_OUT.get(), msfItemProperties("cropressor").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> TUBE_PIECE = ITEMS.register("tube_piece", () -> new Item(msfItemProperties("tube_piece")));
    public static final DeferredItem<Item> BELT_PIECE = ITEMS.register("belt_piece", () -> new Item(msfItemProperties("belt_piece")));
    public static final DeferredItem<Item> SCRAP_PIECE = ITEMS.register("scrap_piece", () -> new Item(msfItemProperties("scrap_piece")));
    public static final DeferredItem<Item> ENGINE_PIECE = ITEMS.register("engine_piece", () -> new Item(msfItemProperties("engine_piece")));
    public static final DeferredItem<Item> PRESS_PIECE = ITEMS.register("press_piece", () -> new Item(msfItemProperties("press_piece")));

    public static final DeferredItem<Item> REBREWING_STAND = ITEMS.register("rebrewing_stand", () -> new BlockItem(ModBlocks.REBREWING_STAND_BOTTOM.get(), msfItemProperties("rebrewing_stand").useItemDescriptionPrefix()));
    public static final DeferredItem<Item> BROKEN_REBREWING_STAND = ITEMS.register("broken_rebrewing_stand", () -> new Item(msfItemProperties("broken_rebrewing_stand")));
    public static final DeferredItem<Item> EXTRACTION_BOTTLE = ITEMS.register("extraction_bottle", () -> new BottleOfExtractionItem(msfItemProperties("extraction_bottle").stacksTo(1)));
    public static final DeferredItem<Item> EXTRACTED_BOTTLE = ITEMS.register("extracted_bottle", () -> new PotionItem(msfItemProperties("extracted_bottle").stacksTo(1)) {
        @Override
        public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.extracted_bottle.obtain", "Obtainable using Bottle o' Extraction").withStyle(ChatFormatting.GOLD));
        }
    });
    public static final DeferredItem<Item> REBREWED_POTION = ITEMS.register("rebrewed_potion", () -> new PotionItem(msfItemProperties("rebrewed_potion").stacksTo(1)));
    public static final DeferredItem<Item> REBREWED_SPLASH_POTION = ITEMS.register("rebrewed_splash_potion", () -> new SplashPotionItem(msfItemProperties("rebrewed_splash_potion").stacksTo(1)));
    public static final DeferredItem<Item> REBREWED_LINGERING_POTION = ITEMS.register("rebrewed_lingering_potion", () -> new LingeringPotionItem(msfItemProperties("rebrewed_lingering_potion").stacksTo(1)));
    
    public static final DeferredItem<Item> CROPRESSED_POTATO = ITEMS.register("cropressed_potato", () -> new TrimMaterialItem(msfItemProperties("cropressed_potato")));
    public static final DeferredItem<Item> CROPRESSED_CARROT = ITEMS.register("cropressed_carrot", () -> new TrimMaterialItem(msfItemProperties("cropressed_carrot")));
    public static final DeferredItem<Item> CROPRESSED_BEETROOT = ITEMS.register("cropressed_beetroot", () -> new TrimMaterialItem(msfItemProperties("cropressed_beetroot")));
    public static final DeferredItem<Item> CROPRESSED_NETHERWART = ITEMS.register("cropressed_nether_wart", () -> new TrimMaterialItem(msfItemProperties("cropressed_nether_wart")));
    public static final DeferredItem<Item> CROPRESSED_WHEAT = ITEMS.register("cropressed_wheat", () -> new TrimMaterialItem(msfItemProperties("cropressed_wheat")));

    public static final DeferredItem<Item> TATER_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("tater_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("tater_armor_trim_smithing_template")));
    public static final DeferredItem<Item> CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carotene_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("carotene_armor_trim_smithing_template")));
    public static final DeferredItem<Item> BEAT_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("beat_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("beat_armor_trim_smithing_template")));
    public static final DeferredItem<Item> NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("nether_wart_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("nether_wart_armor_trim_smithing_template")));
    public static final DeferredItem<Item> GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("grain_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(msfItemProperties("grain_armor_trim_smithing_template")));

    public static final DeferredItem<Item> VIVICUS_ANTIDOTE = ITEMS.register("vivicus_antidote", () -> new VivicusAntidoteItem(msfItemProperties("vivicus_antidote")));
    public static final DeferredItem<Item> CORRUPTED_BOBLING_CORE = ITEMS.register("corrupted_bobling_core", () -> new Item(msfItemProperties("corrupted_bobling_core")));
    public static final DeferredItem<Item> BOBLING_CORE = ITEMS.register("bobling_core", () -> new Item(msfItemProperties("bobling_core")));
    public static final DeferredItem<Item> CORRUPTED_SLIME_BALL = ITEMS.register("corrupted_slime_ball", () -> new CorruptedSlimeBallItem(msfItemProperties("corrupted_slime_ball")));
    
    public static final DeferredItem<Item> CORRUPTED_SIGN = ITEMS.register("corrupted_sign", () -> new SignItem(ModBlocks.CORRUPTED_SIGN.get(), ModBlocks.CORRUPTED_WALL_SIGN.get(), msfItemProperties("corrupted_sign")));
    public static final DeferredItem<Item> CORRUPTED_HANGING_SIGN = ITEMS.register("corrupted_hanging_sign", () -> new HangingSignItem(ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), msfItemProperties("corrupted_hanging_sign")));
    public static final DeferredItem<Item> CORRUPTED_BOAT = ITEMS.register("corrupted_boat", () -> new ModBoatItem(false, ModBoatEntity.Type.CORRUPTED, msfItemProperties("corrupted_boat")));
    public static final DeferredItem<Item> CORRUPTED_CHEST_BOAT = ITEMS.register("corrupted_chest_boat", () -> new ModBoatItem(true, ModBoatEntity.Type.CORRUPTED, msfItemProperties("corrupted_chest_boat")));

    public static final DeferredItem<Item> VIVICUS_SIGN = ITEMS.register("vivicus_sign", () -> new SignItem(ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_WALL_SIGN.get(), msfItemProperties("vivicus_sign")));
    public static final DeferredItem<Item> VIVICUS_HANGING_SIGN = ITEMS.register("vivicus_hanging_sign", () -> new HangingSignItem(ModBlocks.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_WALL_HANGING_SIGN.get(), msfItemProperties("vivicus_hanging_sign")));
    public static final DeferredItem<Item> VIVICUS_BOAT = ITEMS.register("vivicus_boat", () -> new ModBoatItem(false, ModBoatEntity.Type.VIVICUS, msfItemProperties("vivicus_boat")));
    public static final DeferredItem<Item> VIVICUS_CHEST_BOAT = ITEMS.register("vivicus_chest_boat", () -> new ModBoatItem(true, ModBoatEntity.Type.VIVICUS, msfItemProperties("vivicus_chest_boat")));

    public static final DeferredItem<Item> BOBLING_SPAWN_EGG = ITEMS.register("bobling_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.BOBLING.get(), msfItemProperties("bobling_spawn_egg")));

    public static final DeferredItem<Item> CAULORFLOWER_SEEDS = ITEMS.register("caulorflower_seeds", () -> new BlockItem(ModBlocks.CAULORFLOWER.get(), msfItemProperties("caulorflower_seeds").useItemDescriptionPrefix()));
    
    public static final DeferredItem<Item> CREATIVE_TAB_ICON = ITEMS.register("creative_tab_icon", () -> new CreativeTabItem(msfItemProperties("creative_tab_icon")));
    public static final DeferredItem<Item> WAND_OF_CUBING = ITEMS.register("wand_of_cubing", () -> new WandOfCubingItem(msfItemProperties("wand_of_cubing")));

    protected static Item.Properties msfItemProperties(String name) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MoreSnifferFlowers.loc(name));
        return new Item.Properties().setId(key);
    }

}