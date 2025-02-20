package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NutritionStack {
    public ItemStack stack;
    public Nutrition nutrition;

    public NutritionStack(ItemStack stack, Nutrition nutrition) {
        this.stack = stack;
        this.nutrition = nutrition;
    }
    
    public CompoundTag serialize(CompoundTag tag) {
        tag.put("itemStack", this.stack.save(new CompoundTag()));
        tag.putInt("nutritionEntries", this.nutrition.getNutritionEntries().size());
        for (int i = 0; i < this.nutrition.getNutritionEntries().size(); i++) {
            NutritionEntry nutritionEntry = this.nutrition.getNutritionEntries().get(i);
            tag.put("nutritionEntry" + i, nutritionEntry.serialize(new CompoundTag()));
        }
        
        return tag;
    }
    
    public static NutritionStack deserialize(CompoundTag tag) {
        ItemStack itemStack = ItemStack.of(tag.getCompound("itemStack"));
        int entries = tag.getInt("nutritionEntries");
        List<NutritionEntry> nutritionEntries = new ArrayList<>();
        for (int i = 0; i < entries; i++) {
            nutritionEntries.add(NutritionEntry.deserialize(tag.getCompound("nutritionEntry" + i)));
        }
        
        return new NutritionStack(itemStack, new Nutrition(itemStack.getItem(), nutritionEntries));
    }
}
