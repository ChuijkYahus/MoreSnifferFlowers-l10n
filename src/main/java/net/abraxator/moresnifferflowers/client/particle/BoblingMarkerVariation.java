package net.abraxator.moresnifferflowers.client.particle;

import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum BoblingMarkerVariation {
    HUNGER("hunger"),
    BLOCK("block");

    private static final IntFunction<BoblingMarkerVariation> BY_ID = ByIdMap.continuous(BoblingMarkerVariation::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public final String name;
    
    BoblingMarkerVariation(String name) {
        this.name = name;
    }

    public static BoblingMarkerVariation byId(int id) {
        return BY_ID.apply(id);
    }
}
