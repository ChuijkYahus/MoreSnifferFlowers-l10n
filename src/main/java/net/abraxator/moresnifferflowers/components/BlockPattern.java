package net.abraxator.moresnifferflowers.components;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.awt.*;
import java.util.Arrays;
import java.util.function.IntFunction;

public enum BlockPattern implements StringRepresentable {
    LINES("lines", ModItems.BLOCK_PATTERN_1.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    BRICKS("bricks", ModItems.BLOCK_PATTERN_2.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    FOCUS("focus", ModItems.BLOCK_PATTERN_3.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),

    ;

    public static final IntFunction<BlockPattern> BY_ID = ByIdMap.continuous(BlockPattern::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final String name;
    private final Item item;
    private final int color;

    BlockPattern(String name, Item item, int color){
        this.name = name;
        this.item = item;
        this.color = color;
    }

    public static BlockPattern byIndex(int index) {
        return BY_ID.apply(index);
    }

    public static BlockPattern fromItem(Item item){
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.item == item).findFirst().orElse(null);
    }

    @Override
    public String getSerializedName() {
        return "";
    }

}