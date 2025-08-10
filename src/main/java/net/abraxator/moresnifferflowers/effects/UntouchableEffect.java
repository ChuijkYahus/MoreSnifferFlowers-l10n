package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class UntouchableEffect extends MobEffect {
    public UntouchableEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player && !player.level().isClientSide)
            player.getCapability(CapabilityList.UNTOUCHABLE).ifPresent(cap -> cap.tick(player, amplifier));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
