package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<XbushBlockEntity>> XBUSH = BLOCK_ENTITIES.register("xbush", () -> new BlockEntityType<>(XbushBlockEntity::new, ModBlocks.AMBUSH_TOP.get(), ModBlocks.GARBUSH_TOP.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GiantCropBlockEntity>> GIANT_CROP = BLOCK_ENTITIES.register("giant_crop", () -> new BlockEntityType<>(GiantCropBlockEntity::new, ModBlocks.GIANT_CARROT.get(), ModBlocks.GIANT_POTATO.get(), ModBlocks.GIANT_NETHERWART.get(), ModBlocks.GIANT_BEETROOT.get(), ModBlocks.GIANT_WHEAT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CropressorBlockEntity>> CROPRESSOR = BLOCK_ENTITIES.register("cropressor", () -> new BlockEntityType<>(CropressorBlockEntity::new, ModBlocks.CROPRESSOR_OUT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RebrewingStandBlockEntity>> REBREWING_STAND = BLOCK_ENTITIES.register("rebrewing_stand", () -> new BlockEntityType<>(RebrewingStandBlockEntity::new, ModBlocks.REBREWING_STAND_TOP.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DyespriaPlantBlockEntity>> DYESPRIA_PLANT = BLOCK_ENTITIES.register("dyespria_plant", () -> new BlockEntityType<>(DyespriaPlantBlockEntity::new, ModBlocks.DYESPRIA_PLANT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoblingSackBlockEntity>> BOBLING_SACK = BLOCK_ENTITIES.register("bobling_sack", () -> new BlockEntityType<>(BoblingSackBlockEntity::new, ModBlocks.BOBLING_SACK.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CorruptedSludgeBlockEntity>> CORRUPTED_SLUDGE = BLOCK_ENTITIES.register("corrupted_sludge", () -> new BlockEntityType<>(CorruptedSludgeBlockEntity::new, ModBlocks.CORRUPTED_SLUDGE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BondripiaBlockEntity>> BONDRIPIA = BLOCK_ENTITIES.register("bondripia", () -> new BlockEntityType<>(BondripiaBlockEntity::new, ModBlocks.BONDRIPIA.get(), ModBlocks.ACIDRIPIA.get()));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> MOD_SIGN = BLOCK_ENTITIES.register("mod_sign", () -> new BlockEntityType<>(ModSignBlockEntity::new, ModBlocks.CORRUPTED_SIGN.get(), ModBlocks.CORRUPTED_WALL_SIGN.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VivicusSignBlockEntity>> VIVICUS_SIGN = BLOCK_ENTITIES.register("vivicus_sign", () -> new BlockEntityType<>(VivicusSignBlockEntity::new, ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_WALL_SIGN.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN = BLOCK_ENTITIES.register("mod_hanging_sign", () -> new BlockEntityType<>(ModHangingSignBlockEntity::new, ModBlocks.CORRUPTED_HANGING_SIGN.get(), ModBlocks.CORRUPTED_WALL_HANGING_SIGN.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VivicusHangingSignBlockEntity>> VIVICUS_HANGING_SIGN = BLOCK_ENTITIES.register("vivicus_hanging_sign", () -> new BlockEntityType<>(VivicusHangingSignBlockEntity::new, ModBlocks.VIVICUS_HANGING_SIGN.get(), ModBlocks.VIVICUS_WALL_HANGING_SIGN.get()));
}
