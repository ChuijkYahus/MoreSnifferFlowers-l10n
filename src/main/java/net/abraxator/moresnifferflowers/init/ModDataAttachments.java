package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MoreSnifferFlowers.MOD_ID);

    public static final Supplier<AttachmentType<BlockPatternCapability>> BLOCK_PATTERNS = ATTACHMENT_TYPES.register("block_patterns", () -> AttachmentType.builder(() -> new BlockPatternCapability(new HashMap<>())).serialize(BlockPatternCapability.CODEC).sync(BlockPatternCapability.STREAM_CODEC).build());
    public static final Supplier<AttachmentType<ComboMealCapability>> COMBO_MEAL = ATTACHMENT_TYPES.register("combo_meal", () -> AttachmentType.builder(() -> new ComboMealCapability(1f, 0)).serialize(ComboMealCapability.CODEC).sync(ByteBufCodecs.fromCodec(ComboMealCapability.CODEC)).build());
    public static final Supplier<AttachmentType<CorruptionCapability>> CHUNK_CORRUPTION = ATTACHMENT_TYPES.register("corruption", () -> AttachmentType.builder(CorruptionCapability::new).serialize(CorruptionCapability.CODEC).build());
    public static final Supplier<AttachmentType<GluedCapability>> GLUED = ATTACHMENT_TYPES.register("corruption", () -> AttachmentType.builder(GluedCapability::new).serialize(GluedCapability.CODEC).build());
    public static final Supplier<AttachmentType<HardenedMouthCapability>> HARDENED_MOUTH = ATTACHMENT_TYPES.register("hardened_mouth", () -> AttachmentType.builder(HardenedMouthCapability::new).serialize(HardenedMouthCapability.CODEC).build());
    public static final Supplier<AttachmentType<SlipperyCapability>> SLIPPERY = ATTACHMENT_TYPES.register("slippery", () -> AttachmentType.builder(SlipperyCapability::new).serialize(SlipperyCapability.CODEC).build());
    public static final Supplier<AttachmentType<UntouchableCapability>> UNTOUCHABLE = ATTACHMENT_TYPES.register("untouchable", () -> AttachmentType.builder(UntouchableCapability::new).serialize(UntouchableCapability.CODEC).build());
    public static final Supplier<AttachmentType<NutritionCapability>> NUTRITION = ATTACHMENT_TYPES.register("nutrition", () -> AttachmentType.builder(NutritionCapability::new).serialize(NutritionCapability.CODEC).build());


}
