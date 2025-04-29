package net.abraxator.moresnifferflowers.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class ModFoods {
    public static final FoodProperties DAWNBERRY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build();
    public static final FoodProperties GLOOMBERRY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build();

    public static class ModConsumables {
        public static final Consumable FAST_CONSUMABLE = Consumables.defaultFood().consumeSeconds(0.8F).build();

        public static final Consumable GLOOMBERRY = Consumables.defaultFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(
                        List.of(
                                new MobEffectInstance(MobEffects.CONFUSION, 100, 1),
                                new MobEffectInstance(MobEffects.POISON, 100, 2)),
                        0.8F))
                .build();
    }

}
