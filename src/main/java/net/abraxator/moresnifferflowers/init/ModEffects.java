package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
            ForgeRegistries.MOB_EFFECTS, MoreSnifferFlowers.MOD_ID);
    public static final RegistryObject<MobEffect> EXTRACTED = EFFECTS.register("extracted", () -> new ExtractedEffect(MobEffectCategory.NEUTRAL, 14058905));

    //Sour
    public static final RegistryObject<MobEffect> NEGATIVE_SOUR = EFFECTS.register("negative_sour", () -> new SimpleEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> POSITIVE_SOUR = EFFECTS.register("positive_sour", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    //Salty
    public static final RegistryObject<MobEffect> SALTY = EFFECTS.register("salty", () -> new SaltyEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> COMBO_MEAL = EFFECTS.register("combo_meal", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    //Spicy
    public static final RegistryObject<MobEffect> PANTS_ON_FIRE = EFFECTS.register("pants_on_fire", () -> new PantsOnFireEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> HARDENED_MOUTH = EFFECTS.register("hardened_mouth", () -> new HardenedMouthEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00));
    //Sweet
    public static final RegistryObject<MobEffect> STICKY = EFFECTS.register("sticky", () -> new StickyEffect(MobEffectCategory.HARMFUL, 0xa3679c));
    public static final RegistryObject<MobEffect> GLUING_TOUCH = EFFECTS.register("gluing_touch", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0xe084b8));
    public static final RegistryObject<MobEffect> GLUED = EFFECTS.register("glued", () -> new GluedEffect(MobEffectCategory.HARMFUL, 0x863c93)); // caused by the 2 above

    // Neutral
    public static final RegistryObject<MobEffect> BLAND = EFFECTS.register("bland", () -> new SimpleEffect(MobEffectCategory.HARMFUL, 0x08d7b00));
    public static final RegistryObject<MobEffect> WELL_BALANCED = EFFECTS.register("well_balanced", () -> new WellBalancedEffect(MobEffectCategory.BENEFICIAL, 0x08d7b00)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "41DD0153-E92A-486E-9800-EFFEC12C4386", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "41DD0153-E92A-486E-9800-EFFEC22C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.LUCK, "41DD0153-E92A-486E-9800-EFFEC32C4386", 0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "41DD0153-E92A-486E-9800-EFFEC42C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.MAX_HEALTH, "41DD0153-E92A-486E-9800-EFFEC52C4386", 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ARMOR, "41DD0153-E92A-486E-9800-EFFEC62C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "41DD0153-E92A-486E-9800-EFFEC72C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, "41DD0153-E92A-486E-9800-EFFEC82C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, "41DD0153-E92A-486E-9800-EFFEC92C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, "41DD0153-E92A-486E-9800-EFFEC13C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "41DD0153-E92A-486E-9800-EFFEC23C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(ForgeMod.BLOCK_REACH.get(), "41DD0153-E92A-486E-9800-EFFEC33C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "41DD0153-E92A-486E-9800-EFFEC43C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "41DD0153-E92A-486E-9800-EFFEC53C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(ForgeMod.ENTITY_REACH.get(), "41DD0153-E92A-486E-9800-EFFEC63C4386", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)

    );

}
