package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.effects.ExtractedEffect;
import net.abraxator.moresnifferflowers.effects.MidEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.MOB_EFFECTS, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<MobEffect> EXTRACTED = EFFECTS.register("extracted", () -> new ExtractedEffect(MobEffectCategory.NEUTRAL, 14058905));
    public static final RegistryObject<MobEffect> MID = EFFECTS.register("mid", () -> new MidEffect(MobEffectCategory.NEUTRAL, 14058905));
    public static final RegistryObject<MobEffect> NEGATIVE_SWEET = EFFECTS.register("negative_sweet", () -> new ExtractedEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
}
