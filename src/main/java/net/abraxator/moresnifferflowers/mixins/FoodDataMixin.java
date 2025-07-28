package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow public abstract void eat(int foodLevelModifier, float saturationLevelModifier);

    @Redirect(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    public void eat(FoodData instance, int foodLevelModifier, float saturationLevelModifier, @Local(argsOnly = true) LivingEntity entity){
        if (entity.hasEffect(ModEffects.BLAND.get())) {
            int amplifier = Objects.requireNonNull(entity.getEffect(ModEffects.BLAND.get())).getAmplifier() + 1;
            float division =  1f + amplifier / 2f;
            float hungerChance = 0.2f + amplifier / 10f;

            Level level = entity.level();
            if (!level.isClientSide &&  level.random.nextFloat() < hungerChance){
                entity.addEffect( new MobEffectInstance(MobEffects.HUNGER, Math.round(10 * division) * 20, amplifier - 1));
            }

            foodLevelModifier = Math.round(foodLevelModifier / division);
            saturationLevelModifier = saturationLevelModifier / division;

            eat(foodLevelModifier, saturationLevelModifier);
        }
    }
}
