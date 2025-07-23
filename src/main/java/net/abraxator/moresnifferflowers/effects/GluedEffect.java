package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GluedEffect extends MobEffect {
    public GluedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(Vec3.ZERO);
        entity.setJumping(false);
        entity.hasImpulse = false;

        if (entity.level().getGameTime() % 41 == 0) {
            GluedCapability.setAndSync(entity, true);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
