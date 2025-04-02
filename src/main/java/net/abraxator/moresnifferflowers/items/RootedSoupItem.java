package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RootedSoupItem extends Item {
    public RootedSoupItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (itemstack.isEdible()) {
            if (player.canEat(itemstack.getFoodProperties(player).canAlwaysEat())) {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        }
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Player player = (Player) livingEntity;
        FoodData foodData = player.getFoodData();
        CompoundTag tag = stack.getOrCreateTag();
        int food = tag.getInt("soupFood");
        int sat = tag.getInt("soupSat");
        List<MobEffectInstance> effects = new ArrayList<>();
        ListTag effectsTag = tag.getList("soupSats", 10);
        for (Tag tag1 : effectsTag) {
            CompoundTag effectTag = (CompoundTag) tag1;
            int id = effectTag.getInt("nutritionType");
            int dur = effectTag.getInt("dur");
            int amp = effectTag.getInt("amp");
            boolean positive = effectTag.getBoolean("positive");
            MobEffect mobEffect = NutritionType.getEffect(NutritionType.byId(id), positive);
            effects.add(new MobEffectInstance(mobEffect, dur, amp));
        }
        
        foodData.eat(food, sat);
        for (MobEffectInstance effect : effects) {
            player.addEffect(effect);
        }
        
        int uses = tag.getInt("soupCount") - 1;
        
        if(uses <= 0) {
            return Items.BOWL.getDefaultInstance();
        } 
        
        tag.putInt("soupCount", uses);
        stack.setTag(tag);
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrCreateTag().getInt("soupCount") > 4;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int lowColor = 0x8c1111;
        int highColor = 0x179529;
        int input = stack.getOrCreateTag().getInt("soupCount");
        int maxInput= 4;

        int lowRed = (lowColor >> 16) & 0xFF;
        int lowGreen = (lowColor >> 8) & 0xFF;
        int lowBlue = lowColor & 0xFF;

        int highRed = (highColor >> 16) & 0xFF;
        int highGreen = (highColor >> 8) & 0xFF;
        int highBlue = highColor & 0xFF;

        float[] lowHSB =  Color.RGBtoHSB(lowRed, lowGreen, lowBlue, null);
        float[] highHSB =  Color.RGBtoHSB(highRed, highGreen, highBlue, null);


        float finalHue = ((lowHSB[0] * (Math.abs(input - maxInput))) + (highHSB[0] * input)) / maxInput;

        return Mth.hsvToRgb(finalHue, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(stack.getOrCreateTag().getInt("soupCount") * 13.0F / 4);
    }
}
