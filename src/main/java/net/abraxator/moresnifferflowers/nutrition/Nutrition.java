package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.*;

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
        Set<Nutrition> nutritions = NutritionLoader.modNutritions.get(BuiltInRegistries.ITEM.getKey(item).getNamespace());
        if (nutritions != null) {
            for (Nutrition nutrition : nutritions) {
                if (nutrition.getItem() == item) {
                    return nutrition;
                }
            }
        }
        return EMPTY;
    }

    public static NutritionType getLargestNutrition(Item item){
        Nutrition nutrition = Nutrition.getNutritionForItem(item);

        List<NutritionEntry> list =  new ArrayList<>(nutrition.nutritionEntries);
        list.sort(Comparator.comparing(nutritionEntry -> -(nutritionEntry.weight() + nutritionEntry.nutrition().priority)));

        return list.get(0).nutrition();
    }
    
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
