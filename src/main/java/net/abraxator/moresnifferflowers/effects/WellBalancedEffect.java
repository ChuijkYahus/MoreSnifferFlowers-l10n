package net.abraxator.moresnifferflowers.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class WellBalancedEffect extends MobEffect {
    public WellBalancedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
        if (livingEntity.getHealth() > livingEntity.getMaxHealth()) {
            livingEntity.setHealth(livingEntity.getMaxHealth());
        }

    }

}
