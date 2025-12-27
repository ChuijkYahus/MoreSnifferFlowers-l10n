package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

import java.util.ArrayList;
import java.util.List;

public class RootedSoupItem extends Item {
    public RootedSoupItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemstack);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Player player = (Player) livingEntity;
        FoodData foodData = player.getFoodData();
        CompoundTag tag = stack.getOrCreateTag();
        int food = tag.getInt("soupFood");
        int sat = tag.getInt("soupSat");
        List<MobEffectInstance> effects = new ArrayList<>();
        ListTag effectsTag = tag.getList("effects", 10);

        for (Tag tag1 : effectsTag) {
            CompoundTag effectTag = (CompoundTag) tag1;
            int id = effectTag.getInt("nutritionType");
            int dur = effectTag.getInt("dur");
            int amp = effectTag.getInt("amp");
            boolean positive = effectTag.getBoolean("positive");

            player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> cap.unlockedEffects.add(NutritionCapability.idFromNutrition(NutritionType.byId(id), positive)));

            MobEffect mobEffect = NutritionType.getEffect(NutritionType.byId(id), positive);
           if (mobEffect != null) {
               effects.add(new MobEffectInstance(mobEffect, dur, amp));
           }
        }
        
        foodData.eat(food, sat);
        if (!level.isClientSide) {
            for (MobEffectInstance effect : effects) {
                player.addEffect(effect);
            }
        }

        int uses = tag.getInt("soupCount") - 1;
        
        if(uses <= 0) {
            return Items.BOWL.getDefaultInstance();
        }

        // Cookbook unlocking
        ListTag ingredientListTag = tag.getList("ingredients", 10);
        for (Tag ingredientTag : ingredientListTag) {
            ItemStack ingredient = ItemStack.of((CompoundTag) ingredientTag);
            player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(nutritionCapability -> {
                nutritionCapability.addItem(ingredient.getItem());
            });
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
        return stack.getOrCreateTag().getInt("soupCount") > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int input = stack.getOrCreateTag().getInt("soupCount");
        int maxInput= stack.getOrCreateTag().contains("soupCountMax") ? stack.getOrCreateTag().getInt("soupCountMax") : 4;

        return ModColorHandler.barColorHelper(input, maxInput);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int max = stack.getOrCreateTag().contains("soupCountMax") ? stack.getOrCreateTag().getInt("soupCountMax") : 4;
        return Math.round((float) stack.getOrCreateTag().getInt("soupCount") / max * 13.0F);
    }
}
