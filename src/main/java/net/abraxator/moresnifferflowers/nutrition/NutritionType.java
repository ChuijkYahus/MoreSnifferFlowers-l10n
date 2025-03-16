package net.abraxator.moresnifferflowers.nutrition;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.ChatFormatting;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.IntFunction;

public enum NutritionType {
    SOUR("sour", ChatFormatting.YELLOW.getColor()),
    SALTY("salty", ChatFormatting.GRAY.getColor()),
    SPICY("spicy", ChatFormatting.GOLD.getColor()),
    SWEET("sweet", ChatFormatting.LIGHT_PURPLE.getColor()),
    NEUTRAL("neutral", ChatFormatting.DARK_RED.getColor()),;


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
    
    public static MobEffect getEffect(NutritionType nutritionType, boolean positive) {
        return ForgeRegistries.MOB_EFFECTS.getValue(MoreSnifferFlowers.loc((positive ? "positive" : "negative") + "_" + nutritionType.name()));
    }
}
