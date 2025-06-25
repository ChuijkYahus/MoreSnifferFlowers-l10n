package net.abraxator.moresnifferflowers.nutrition;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.IntFunction;

public enum NutritionType {
    SOUR("sour", 0xe6a005),
    SALTY("salty", 0x8bb8c3),
    SPICY("spicy", 0xbb4330),
    SWEET("sweet", 0xe67896),
    NEUTRAL("neutral", 0x8c661e),;


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
        Map<NutritionType, ResourceLocation> map = positive ? POSITIVE_SOUP_EFFECT_MAP : NEGATIVE_SOUP_EFFECT_MAP;

        return ForgeRegistries.MOB_EFFECTS.getValue(map.get(nutritionType));
    }

    public static final Map<NutritionType, ResourceLocation> NEGATIVE_SOUP_EFFECT_MAP = Map.of(
            SOUR,MoreSnifferFlowers.loc("negative_sour"),
            SALTY, MoreSnifferFlowers.loc("negative_salty"),
            SPICY, MoreSnifferFlowers.loc("pants_on_fire"),
            SWEET, MoreSnifferFlowers.loc("sticky"),
            NEUTRAL, MoreSnifferFlowers.loc("bland")
    );

    public static final Map<NutritionType, ResourceLocation> POSITIVE_SOUP_EFFECT_MAP = Map.of(
            SOUR, MoreSnifferFlowers.loc("positive_sour"),
            SALTY,  MoreSnifferFlowers.loc("positive_salty"),
            SPICY,  MoreSnifferFlowers.loc("hardened_mouth"),
            SWEET,  MoreSnifferFlowers.loc("positive_sweet"),
            NEUTRAL, MoreSnifferFlowers.loc("well_balanced")
    );
}
