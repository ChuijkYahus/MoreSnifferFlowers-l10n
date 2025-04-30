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
import net.minecraft.resources.ResourceLocation;
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

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreSnifferFlowers.MOD_ID);

    public static final DeferredBlock<Block> DAWNBERRY_VINE = registerBlockNoItem("dawnberry_vine", registryName -> new DawnberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel(value -> value.getValue(DawnberryVineBlock.AGE) >= 3 ? 3 : 0).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion().setId(getBlockResourceKey(registryName)), false));
    public static final DeferredBlock<Block> GLOOMBERRY_VINE = registerBlockNoItem("gloomberry_vine", registryName -> new GloomberryVineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().noOcclusion().setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> AMBUSH_BOTTOM = BLOCKS.register("ambush_bottom", registryName -> new AmbushBlockLower(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> AMBUSH_TOP = BLOCKS.register("ambush_top", registryName -> new AmbushBlockUpper(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARBUSH_BOTTOM = BLOCKS.register("garbush_bottom", registryName -> new GarbushBlockLower(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARBUSH_TOP = BLOCKS.register("garbush_top", registryName -> new GarbushBlockUpper(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> AMBER_BLOCK = registerBlockWithItem("amber_block", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CHISELED_AMBER = registerBlockWithItem("chiseled_amber", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CHISELED_AMBER_SLAB = registerBlockWithItem("chiseled_amber_slab", (registryName) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHISELED_AMBER.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CRACKED_AMBER = registerBlockWithItem("cracked_amber", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> AMBER_MOSAIC = registerBlockWithItem("amber_mosaic", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> AMBER_MOSAIC_SLAB = registerBlockWithItem("amber_mosaic_slab", (registryName) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> AMBER_MOSAIC_STAIRS = registerBlockWithItem("amber_mosaic_stairs", (registryName) -> new StairBlock(AMBER_MOSAIC.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(AMBER_MOSAIC.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> AMBER_MOSAIC_WALL = registerBlockWithItem("amber_mosaic_wall", (registryName) -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.AMBER_MOSAIC.get())));
    public static final DeferredBlock<Block> GARNET_BLOCK = registerBlockWithItem("garnet_block", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(5.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CHISELED_GARNET = registerBlockWithItem("chiseled_garnet", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CHISELED_GARNET_SLAB = registerBlockWithItem("chiseled_garnet_slab", (registryName) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHISELED_GARNET.get())));
    public static final DeferredBlock<Block> CRACKED_GARNET = registerBlockWithItem("cracked_garnet", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARNET_MOSAIC = registerBlockWithItem("garnet_mosaic", (registryName) -> new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(SoundType.GLASS).strength(3.0F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARNET_MOSAIC_SLAB = registerBlockWithItem("garnet_mosaic_slab", (registryName) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GARNET_MOSAIC.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARNET_MOSAIC_STAIRS = registerBlockWithItem("garnet_mosaic_stairs", (registryName) -> new StairBlock(GARNET_MOSAIC.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GARNET_MOSAIC.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GARNET_MOSAIC_WALL = registerBlockWithItem("garnet_mosaic_wall", (registryName) -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GARNET_MOSAIC.get()).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> CAULORFLOWER = registerBlockNoItem("caulorflower", (registryName) ->  new CaulorflowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).sound(SoundType.GRASS).strength(2.0F).noCollission().noOcclusion().randomTicks().setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> GIANT_CARROT = registerGiantCrop("giant_carrot", (registryName) ->  new GiantCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(3.0F).sound(SoundType.MOSS_CARPET).noOcclusion().pushReaction(PushReaction.BLOCK).isSuffocating(GiantCropBlock.statePredicate).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GIANT_POTATO = registerGiantCrop("giant_potato", (registryName) ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GIANT_NETHERWART = registerGiantCrop("giant_netherwart", (registryName) ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate).dynamicShape().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GIANT_BEETROOT = registerGiantCrop("giant_beetroot", (registryName) ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> GIANT_WHEAT = registerGiantCrop("giant_wheat", (registryName) ->  new GiantCropBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.GIANT_CARROT.get()).noOcclusion().isSuffocating(GiantCropBlock.statePredicate).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> BONMEELIA = registerBlockNoItem("bonmeelia", (registryName) ->  new BonmeeliaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion().setId(getBlockResourceKey(registryName)), false));
    public static final DeferredBlock<Block> BONWILTIA = registerBlockNoItem("bonwiltia", (registryName) ->  new BonmeeliaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F).lightLevel(value -> 3).noOcclusion().setId(getBlockResourceKey(registryName)), true));
    public static final DeferredBlock<Block> BONDRIPIA = registerBlockNoItem("bondripia", (registryName) ->  new BondripiaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> ACIDRIPIA = registerBlockNoItem("acidripia", (registryName) ->  new AciddripiaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPORE_BLOSSOM).strength(0.2F).lightLevel(value -> 3).noOcclusion().randomTicks().pushReaction(PushReaction.BLOCK).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> BONMEEL_FILLED_CAULDRON = registerBlockNoItem("bonmeel_filled_cauldron", (registryName) ->  new ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.BONMEEL, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> ACID_FILLED_CAULDRON = registerBlockNoItem("acid_filled_cauldron", (registryName) ->  new ModLayeredCauldronBlock(Biome.Precipitation.NONE, ModCauldronInteractions.ACID, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> CROPRESSOR_CENTER = registerBlockNoItem("cropressor_center", (registryName) ->  new CropressorBlockBase(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).setId(getBlockResourceKey(registryName)), CropressorBlockBase.Part.CENTER));
    public static final DeferredBlock<Block> CROPRESSOR_OUT = registerBlockNoItem("cropressor_out", (registryName) ->  new CropressorBlockOut(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).setId(getBlockResourceKey(registryName)), CropressorBlockBase.Part.OUT));

    public static final DeferredBlock<Block> REBREWING_STAND_BOTTOM = registerBlockNoItem("rebrewing_stand_bottom", (registryName) -> new RebrewingStandBlockBase(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> REBREWING_STAND_TOP = registerBlockNoItem("rebrewing_stand_top", (registryName) -> new RebrewingStandBlockTop(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.5F).noOcclusion().setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> DYESPRIA_PLANT = registerBlockNoItem("dyespria_plant", (registryName) ->  new DyespriaPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> DYESCRAPIA_PLANT = registerBlockNoItem("dyescrapia_plant", (registryName) ->  new DyescrapiaPlantBlock(BlockBehaviour.Properties.ofFullCopy(DYESPRIA_PLANT.get()).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> CORRUPTED_LOG = registerBlockWithItem("corrupted_log", (registryName) -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_WOOD = registerBlockWithItem("corrupted_wood", (registryName) -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE)));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_LOG = registerBlockWithItem("stripped_corrupted_log", (registryName) -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> STRIPPED_CORRUPTED_WOOD = registerBlockWithItem("stripped_corrupted_wood", (registryName) -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_PLANKS = registerBlockWithItem("corrupted_planks", (registryName) -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_STAIRS = registerBlockWithItem("corrupted_stairs", (registryName) -> new StairBlock(CORRUPTED_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORRUPTED_PLANKS.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_SLAB = registerBlockWithItem("corrupted_slab", (registryName) -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SLAB).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_FENCE = registerBlockWithItem("corrupted_fence", (registryName) -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE)));
    public static final DeferredBlock<Block> CORRUPTED_FENCE_GATE = registerBlockWithItem("corrupted_fence_gate", (registryName) -> new FenceGateBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_FENCE_GATE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_DOOR = registerBlockWithItem("corrupted_door", (registryName) -> new DoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_DOOR).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_TRAPDOOR = registerBlockWithItem("corrupted_trapdoor", (registryName) -> new TrapDoorBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_TRAPDOOR).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_PRESSURE_PLATE = registerBlockWithItem("corrupted_pressure_plate", (registryName) -> new PressurePlateBlock(BlockSetType.WARPED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PRESSURE_PLATE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_BUTTON = registerBlockWithItem("corrupted_button", (registryName) -> new ButtonBlock(BlockSetType.WARPED, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_BUTTON).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES = registerBlockWithItem("corrupted_leaves", (registryName) -> new CorruptedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES).noOcclusion().setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_SAPLING = registerBlockWithItem("corrupted_sapling", (registryName) -> new SaplingBlock(ModTreeGrowers.CORRUPTED_TREE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING)));
    public static final DeferredBlock<Block> CORRUPTED_SLUDGE = registerBlockWithItem("corrupted_sludge", (registryName) -> new CorruptedSludgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(2.0F).friction(0.8F).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_SLIME_LAYER = registerBlockWithItem("corrupted_slime_layer", (registryName) -> new CorruptedSlimeLayerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).strength(0.5F).friction(0.8F).noOcclusion().randomTicks().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).lightLevel(value -> 4).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_SIGN = registerBlockNoItem("corrupted_sign", (registryName) -> new ModStandingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_WALL_SIGN = registerBlockNoItem("corrupted_wall_sign", (registryName) -> new ModWallSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_HANGING_SIGN = registerBlockNoItem("corrupted_hanging_sign", (registryName) -> new ModHangingSignBlock(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HANGING_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_WALL_HANGING_SIGN = registerBlockNoItem("corrupted_wall_hanging_sign", (registryName) -> new ModWallHangingSign(ModWoodTypes.CORRUPTED, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WALL_HANGING_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_LEAVES_BUSH = registerBlockWithItem("corrupted_leaves_bush", (registryName) -> new CorruptedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CORRUPTED_LEAVES.get()).noOcclusion().setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> DECAYED_LOG = registerBlockWithItem("decayed_log", (registryName) -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CORRUPTED_GRASS_BLOCK = registerBlockWithItem("corrupted_grass_block", (registryName) -> new CorruptedGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> CURED_GRASS_BLOCK = registerBlockWithItem("cured_grass_block", (registryName) -> new CuredGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.WET_GRASS).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> VIVICUS_LOG = registerBlockWithItem("vivicus_log", (registryName) -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_WOOD = registerBlockWithItem("vivicus_wood", (registryName) -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_LOG = registerBlockWithItem("stripped_vivicus_log", (registryName) -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_LOG).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> STRIPPED_VIVICUS_WOOD = registerBlockWithItem("stripped_vivicus_wood", (registryName) -> new VivicusRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_PLANKS = registerBlockWithItem("vivicus_planks", (registryName) -> new VivicusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_STAIRS = registerBlockWithItem("vivicus_stairs", (registryName) -> new VivicusStairBlock(VIVICUS_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(VIVICUS_PLANKS.get()).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_SLAB = registerBlockWithItem("vivicus_slab", (registryName) -> new VivicusSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_FENCE = registerBlockWithItem("vivicus_fence", (registryName) -> new VivicusFenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_FENCE_GATE = registerBlockWithItem("vivicus_fence_gate", (registryName) -> new VivicusFenceGateBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE_GATE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_DOOR = registerBlockWithItem("vivicus_door", (registryName) -> new VivicusDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_TRAPDOOR = registerBlockWithItem("vivicus_trapdoor", (registryName) -> new VivicusTrapDoorBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_TRAPDOOR).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_PRESSURE_PLATE = registerBlockWithItem("vivicus_pressure_plate", (registryName) -> new VivicusPressurePlateBlock(BlockSetType.CHERRY, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PRESSURE_PLATE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_BUTTON = registerBlockWithItem("vivicus_button", (registryName) -> new VivicusButtonBlock(BlockSetType.CHERRY, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_LEAVES = registerBlockWithItem("vivicus_leaves", (registryName) -> new VivicusLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_SAPLING = registerBlockWithItem("vivicus_sapling", (registryName) -> new VivicusSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_LEAVES_SPROUT = registerBlockWithItem("vivicus_leaves_sprout", (registryName) -> new VivicusSproutingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PROPAGULE).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_SIGN = registerBlockNoItem("vivicus_sign", (registryName) -> new VivicusStandingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_WALL_SIGN = registerBlockNoItem("vivicus_wall_sign", (registryName) -> new VivicusWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_HANGING_SIGN = registerBlockNoItem("vivicus_hanging_sign", (registryName) -> new VivicusHangingSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_HANGING_SIGN).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> VIVICUS_WALL_HANGING_SIGN = registerBlockNoItem("vivicus_wall_hanging_sign", (registryName) -> new VivicusHangingWallSignBlock(ModWoodTypes.VIVICUS, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WALL_HANGING_SIGN).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> BOBLING_SACK = registerBlockNoItem("bobling_sack", (registryName) -> new BoblingSackBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES).setId(getBlockResourceKey(registryName))));

    public static final DeferredBlock<Block> POTTED_DYESPRIA = registerBlockNoItem("potted_dyespria", (registryName) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DYESPRIA_PLANT, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> POTTED_CORRUPTED_SAPLING = registerBlockNoItem("potted_corrupted_sapling", (registryName) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORRUPTED_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey(registryName))));
    public static final DeferredBlock<Block> POTTED_VIVICUS_SAPLING = registerBlockNoItem("potted_vivicus_sapling", (registryName) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VIVICUS_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(getBlockResourceKey(registryName))));

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Function<ResourceLocation, ? extends T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<ResourceLocation, ? extends T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, (registryName) -> new BlockItem(block.get(),
                new Item.Properties().setId(ModItems.getItemResourceKey(registryName))));
    }

    private static <T extends Block> DeferredBlock<T> registerGiantCrop(String name, Function<ResourceLocation, ? extends T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerGiantCropItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerGiantCropItem(String name, DeferredBlock<T> block) {
        return ModItems.ITEMS.register(name, (registryName) -> new GiantCropItem(block.get(),
                new Item.Properties().setId(ModItems.getItemResourceKey(registryName))));
    }

    public static Block stair(Block pBaseBlock) {
        return new StairBlock(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock));
    }

    protected static @NotNull ResourceKey<Block> getBlockResourceKey(ResourceLocation name) {
        return ResourceKey.create(Registries.BLOCK, name);
    }
}
