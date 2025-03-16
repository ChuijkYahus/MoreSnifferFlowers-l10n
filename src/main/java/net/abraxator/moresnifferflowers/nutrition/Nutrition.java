package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class Nutrition {
    private final Item item;
    private final List<NutritionEntry> nutritionEntries;
    private int saturation;
    public static final Nutrition EMPTY = new Nutrition(Items.AIR, List.of());
    
    public Nutrition(Item item, List<NutritionEntry> nutritionEntries) {
        this.item = item;
        this.nutritionEntries = nutritionEntries;

    }

    public Item getItem() {
        return item;
    }

    public List<NutritionEntry> getNutritionEntries() {
        return nutritionEntries;
    }
    
    public static Nutrition getNutritionForItem(Item item) {
        for (Nutrition nutrition : NutritionLoader.modNutritions.get(BuiltInRegistries.ITEM.getKey(item).getNamespace())) {
            if (nutrition.getItem() == item) {
                return nutrition;
            }
        }
        
        return EMPTY;
    }
    
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
