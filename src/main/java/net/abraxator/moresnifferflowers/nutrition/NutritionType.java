package net.abraxator.moresnifferflowers.nutrition;

import net.minecraft.ChatFormatting;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.DyeColor;

import java.awt.*;
import java.util.function.IntFunction;

public enum NutritionType {
    SWEET("sweet", ChatFormatting.RED.getColor()),
    SALTY("salty", ChatFormatting.GRAY.getColor()),
    SOUR("sour", ChatFormatting.YELLOW.getColor()),
    NEUTRAL("neutral", ChatFormatting.DARK_GREEN.getColor()),
    SPICY("spicy", ChatFormatting.DARK_RED.getColor()),;
    
    public final String name;
    public final int color;
    private static final IntFunction<NutritionType> BY_ID = ByIdMap.continuous(NutritionType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    
    NutritionType(String name, int color) {
        this.name = name;
        this.color = color;
    }
    
    public static NutritionType byId(int id) {
        return BY_ID.apply(id);
    }
}
