package net.abraxator.moresnifferflowers.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GiantSoupItem extends Item {
    public GiantSoupItem(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return super.getFoodProperties(stack, entity);
    }
}
