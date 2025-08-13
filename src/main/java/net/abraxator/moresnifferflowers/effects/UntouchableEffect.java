package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class UntouchableEffect extends MobEffect {
    public UntouchableEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player)
            player.getData(ModDataAttachments.UNTOUCHABLE.get()).tick(player, amplifier);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
