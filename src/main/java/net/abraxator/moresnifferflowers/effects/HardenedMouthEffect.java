package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HardenedMouthEffect extends MobEffect {
    public HardenedMouthEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player && !livingEntity.level().isClientSide)
            player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> cap.tick(player));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
