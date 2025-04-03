package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static NutritionType getLargestNutrition(Item item){
        Map<NutritionType, Integer> map = new HashMap<>();
        Nutrition nutrition = Nutrition.getNutritionForItem(item);
        nutrition.nutritionEntries.forEach(entry ->
                map.merge(entry.nutrition(), entry.weight(), Integer::sum));
        int maxValueInMap = (Collections.max(map.values()));
        for (Map.Entry<NutritionType, Integer> entry :
                map.entrySet()) {

            if (entry.getValue() == maxValueInMap) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
