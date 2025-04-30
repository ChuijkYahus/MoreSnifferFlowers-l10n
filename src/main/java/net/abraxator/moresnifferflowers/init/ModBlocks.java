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
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreSnifferFlowers.MOD_ID);

    public static final DeferredBlock<Block> GLOOMBERRY_VINE = BLOCKS.register("gloomberry_vine", registryName  ->  new GloomberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion().setId(getBlockResourceKey("chiseled_amber"))));

    public static final DeferredBlock<Block> DAWNBERRY_VINE = SPECIALregisterNOitem("dawnberry_vine", () -> new DawnberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion().setId(getBlockResourceKey("dawnberry_vine")), false));
   // public static final DeferredBlock<Block> GLOOMBERRY_VINE = registerBlockNoItem("gloomberry_vine",  GloomberryVineBlock::new, (BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion()));
    
    public static final DeferredBlock<Block> AMBUSH_BOTTOM = registerBlockNoItem("ambush_bottom",  AmbushBlockLower::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> AMBUSH_TOP = registerBlockNoItem("ambush_top",  AmbushBlockUpper::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> GARBUSH_BOTTOM = registerBlockNoItem("garbush_bottom",  GarbushBlockLower::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
    public static final DeferredBlock<Block> GARBUSH_TOP = registerBlockNoItem("garbush_top",  GarbushBlockUpper::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
    
    public static final DeferredBlock<Block> AMBER_BLOCK = registerBlockWithItem("amber_block",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_AMBER = registerBlockWithItem("chiseled_amber",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey("chiseled_amber"))));
    public static final DeferredBlock<Block> CHISELED_AMBER_SLAB = registerBlockWithItem("chiseled_amber_slab",  SlabBlock::new, (BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHISELED_AMBER.get()).setId(getBlockResourceKey("chiseled_amber_slab"))));
    public static final DeferredBlock<Block> CRACKED_AMBER = registerBlockWithItem("cracked_amber",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC = registerBlockWithItem("amber_mosaic",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> AMBER_MOSAIC_SLAB = registerBlockWithItem("amber_mosaic_slab",  SlabBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> AMBER_MOSAIC_STAIRS = SPECIALregisterNOitem("amber_mosaic_stairs", () -> new StairBlock(AMBER_MOSAIC.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(AMBER_MOSAIC.get()).setId(getBlockResourceKey("amber_mosaic_stairs"))));
    public static final DeferredBlock<Block> AMBER_MOSAIC_WALL = registerBlockWithItem("amber_mosaic_wall",  WallBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> GARNET_BLOCK = registerBlockWithItem("garnet_block",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(5.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_GARNET = registerBlockWithItem("chiseled_garnet",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> CHISELED_GARNET_SLAB = registerBlockWithItem("chiseled_garnet_slab",  SlabBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHISELED_GARNET.get())));
    public static final DeferredBlock<Block> CRACKED_GARNET = registerBlockWithItem("cracked_garnet",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> GARNET_MOSAIC = registerBlockWithItem("garnet_mosaic",  HalfTransparentBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion()));
    public static final DeferredBlock<Block> GARNET_MOSAIC_SLAB = registerBlockWithItem("garnet_mosaic_slab",  SlabBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GARNET_MOSAIC.get())));
    public static final DeferredBlock<Block> GARNET_MOSAIC_STAIRS = SPECIALregisterNOitem("garnet_mosaic_stairs", () -> new StairBlock(GARNET_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(AMBER_MOSAIC.get()).setId(getBlockResourceKey("garnet_mosaic_stairs"))));
    public static final DeferredBlock<Block> GARNET_MOSAIC_WALL = registerBlockWithItem("garnet_mosaic_wall",  WallBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GARNET_MOSAIC.get())));

    public static final DeferredBlock<Block> CAULORFLOWER = registerBlockNoItem("caulorflower",   CaulorflowerBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion().randomTicks()));
    
    public static final DeferredBlock<Block> GIANT_CARROT = registerGiantCrop("giant_carrot",   GiantCropBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(GiantCropBlock.statePredicate)));
    public static final DeferredBlock<Block> GIANT_POTATO = registerGiantCrop("giant_potato",   GiantCropBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get())));
    public static final DeferredBlock<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart",   GiantCropBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate).dynamicShape()));
    public static final DeferredBlock<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot",   GiantCropBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate)));
    public static final DeferredBlock<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat",   GiantCropBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate)));
    
    public static final DeferredBlock<Block> BONMEELIA = SPECIALregisterNOitem("bonmeelia",  () -> new BonmeeliaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion().setId(getBlockResourceKey("bonmeelia")), false));
    public static final DeferredBlock<Block> BONWILTIA = SPECIALregisterNOitem("bonwiltia",  () -> new BonmeeliaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion().setId(getBlockResourceKey("bonwiltia")), true));
    public static final DeferredBlock<Block> BONDRIPIA = registerBlockNoItem("bondripia",   BondripiaBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Block> ACIDRIPIA = registerBlockNoItem("acidripia",   AciddripiaBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK)));
    public static final DeferredBlock<Block> BONMEEL_FILLED_CAULDRON = SPECIALregisterNOitem("bonmeel_filled_cauldron",  () -> new  ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.BONMEEL, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).setId(getBlockResourceKey("bonmeel_filled_cauldron"))));
    public static final DeferredBlock<Block> ACID_FILLED_CAULDRON = SPECIALregisterNOitem("acid_filled_cauldron",  () -> new  ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.ACID, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).setId(getBlockResourceKey("acid_filled_cauldron"))));
    
    public static final DeferredBlock<Block> CROPRESSOR_CENTER = SPECIALregisterNOitem("cropressor_center",   () -> new CropressorBlockBase(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).setId(getBlockResourceKey("cropressor_center")), CropressorBlockBase.Part.CENTER));
    public static final DeferredBlock<Block> CROPRESSOR_OUT = SPECIALregisterNOitem("cropressor_out",   () -> new CropressorBlockOut(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).setId(getBlockResourceKey("cropressor_out")), CropressorBlockBase.Part.OUT));

    public static final DeferredBlock<Block> REBREWING_STAND_BOTTOM = registerBlockNoItem("rebrewing_stand_bottom",  RebrewingStandBlockBase::new,(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    public static final DeferredBlock<Block> REBREWING_STAND_TOP = registerBlockNoItem("rebrewing_stand_top",  RebrewingStandBlockTop::new,(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion()));
    
    public static final DeferredBlock<Block> DYESPRIA_PLANT = registerBlockNoItem("dyespria_plant",   DyespriaPlantBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> DYESCRAPIA_PLANT = registerBlockNoItem("dyescrapia_plant",   DyescrapiaPlantBlock::new,(BlockBehaviour.Properties.ofFullCopy(DYESPRIA_PLANT.get())));

    public static final DeferredBlock<Block> CORRUPTED_LOG = registerBlockWithItem("corrupted_log",  RotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM)));
    public static final DeferredBlock<Block> CORRUPTED_WOOD = registerBlockWithItem("corrupted_wood",  RotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_LOG = registerBlockWithItem("stripped_corrupted_log",  RotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_WOOD = registerBlockWithItem("stripped_corrupted_wood",  RotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE)));
    public static final DeferredBlock<Block> CORRUPTED_PLANKS = registerBlockWithItem("corrupted_planks",  Block::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS)));
    public static final DeferredBlock<Block> CORRUPTED_STAIRS = SPECIALregisterYESItem("corrupted_stairs", () -> new StairBlock(CORRUPTED_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORRUPTED_PLANKS.get()).setId(getBlockResourceKey("corrupted_stairs"))));
    public static final DeferredBlock<Block> CORRUPTED_SLAB = registerBlockWithItem("corrupted_slab", SlabBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SLAB)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE = registerBlockWithItem("corrupted_fence", FenceBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE_GATE = SPECIALregisterYESItem("corrupted_fence_gate", () -> new FenceGateBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE_GATE).setId(getBlockResourceKey("corrupted_fence_gate"))));
    public static final DeferredBlock<Block> CORRUPTED_DOOR = SPECIALregisterYESItem("corrupted_door", () -> new DoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_DOOR).setId(getBlockResourceKey("corrupted_door"))));
    public static final DeferredBlock<Block> CORRUPTED_TRAPDOOR = SPECIALregisterYESItem("corrupted_trapdoor", () -> new TrapDoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_TRAPDOOR).setId(getBlockResourceKey("corrupted_trapdoor"))));
    public static final DeferredBlock<Block> CORRUPTED_PRESSURE_PLATE = SPECIALregisterYESItem("corrupted_pressure_plate", () -> new PressurePlateBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PRESSURE_PLATE).setId(getBlockResourceKey("corrupted_pressure_plate"))));
    public static final DeferredBlock<Block> CORRUPTED_BUTTON = SPECIALregisterYESItem("corrupted_button", () -> new ButtonBlock(BlockSetType.WARPED, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_BUTTON).setId(getBlockResourceKey("corrupted_button"))));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES = SPECIALregisterYESItem("corrupted_leaves", () -> new CorruptedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES).noOcclusion().setId(getBlockResourceKey("corrupted_leaves"))));
    public static final DeferredBlock<Block> CORRUPTED_SAPLING = SPECIALregisterYESItem("corrupted_sapling", () -> new SaplingBlock(ModTreeGrowers.CORRUPTED_TREE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING).setId(getBlockResourceKey("corrupted_sapling"))));
    public static final DeferredBlock<Block> CORRUPTED_SLUDGE = registerBlockWithItem("corrupted_sludge",  CorruptedSludgeBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(2.0F).friction(0.8F).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SLIME_LAYER = registerBlockWithItem("corrupted_slime_layer",  CorruptedSlimeLayerBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(0.5F).friction(0.8F).noOcclusion().randomTicks().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4)));
    public static final DeferredBlock<Block> CORRUPTED_SIGN = SPECIALregisterNOitem("corrupted_sign", () -> new ModStandingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SIGN).setId(getBlockResourceKey("corrupted_sign"))));
    public static final DeferredBlock<Block> CORRUPTED_WALL_SIGN = SPECIALregisterNOitem("corrupted_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_SIGN).setId(getBlockResourceKey("corrupted_wall_sign"))));
    public static final DeferredBlock<Block> CORRUPTED_HANGING_SIGN = SPECIALregisterNOitem("corrupted_hanging_sign", () -> new ModHangingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HANGING_SIGN).setId(getBlockResourceKey("corrupted_hanging_sign"))));
    public static final DeferredBlock<Block> CORRUPTED_WALL_HANGING_SIGN = SPECIALregisterNOitem("corrupted_wall_hanging_sign", () -> new ModWallHangingSign(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_HANGING_SIGN).setId(getBlockResourceKey("corrupted_wall_hanging_sign"))));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES_BUSH = registerBlockWithItem("corrupted_leaves_bush", CorruptedLeavesBlock::new,(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CORRUPTED_LEAVES.get()).noOcclusion()));

    public static final DeferredBlock<Block> DECAYED_LOG = registerBlockWithItem("decayed_log",  RotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> CORRUPTED_GRASS_BLOCK = registerBlockWithItem("corrupted_grass_block",  CorruptedGrassBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS)));
    public static final DeferredBlock<Block> CURED_GRASS_BLOCK = registerBlockWithItem("cured_grass_block",  CuredGrassBlock::new,(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS)));

    public static final DeferredBlock<Block> VIVICUS_LOG = registerBlockWithItem("vivicus_log",  VivicusRotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG)));
    public static final DeferredBlock<Block> VIVICUS_WOOD = registerBlockWithItem("vivicus_wood",  VivicusRotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_LOG = registerBlockWithItem("stripped_vivicus_log",  VivicusRotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_LOG)));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_WOOD = registerBlockWithItem("stripped_vivicus_wood",  VivicusRotatedPillarBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD)));
    public static final DeferredBlock<Block> VIVICUS_PLANKS = registerBlockWithItem("vivicus_planks",  VivicusBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));
    public static final DeferredBlock<Block> VIVICUS_STAIRS = SPECIALregisterYESItem("vivicus_stairs", () -> new VivicusStairBlock(VIVICUS_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(VIVICUS_PLANKS.get()).setId(getBlockResourceKey("vivicus_stairs"))));
    public static final DeferredBlock<Block> VIVICUS_SLAB = registerBlockWithItem("vivicus_slab",  VivicusSlabBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB)));
    public static final DeferredBlock<Block> VIVICUS_FENCE = registerBlockWithItem("vivicus_fence",  VivicusFenceBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE)));
    public static final DeferredBlock<Block> VIVICUS_FENCE_GATE = SPECIALregisterYESItem("vivicus_fence_gate", () -> new VivicusFenceGateBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE_GATE).setId(getBlockResourceKey("vivicus_fence_gate"))));
    public static final DeferredBlock<Block> VIVICUS_DOOR = SPECIALregisterYESItem("vivicus_door", () -> new VivicusDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR).setId(getBlockResourceKey("vivicus_door"))));
    public static final DeferredBlock<Block> VIVICUS_TRAPDOOR = SPECIALregisterYESItem("vivicus_trapdoor", () -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_TRAPDOOR).setId(getBlockResourceKey("vivicus_trapdoor"))));
    public static final DeferredBlock<Block> VIVICUS_PRESSURE_PLATE = SPECIALregisterYESItem("vivicus_pressure_plate", () -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PRESSURE_PLATE).setId(getBlockResourceKey("vivicus_pressure_plate"))));
    public static final DeferredBlock<Block> VIVICUS_BUTTON = SPECIALregisterYESItem("vivicus_button", () -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON).setId(getBlockResourceKey("vivicus_button"))));
    public static final DeferredBlock<Block> VIVICUS_LEAVES = registerBlockWithItem("vivicus_leaves",  VivicusLeavesBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
    public static final DeferredBlock<Block> VIVICUS_SAPLING = registerBlockWithItem("vivicus_sapling",  VivicusSaplingBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING)));
    public static final DeferredBlock<Block> VIVICUS_LEAVES_SPROUT = registerBlockWithItem("vivicus_leaves_sprout",  VivicusSproutingBlock::new,(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PROPAGULE)));
    public static final DeferredBlock<Block> VIVICUS_SIGN = SPECIALregisterNOitem("vivicus_sign", () -> new VivicusStandingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SIGN).setId(getBlockResourceKey("vivicus_sign"))));
    public static final DeferredBlock<Block> VIVICUS_WALL_SIGN = SPECIALregisterNOitem("vivicus_wall_sign", () -> new VivicusWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_SIGN).setId(getBlockResourceKey("vivicus_wall_sign"))));
    public static final DeferredBlock<Block> VIVICUS_HANGING_SIGN = SPECIALregisterNOitem("vivicus_hanging_sign", () -> new VivicusHangingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_HANGING_SIGN).setId(getBlockResourceKey("vivicus_hanging_sign"))));
    public static final DeferredBlock<Block> VIVICUS_WALL_HANGING_SIGN = SPECIALregisterNOitem("vivicus_wall_hanging_sign", () -> new VivicusHangingWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_HANGING_SIGN).setId(getBlockResourceKey("vivicus_wall_hanging_sign"))));

    public static final DeferredBlock<Block> BOBLING_SACK = registerBlockNoItem("bobling_sack", BoblingSackBlock::new, (BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES)));
        
    public static final DeferredBlock<Block> POTTED_DYESPRIA = SPECIALregisterNOitem("potted_dyespria", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey("potted_dyespria"))));
    public static final DeferredBlock<Block> POTTED_CORRUPTED_SAPLING = SPECIALregisterNOitem("potted_corrupted_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey("potted_corrupted_sapling"))));
    public static final DeferredBlock<Block> POTTED_VIVICUS_SAPLING = SPECIALregisterNOitem("potted_vivicus_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey("potted_vivicus_sapling"))));


    private static <T extends Block> DeferredBlock<T> SPECIALregisterYESItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> SPECIALregisterNOitem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static  <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props) {
        return BLOCKS.registerBlock(name, func, props);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, func, props);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().setId(ModItems.getItemResourceKey(name))));
    }

    private static <T extends Block> DeferredBlock<T> registerGiantCrop(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties props) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, func, props);
        registerGiantCropItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerGiantCropItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, () -> new GiantCropItem(block.get(),
                new Item.Properties().setId(ModItems.getItemResourceKey(name))));
    }


    public static Block vivicusStair(Block pBaseBlock) {
        return new VivicusStairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock));
    }
    
    public static Block stair(Block pBaseBlock) {
        return new StairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock));
    }

    protected static @NotNull ResourceKey<Block> getBlockResourceKey(String name) {
        return ResourceKey.create(Registries.BLOCK, MoreSnifferFlowers.loc(name));
    }
}
