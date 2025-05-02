package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MoreSnifferFlowers.MOD_ID);
    public static final DeferredItem<Item> DAWNBERRY_VINE_SEEDS = ITEMS.register("dawnberry_vine_seeds", (registryName) ->new BlockItem(ModBlocks.DAWNBERRY_VINE.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> GLOOMBERRY_VINE_SEEDS = ITEMS.register("gloomberry_vine_seeds", (registryName) ->new BlockItem(ModBlocks.GLOOMBERRY_VINE.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> DAWNBERRY = ITEMS.register("dawnberry", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName)).food(ModFoods.DAWNBERRY)));
    public static final DeferredItem<Item> GLOOMBERRY = ITEMS.register("gloomberry", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName)).food(ModFoods.GLOOMBERRY)));

    public static final DeferredItem<Item> AMBUSH_SEEDS = ITEMS.register("ambush_seeds", (registryName) ->new BlockItem(ModBlocks.AMBUSH_BOTTOM.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> GARBUSH_SEEDS = ITEMS.register("garbush_seeds", (registryName) ->new BlockItem(ModBlocks.GARBUSH_BOTTOM.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));

    public static final DeferredItem<Item> AMBUSH_BANNER_PATTERN = ITEMS.register("ambush_banner_pattern", (registryName) ->new BannerPatternItem(ModTags.ModBannerPatternTags.AMBUSH_BANNER_PATTERN, new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    public static final DeferredItem<Item> EVIL_BANNER_PATTERN = ITEMS.register("evil_banner_pattern", (registryName) ->new BannerPatternItem(ModTags.ModBannerPatternTags.EVIL_BANNER_PATTERN, new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));

    public static final DeferredItem<Item> AMBER_SHARD = ITEMS.register("amber_shard", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> GARNET_SHARD = ITEMS.register("garnet_shard", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> AROMA_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("aroma_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CARNAGE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carnage_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> DRAGONFLY = ITEMS.register("dragonfly", (registryName) ->new DragonflyItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> DYESPRIA = ITEMS.register("dyespria", (registryName) ->new DyespriaItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    public static final DeferredItem<Item> DYESCRAPIA = ITEMS.register("dyescrapia", (registryName) ->new DyescrapiaItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    public static final DeferredItem<Item> DYESPRIA_SEEDS = ITEMS.register("dyespria_seeds", (registryName) ->new BlockItem(ModBlocks.DYESPRIA_PLANT.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()) {
        @Override
        public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.dyespria_seeds", "Shear to hide dye").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> BONMEELIA_SEEDS = ITEMS.register("bonmeelia_seeds", (registryName) ->new BlockItem(ModBlocks.BONMEELIA.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> JAR_OF_BONMEEL = ITEMS.register("jar_of_bonmeel", (registryName) ->new JarOfBonmeelItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> BONDRIPIA_SEEDS = ITEMS.register("bondripia_seeds", (registryName) ->new BlockItem(ModBlocks.BONDRIPIA.get(), new Item.Properties().setId(getItemResourceKey(registryName))) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.bondripia_seeds", "Plantable underneath an area of 5 blocks in a + shape").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> BONWILTIA_SEEDS = ITEMS.register("bonwiltia_seeds", (registryName) ->new BlockItem(ModBlocks.BONWILTIA.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> JAR_OF_ACID = ITEMS.register("jar_of_acid", (registryName) ->new JarOfAcidItem(new Item.Properties().setId(getItemResourceKey(registryName))) {
        @Override
        public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.acid_jar", "Ungrows organic blocks").withStyle(ChatFormatting.GOLD));
        }
    });

    public static final DeferredItem<Item> ACIDRIPIA_SEEDS = ITEMS.register("acidripia_seeds", (registryName) ->new BlockItem(ModBlocks.ACIDRIPIA.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));

    public static final DeferredItem<Item> CROPRESSOR = ITEMS.register("cropressor", (registryName) ->new CropressorItem(ModBlocks.CROPRESSOR_OUT.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> TUBE_PIECE = ITEMS.register("tube_piece", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> BELT_PIECE = ITEMS.register("belt_piece", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> SCRAP_PIECE = ITEMS.register("scrap_piece", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> ENGINE_PIECE = ITEMS.register("engine_piece", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> PRESS_PIECE = ITEMS.register("press_piece", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> REBREWING_STAND = ITEMS.register("rebrewing_stand", (registryName) ->new BlockItem(ModBlocks.REBREWING_STAND_BOTTOM.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    public static final DeferredItem<Item> BROKEN_REBREWING_STAND = ITEMS.register("broken_rebrewing_stand", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> EXTRACTION_BOTTLE = ITEMS.register("extraction_bottle", (registryName) ->new BottleOfExtractionItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    public static final DeferredItem<Item> EXTRACTED_BOTTLE = ITEMS.register("extracted_bottle", (registryName) ->new PotionItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)) {
        @Override
        public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.extracted_bottle.obtain", "Obtainable using Bottle o' Extraction").withStyle(ChatFormatting.GOLD));
        }
    });
    public static final DeferredItem<Item> REBREWED_POTION = ITEMS.register("rebrewed_potion", (registryName) ->new PotionItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)));
    public static final DeferredItem<Item> REBREWED_SPLASH_POTION = ITEMS.register("rebrewed_splash_potion", (registryName) ->new SplashPotionItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    public static final DeferredItem<Item> REBREWED_LINGERING_POTION = ITEMS.register("rebrewed_lingering_potion", (registryName) ->new LingeringPotionItem(new Item.Properties().setId(getItemResourceKey(registryName)).stacksTo(1)));
    
    public static final DeferredItem<Item> CROPRESSED_POTATO = ITEMS.register("cropressed_potato", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CROPRESSED_CARROT = ITEMS.register("cropressed_carrot", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CROPRESSED_BEETROOT = ITEMS.register("cropressed_beetroot", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CROPRESSED_NETHERWART = ITEMS.register("cropressed_nether_wart", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CROPRESSED_WHEAT = ITEMS.register("cropressed_wheat", (registryName) ->new TrimMaterialItem(new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> TATER_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("tater_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CAROTENE_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("carotene_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> BEAT_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("beat_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> NETHER_WART_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("nether_wart_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> GRAIN_ARMOR_TRIM_SMITHING_TEMPLATE = ITEMS.register("grain_armor_trim_smithing_template", (registryName) ->SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.RARE).setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> VIVICUS_ANTIDOTE = ITEMS.register("vivicus_antidote", (registryName) ->new VivicusAntidoteItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CORRUPTED_BOBLING_CORE = ITEMS.register("corrupted_bobling_core", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> BOBLING_CORE = ITEMS.register("bobling_core", (registryName) ->new Item(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CORRUPTED_SLIME_BALL = ITEMS.register("corrupted_slime_ball", (registryName) ->new CorruptedSlimeBallItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    
    public static final DeferredItem<Item> CORRUPTED_SIGN = ITEMS.register("corrupted_sign", (registryName) ->new SignItem(ModBlocks.CORRUPTED_SIGN.get(), ModBlocks.CORRUPTED_WALL_SIGN.get(), new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CORRUPTED_HANGING_SIGN = ITEMS.register("corrupted_hanging_sign", (registryName) ->new HangingSignItem(ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get(), new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CORRUPTED_BOAT = ITEMS.register("corrupted_boat", (registryName) ->new ModBoatItem(false, ModBoatEntity.Type.CORRUPTED, new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> CORRUPTED_CHEST_BOAT = ITEMS.register("corrupted_chest_boat", (registryName) ->new ModBoatItem(true, ModBoatEntity.Type.CORRUPTED, new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> VIVICUS_SIGN = ITEMS.register("vivicus_sign", (registryName) ->new SignItem(ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_WALL_SIGN.get(), new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> VIVICUS_HANGING_SIGN = ITEMS.register("vivicus_hanging_sign", (registryName) ->new HangingSignItem(ModBlocks.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_WALL_HANGING_SIGN.get(), new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> VIVICUS_BOAT = ITEMS.register("vivicus_boat", (registryName) ->new ModBoatItem(false, ModBoatEntity.Type.VIVICUS, new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> VIVICUS_CHEST_BOAT = ITEMS.register("vivicus_chest_boat", (registryName) ->new ModBoatItem(true, ModBoatEntity.Type.VIVICUS, new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> BOBLING_SPAWN_EGG = ITEMS.register("bobling_spawn_egg", (registryName) ->new SpawnEggItem(ModEntityTypes.BOBLING.get(), new Item.Properties().setId(getItemResourceKey(registryName))));

    public static final DeferredItem<Item> CAULORFLOWER_SEEDS = ITEMS.register("caulorflower_seeds", (registryName) ->new BlockItem(ModBlocks.CAULORFLOWER.get(), new Item.Properties().setId(getItemResourceKey(registryName)).useItemDescriptionPrefix()));
    
    public static final DeferredItem<Item> CREATIVE_TAB_ICON = ITEMS.register("creative_tab_icon", (registryName) ->new CreativeTabItem(new Item.Properties().setId(getItemResourceKey(registryName))));
    public static final DeferredItem<Item> WAND_OF_CUBING = ITEMS.register("wand_of_cubing", (registryName) ->new WandOfCubingItem(new Item.Properties().setId(getItemResourceKey(registryName))));


    protected static @NotNull ResourceKey<Item> getItemResourceKey(ResourceLocation name) {
        return ResourceKey.create(Registries.ITEM, name);
    }
}