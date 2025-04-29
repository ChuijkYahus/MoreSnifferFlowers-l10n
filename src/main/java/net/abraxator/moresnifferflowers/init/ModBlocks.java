package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.*;
import net.abraxator.moresnifferflowers.blocks.corrupted.*;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockBase;
import net.abraxator.moresnifferflowers.blocks.cropressor.CropressorBlockOut;
import net.abraxator.moresnifferflowers.blocks.giantcrops.GiantCropBlock;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockBase;
import net.abraxator.moresnifferflowers.blocks.rebrewingstand.RebrewingStandBlockTop;
import net.abraxator.moresnifferflowers.blocks.signs.ModHangingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModStandingSignBlock;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallHangingSign;
import net.abraxator.moresnifferflowers.blocks.signs.ModWallSignBlock;
import net.abraxator.moresnifferflowers.blocks.vivicus.*;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.AmbushBlockUpper;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockLower;
import net.abraxator.moresnifferflowers.blocks.xbush.GarbushBlockUpper;
import net.abraxator.moresnifferflowers.items.GiantCropItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreSnifferFlowers.MOD_ID);

    public static final DeferredBlock<Block> DAWNBERRY_VINE = registerBlockNoItem("dawnberry_vine", () -> new DawnberryVineBlock(msfBlockPropertiesOf("dawnberry_vine").mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion(), false));
    public static final DeferredBlock<Block> GLOOMBERRY_VINE = registerBlockNoItem("gloomberry_vine", () -> new GloomberryVineBlock(msfBlockPropertiesOf("gloomberry_vine").mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion()));

    public static final DeferredBlock<Block> AMBUSH_BOTTOM = registerBlockNoItem("ambush_bottom", () -> new AmbushBlockLower(msfBlockPropertiesOfFullCopy("ambush_bottom",Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> AMBUSH_TOP = registerBlockNoItem("ambush_top", () -> new AmbushBlockUpper(msfBlockPropertiesOfFullCopy("ambush_top",Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> GARBUSH_BOTTOM = registerBlockNoItem("garbush_bottom", () -> new GarbushBlockLower(msfBlockPropertiesOfFullCopy("garbush_bottom",Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> GARBUSH_TOP = registerBlockNoItem("garbush_top", () -> new GarbushBlockUpper(msfBlockPropertiesOfFullCopy("garbush_top",Blocks.WHEAT).strength(0.2F)));

    public static final DeferredBlock<Block> AMBER_BLOCK = registerBlockWithItem("amber_block", () -> new HalfTransparentBlock(msfBlockPropertiesOf("amber_block").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_AMBER = registerBlockWithItem("chiseled_amber", () -> new HalfTransparentBlock(msfBlockPropertiesOf("chiseled_amber").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_AMBER_SLAB = registerBlockWithItem("chiseled_amber_slab", () -> new SlabBlock(msfBlockPropertiesOfFullCopy("chiseled_amber_slab",ModBlocks.CHISELED_AMBER.get())));
    public static final DeferredBlock<Block> CRACKED_AMBER = registerBlockWithItem("cracked_amber", () -> new HalfTransparentBlock(msfBlockPropertiesOf("cracked_amber").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC = registerBlockWithItem("amber_mosaic", () -> new HalfTransparentBlock(msfBlockPropertiesOf("amber_mosaic").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC_SLAB = registerBlockWithItem("amber_mosaic_slab", () -> new SlabBlock(msfBlockPropertiesOfFullCopy("amber_mosaic_slab",ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> AMBER_MOSAIC_STAIRS = registerBlockWithItem("amber_mosaic_stairs", () -> stair("amber_mosaic_stairs" , AMBER_MOSAIC.get()));
    public static final DeferredBlock<Block> AMBER_MOSAIC_WALL = registerBlockWithItem("amber_mosaic_wall", () -> new WallBlock(msfBlockPropertiesOfFullCopy("amber_mosaic_stairs",ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> GARNET_BLOCK = registerBlockWithItem("garnet_block", () -> new HalfTransparentBlock(msfBlockPropertiesOf("garnet_block").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(5.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_GARNET = registerBlockWithItem("chiseled_garnet", () -> new HalfTransparentBlock(msfBlockPropertiesOf("chiseled_garnet").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_GARNET_SLAB = registerBlockWithItem("chiseled_garnet_slab", () -> new SlabBlock(msfBlockPropertiesOfFullCopy("chiseled_garnet_slab",ModBlocks.CHISELED_GARNET.get())));
    public static final DeferredBlock<Block> CRACKED_GARNET = registerBlockWithItem("cracked_garnet", () -> new HalfTransparentBlock(msfBlockPropertiesOf("cracked_garnet").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> GARNET_MOSAIC = registerBlockWithItem("garnet_mosaic", () -> new HalfTransparentBlock(msfBlockPropertiesOf("garnet_mosaic").mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> GARNET_MOSAIC_SLAB = registerBlockWithItem("garnet_mosaic_slab", () -> new SlabBlock(msfBlockPropertiesOfFullCopy("garnet_mosaic_slab",ModBlocks.GARNET_MOSAIC.get())));
    public static final DeferredBlock<Block> GARNET_MOSAIC_STAIRS = registerBlockWithItem("garnet_mosaic_stairs", () -> stair("garnet_mosaic_stairs" , GARNET_MOSAIC.get()));
    public static final DeferredBlock<Block> GARNET_MOSAIC_WALL = registerBlockWithItem("garnet_mosaic_wall", () -> new WallBlock(msfBlockPropertiesOfFullCopy("garnet_mosaic_wall",ModBlocks.GARNET_MOSAIC.get())));

    public static final DeferredBlock<Block> CAULORFLOWER = registerBlockNoItem("caulorflower", () ->  new CaulorflowerBlock(msfBlockPropertiesOf("garnet_mosaic_stairs").mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion().randomTicks()));

    public static final DeferredBlock<Block> GIANT_CARROT = registerGiantCrop("giant_carrot", () ->  new GiantCropBlock(msfBlockPropertiesOf("giant_carrot").mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(GiantCropBlock.statePredicate)));
    public static final DeferredBlock<Block> GIANT_POTATO = registerGiantCrop("giant_potato", () ->  new GiantCropBlock(msfBlockPropertiesOfFullCopy("giant_potato",ModBlocks.GIANT_CARROT.get())));
    public static final DeferredBlock<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart", () ->  new GiantCropBlock(msfBlockPropertiesOfFullCopy("giant_netherwart",ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate).dynamicShape()));
    public static final DeferredBlock<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot", () ->  new GiantCropBlock(msfBlockPropertiesOfFullCopy("giant_beetroot",ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate)));
    public static final DeferredBlock<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat", () ->  new GiantCropBlock(msfBlockPropertiesOfFullCopy("giant_wheat",ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate)));

    public static final DeferredBlock<Block> BONMEELIA = registerBlockNoItem("bonmeelia", () ->  new BonmeeliaBlock(msfBlockPropertiesOfFullCopy("bonmeelia",Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion(), false));
    public static final DeferredBlock<Block> BONWILTIA = registerBlockNoItem("bonwiltia", () ->  new BonmeeliaBlock(msfBlockPropertiesOfFullCopy("bonwiltia",Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion(), true));
    public static final DeferredBlock<Block> BONDRIPIA = registerBlockNoItem("bondripia", () ->  new BondripiaBlock(msfBlockPropertiesOfFullCopy("bondripia",Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Block> ACIDRIPIA = registerBlockNoItem("acidripia", () ->  new AciddripiaBlock(msfBlockPropertiesOfFullCopy("acidripia",Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Block> BONMEEL_FILLED_CAULDRON = registerBlockNoItem("bonmeel_filled_cauldron", () ->  new ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.BONMEEL, msfBlockPropertiesOfFullCopy("bonmeel_filled_cauldron",Blocks.CAULDRON)));
    public static final DeferredBlock<Block> ACID_FILLED_CAULDRON = registerBlockNoItem("acid_filled_cauldron", () ->  new ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.ACID, msfBlockPropertiesOfFullCopy("acid_filled_cauldron",Blocks.CAULDRON)));

    public static final DeferredBlock<Block> CROPRESSOR_CENTER = registerBlockNoItem("cropressor_center", () ->  new CropressorBlockBase(msfBlockPropertiesOfFullCopy("cropressor_center",Blocks.ANVIL), CropressorBlockBase.Part.CENTER));
    public static final DeferredBlock<Block> CROPRESSOR_OUT = registerBlockNoItem("cropressor_out", () ->  new CropressorBlockOut(msfBlockPropertiesOfFullCopy("cropressor_out",Blocks.ANVIL), CropressorBlockBase.Part.OUT));

    public static final DeferredBlock<Block> REBREWING_STAND_BOTTOM = registerBlockNoItem("rebrewing_stand_bottom", () -> new RebrewingStandBlockBase(msfBlockPropertiesOf("rebrewing_stand_bottom").requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    public static final DeferredBlock<Block> REBREWING_STAND_TOP = registerBlockNoItem("rebrewing_stand_top", () -> new RebrewingStandBlockTop(msfBlockPropertiesOf("rebrewing_stand_top").requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));

    public static final DeferredBlock<Block> DYESPRIA_PLANT = registerBlockNoItem("dyespria_plant", () ->  new DyespriaPlantBlock(msfBlockPropertiesOf("dyespria_plant").mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> DYESCRAPIA_PLANT = registerBlockNoItem("dyescrapia_plant", () ->  new DyescrapiaPlantBlock(msfBlockPropertiesOfFullCopy("dyescrapia_plant",DYESPRIA_PLANT.get())));

    public static final DeferredBlock<Block> CORRUPTED_LOG = registerBlockWithItem("corrupted_log", () -> new RotatedPillarBlock(msfBlockPropertiesOfFullCopy("corrupted_log",Blocks.WARPED_STEM)));
    public static final DeferredBlock<Block> CORRUPTED_WOOD = registerBlockWithItem("corrupted_wood", () -> new RotatedPillarBlock(msfBlockPropertiesOfFullCopy("corrupted_wood",Blocks.WARPED_HYPHAE)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_LOG = registerBlockWithItem("stripped_corrupted_log", () -> new RotatedPillarBlock(msfBlockPropertiesOfFullCopy("stripped_corrupted_log",Blocks.STRIPPED_WARPED_STEM)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_WOOD = registerBlockWithItem("stripped_corrupted_wood", () -> new RotatedPillarBlock(msfBlockPropertiesOfFullCopy("stripped_corrupted_wood",Blocks.STRIPPED_WARPED_HYPHAE)));
    public static final DeferredBlock<Block> CORRUPTED_PLANKS = registerBlockWithItem("corrupted_planks", () -> new Block(msfBlockPropertiesOfFullCopy("corrupted_planks",Blocks.WARPED_PLANKS)));
    public static final DeferredBlock<Block> CORRUPTED_STAIRS = registerBlockWithItem("corrupted_stairs", () -> stair("corrupted_stairs" ,CORRUPTED_PLANKS.get()));
    public static final DeferredBlock<Block> CORRUPTED_SLAB = registerBlockWithItem("corrupted_slab", () -> new SlabBlock(msfBlockPropertiesOfFullCopy("corrupted_slab",Blocks.WARPED_SLAB)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE = registerBlockWithItem("corrupted_fence", () -> new FenceBlock(msfBlockPropertiesOfFullCopy("corrupted_fence",Blocks.WARPED_FENCE)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE_GATE = registerBlockWithItem("corrupted_fence_gate", () -> new FenceGateBlock(ModWoodTypes.CORRUPTED, msfBlockPropertiesOfFullCopy("corrupted_fence_gate",Blocks.WARPED_FENCE_GATE)));
    public static final DeferredBlock<Block> CORRUPTED_DOOR = registerBlockWithItem("corrupted_door", () -> new DoorBlock(BlockSetType.WARPED, msfBlockPropertiesOfFullCopy("corrupted_door",Blocks.WARPED_DOOR)));
    public static final DeferredBlock<Block> CORRUPTED_TRAPDOOR = registerBlockWithItem("corrupted_trapdoor", () -> new TrapDoorBlock(BlockSetType.WARPED, msfBlockPropertiesOfFullCopy("corrupted_trapdoor",Blocks.WARPED_TRAPDOOR)));
    public static final DeferredBlock<Block> CORRUPTED_PRESSURE_PLATE = registerBlockWithItem("corrupted_pressure_plate", () -> new PressurePlateBlock(BlockSetType.WARPED, msfBlockPropertiesOfFullCopy("corrupted_pressure_plate",Blocks.WARPED_PRESSURE_PLATE)));
    public static final DeferredBlock<Block> CORRUPTED_BUTTON = registerBlockWithItem("corrupted_button", () -> new ButtonBlock(BlockSetType.WARPED, 30, msfBlockPropertiesOfFullCopy("corrupted_button",Blocks.WARPED_BUTTON)));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES = registerBlockWithItem("corrupted_leaves", () -> new CorruptedLeavesBlock(msfBlockPropertiesOfFullCopy("corrupted_leaves",Blocks.ACACIA_LEAVES).noOcclusion()));
    public static final DeferredBlock<Block> CORRUPTED_SAPLING = registerBlockWithItem("corrupted_sapling", () -> new SaplingBlock(ModTreeGrowers.CORRUPTED_TREE, msfBlockPropertiesOfFullCopy("corrupted_sapling",Blocks.ACACIA_SAPLING)));
    public static final DeferredBlock<Block> CORRUPTED_SLUDGE = registerBlockWithItem("corrupted_sludge", () -> new CorruptedSludgeBlock(msfBlockPropertiesOf("corrupted_sludge").mapColor(MapColor.COLOR_MAGENTA).strength(2.0F).friction(0.8F).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SLIME_LAYER = registerBlockWithItem("corrupted_slime_layer", () -> new CorruptedSlimeLayerBlock(msfBlockPropertiesOf("corrupted_slime_layer").mapColor(MapColor.COLOR_MAGENTA).strength(0.5F).friction(0.8F).noOcclusion().randomTicks().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SIGN = registerBlockNoItem("corrupted_sign", () -> new ModStandingSignBlock(ModWoodTypes.CORRUPTED, msfBlockPropertiesOfFullCopy("corrupted_sign",Blocks.WARPED_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_WALL_SIGN = registerBlockNoItem("corrupted_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.CORRUPTED, msfBlockPropertiesOfFullCopy("corrupted_wall_sign",Blocks.WARPED_WALL_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_HANGING_SIGN = registerBlockNoItem("corrupted_hanging_sign", () -> new ModHangingSignBlock(ModWoodTypes.CORRUPTED, msfBlockPropertiesOfFullCopy("corrupted_hanging_sign",Blocks.WARPED_HANGING_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_WALL_HANGING_SIGN = registerBlockNoItem("corrupted_wall_hanging_sign", () -> new ModWallHangingSign(ModWoodTypes.CORRUPTED, msfBlockPropertiesOfFullCopy("corrupted_wall_hanging_sign",Blocks.WARPED_WALL_HANGING_SIGN)));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES_BUSH = registerBlockWithItem("corrupted_leaves_bush", () -> new CorruptedLeavesBlock(msfBlockPropertiesOfFullCopy("corrupted_leaves_bush",ModBlocks.CORRUPTED_LEAVES.get()).noOcclusion()));

    public static final DeferredBlock<Block> DECAYED_LOG = registerBlockWithItem("decayed_log", () -> new RotatedPillarBlock(msfBlockPropertiesOfFullCopy("decayed_log",Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> CORRUPTED_GRASS_BLOCK = registerBlockWithItem("corrupted_grass_block", () -> new CorruptedGrassBlock(msfBlockPropertiesOf("corrupted_grass_block").mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS)));
    public static final DeferredBlock<Block> CURED_GRASS_BLOCK = registerBlockWithItem("cured_grass_block", () -> new CuredGrassBlock(msfBlockPropertiesOf("cured_grass_block").mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS)));

    public static final DeferredBlock<Block> VIVICUS_LOG = registerBlockWithItem("vivicus_log", () -> new VivicusRotatedPillarBlock(msfBlockPropertiesOfFullCopy("vivicus_log",Blocks.CHERRY_LOG)));
    public static final DeferredBlock<Block> VIVICUS_WOOD = registerBlockWithItem("vivicus_wood", () -> new VivicusRotatedPillarBlock(msfBlockPropertiesOfFullCopy("vivicus_wood",Blocks.CHERRY_WOOD)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_LOG = registerBlockWithItem("stripped_vivicus_log", () -> new VivicusRotatedPillarBlock(msfBlockPropertiesOfFullCopy("stripped_vivicus_log",Blocks.STRIPPED_CHERRY_LOG)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_WOOD = registerBlockWithItem("stripped_vivicus_wood", () -> new VivicusRotatedPillarBlock(msfBlockPropertiesOfFullCopy("stripped_vivicus_wood",Blocks.STRIPPED_CHERRY_WOOD)));
    public static final DeferredBlock<Block> VIVICUS_PLANKS = registerBlockWithItem("vivicus_planks", () -> new VivicusBlock(msfBlockPropertiesOfFullCopy("vivicus_planks",Blocks.CHERRY_PLANKS)));
    public static final DeferredBlock<Block> VIVICUS_STAIRS = registerBlockWithItem("vivicus_stairs", () -> vivicusStair("vivicus_stairs" ,VIVICUS_PLANKS.get()));
    public static final DeferredBlock<Block> VIVICUS_SLAB = registerBlockWithItem("vivicus_slab", () -> new VivicusSlabBlock(msfBlockPropertiesOfFullCopy("vivicus_slab",Blocks.CHERRY_SLAB)));
    public static final DeferredBlock<Block> VIVICUS_FENCE = registerBlockWithItem("vivicus_fence", () -> new VivicusFenceBlock(msfBlockPropertiesOfFullCopy("vivicus_fence",Blocks.CHERRY_FENCE)));
    public static final DeferredBlock<Block> VIVICUS_FENCE_GATE = registerBlockWithItem("vivicus_fence_gate", () -> new VivicusFenceGateBlock(ModWoodTypes.VIVICUS, msfBlockPropertiesOfFullCopy("vivicus_fence_gate",Blocks.CHERRY_FENCE_GATE)));
    public static final DeferredBlock<Block> VIVICUS_DOOR = registerBlockWithItem("vivicus_door", () -> new VivicusDoorBlock(BlockSetType.CHERRY, msfBlockPropertiesOfFullCopy("vivicus_door",Blocks.CHERRY_DOOR)));
    public static final DeferredBlock<Block> VIVICUS_TRAPDOOR = registerBlockWithItem("vivicus_trapdoor", () -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, msfBlockPropertiesOfFullCopy("vivicus_trapdoor",Blocks.CHERRY_TRAPDOOR)));
    public static final DeferredBlock<Block> VIVICUS_PRESSURE_PLATE = registerBlockWithItem("vivicus_pressure_plate", () -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, msfBlockPropertiesOfFullCopy("vivicus_pressure_plate",Blocks.CHERRY_PRESSURE_PLATE)));
    public static final DeferredBlock<Block> VIVICUS_BUTTON = registerBlockWithItem("vivicus_button", () -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, msfBlockPropertiesOfFullCopy("vivicus_button",Blocks.CHERRY_BUTTON)));
    public static final DeferredBlock<Block> VIVICUS_LEAVES = registerBlockWithItem("vivicus_leaves", () -> new VivicusLeavesBlock(msfBlockPropertiesOfFullCopy("vivicus_leaves",Blocks.CHERRY_LEAVES)));
    public static final DeferredBlock<Block> VIVICUS_SAPLING = registerBlockWithItem("vivicus_sapling", () -> new VivicusSaplingBlock(msfBlockPropertiesOfFullCopy("vivicus_sapling",Blocks.CHERRY_SAPLING)));
    public static final DeferredBlock<Block> VIVICUS_LEAVES_SPROUT = registerBlockWithItem("vivicus_leaves_sprout", () -> new VivicusSproutingBlock(msfBlockPropertiesOfFullCopy("vivicus_leaves_sprout",Blocks.MANGROVE_PROPAGULE)));
    public static final DeferredBlock<Block> VIVICUS_SIGN = registerBlockNoItem("vivicus_sign", () -> new VivicusStandingSignBlock(ModWoodTypes.VIVICUS, msfBlockPropertiesOfFullCopy("vivicus_sign",Blocks.CHERRY_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_WALL_SIGN = registerBlockNoItem("vivicus_wall_sign", () -> new VivicusWallSignBlock(ModWoodTypes.VIVICUS, msfBlockPropertiesOfFullCopy("vivicus_wall_sign",Blocks.CHERRY_WALL_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_HANGING_SIGN = registerBlockNoItem("vivicus_hanging_sign", () -> new VivicusHangingSignBlock(ModWoodTypes.VIVICUS, msfBlockPropertiesOfFullCopy("vivicus_hanging_sign",Blocks.CHERRY_HANGING_SIGN)));
    public static final DeferredBlock<Block> VIVICUS_WALL_HANGING_SIGN = registerBlockNoItem("vivicus_wall_hanging_sign", () -> new VivicusHangingWallSignBlock(ModWoodTypes.VIVICUS, msfBlockPropertiesOfFullCopy("bobling_sack",Blocks.CHERRY_WALL_HANGING_SIGN)));

    public static final DeferredBlock<Block> BOBLING_SACK = registerBlockNoItem("bobling_sack", () -> new BoblingSackBlock(msfBlockPropertiesOfFullCopy("bobling_sack",Blocks.CHERRY_LEAVES)));

    public static final DeferredBlock<Block> POTTED_DYESPRIA = registerBlockNoItem("potted_dyespria", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, msfBlockPropertiesOfFullCopy("potted_dyespria",Blocks.FLOWER_POT)));
    public static final DeferredBlock<Block> POTTED_CORRUPTED_SAPLING = registerBlockNoItem("potted_corrupted_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, msfBlockPropertiesOfFullCopy("potted_corrupted_sapling",Blocks.FLOWER_POT)));
    public static final DeferredBlock<Block> POTTED_VIVICUS_SAPLING = registerBlockNoItem("potted_vivicus_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, msfBlockPropertiesOfFullCopy("potted_vivicus_sapling",Blocks.FLOWER_POT)));

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, () -> {
            ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, MoreSnifferFlowers.loc(name));
            T block = blockSupplier.get();

            block.properties().setId(key);
            return block;
        });    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                ModItems.msfItemProperties(name)));
    }

    private static <T extends Block> DeferredBlock<T> registerGiantCrop(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerGiantCropItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerGiantCropItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new GiantCropItem(block.get(),
                ModItems.msfItemProperties(name)));
    }

    public static Block vivicusStair(String name ,Block pBaseBlock) {
        return new VivicusStairBlock(pBaseBlock.defaultBlockState(), msfBlockPropertiesOfFullCopy(name,pBaseBlock));
    }

    public static Block stair(String name, Block pBaseBlock) {
        return new StairBlock(pBaseBlock.defaultBlockState(), msfBlockPropertiesOfFullCopy(name,pBaseBlock));
    }

    protected static Block.Properties msfBlockPropertiesOf(String name) {
      return Block.Properties.of().setId(ResourceKey.create(Registries.BLOCK, MoreSnifferFlowers.loc(name)));
    }

    protected static Block.Properties msfBlockPropertiesOfFullCopy(String name, Block base) {
      return Block.Properties.ofFullCopy(base).setId(ResourceKey.create(Registries.BLOCK, MoreSnifferFlowers.loc(name)));
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Supplier<T> deferredBlock = () -> (T) factory.apply(properties);

        return BLOCKS.register(name, deferredBlock);
    }
}
