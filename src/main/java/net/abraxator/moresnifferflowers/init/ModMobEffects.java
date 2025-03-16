package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.effects.ExtractedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.MOB_EFFECTS, MoreSnifferFlowers.MOD_ID);
    public static final RegistryObject<MobEffect> EXTRACTED = EFFECTS.register("extracted", () -> new ExtractedEffect(MobEffectCategory.NEUTRAL, 14058905));
    public static final RegistryObject<MobEffect> NEGATIVE_SOUR = EFFECTS.register("negative_sour", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_SOUR = EFFECTS.register("positive_sour", () -> new ExtractedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    public static final RegistryObject<MobEffect> NEGATIVE_SALTY = EFFECTS.register("negative_salty", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_SALTY = EFFECTS.register("positive_salty", () -> new ExtractedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    public static final RegistryObject<MobEffect> NEGATIVE_SPICY = EFFECTS.register("negative_spicy", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_SPICY = EFFECTS.register("positive_spicy", () -> new ExtractedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    public static final RegistryObject<MobEffect> NEGATIVE_SWEET = EFFECTS.register("negative_sweet", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_SWEET = EFFECTS.register("positive_sweet", () -> new ExtractedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    public static final RegistryObject<MobEffect> NEGATIVE_NEUTRAL = EFFECTS.register("negative_neutral", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_NEUTRAL = EFFECTS.register("positive_neutral", () -> new ExtractedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
}
