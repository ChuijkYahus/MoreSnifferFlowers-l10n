package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.DyeColor;

import java.util.function.IntFunction;

public enum NutritionType {
    SWEET("sweet"),
    SALTY("salty"),
    SOUR("sour"),
    NEUTRAL("neutral"),
    SPICY("spicy");
    
    public final String name;
    private static final IntFunction<NutritionType> BY_ID = ByIdMap.continuous(NutritionType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    
    NutritionType(String name) {
        this.name = name;
    }
    
    public static NutritionType byId(int id) {
        return BY_ID.apply(id);
    }
}
