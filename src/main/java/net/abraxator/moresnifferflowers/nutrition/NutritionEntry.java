package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.nbt.CompoundTag;

public record NutritionEntry(NutritionType nutrition, int weight) {
    public CompoundTag serialize(CompoundTag tag) {
        tag.putInt("nutritionId", nutrition.ordinal());
        tag.putInt("nutritionValue", weight);
        return tag;
    }
    
    public static NutritionEntry deserialize(CompoundTag tag) {
        int id = tag.getInt("nutritionId");
        int weight = tag.getInt("nutritionValue");
        return new NutritionEntry(NutritionType.byId(id), weight);
    }
}
