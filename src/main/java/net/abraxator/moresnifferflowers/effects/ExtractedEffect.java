package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExtractedEffect extends MobEffect {
    public ExtractedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return List.of();
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        List<MobEffectInstance> activeEffects = new ArrayList<>(livingEntity.getActiveEffects());
        activeEffects = activeEffects.stream().filter(mobEffectInstance -> !ModTags.hasEffectTag(mobEffectInstance.getEffect(), ModTags.ModEffectTags.EXTRACTION_BLACKLIST)).toList();

        if (activeEffects.size() <= 1){
            livingEntity.removeEffect(this);
        };
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
