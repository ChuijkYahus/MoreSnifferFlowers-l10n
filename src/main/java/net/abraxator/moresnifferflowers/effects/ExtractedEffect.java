package net.abraxator.moresnifferflowers.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

public class ExtractedEffect extends MobEffect {
    public ExtractedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.clear();
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getActiveEffects().size() <= 1){
            livingEntity.removeEffect(this);
        };
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
