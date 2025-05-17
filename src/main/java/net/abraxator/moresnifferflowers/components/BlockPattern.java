package net.abraxator.moresnifferflowers.components;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

public enum BlockPattern implements StringRepresentable {
    LINES("lines", ModItems.BLOCK_PATTERN_LINES.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    BRICKS("bricks", ModItems.BLOCK_PATTERN_BRICKS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    FOCUS("focus", ModItems.BLOCK_PATTERN_FOCUS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    BUBBLES("bubbles", ModItems.BLOCK_PATTERN_BUBBLES.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    CLOUDS("clouds", ModItems.BLOCK_PATTERN_CLOUDS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    DEEPSLATE("deepslate", ModItems.BLOCK_PATTERN_DEEPSLATE.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    DIAMOND("diamond", ModItems.BLOCK_PATTERN_DIAMOND.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    EYE("eye", ModItems.BLOCK_PATTERN_EYE.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    HEARTS("hearts", ModItems.BLOCK_PATTERN_HEARTS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    HONEYCOMB("honeycomb", ModItems.BLOCK_PATTERN_HONEYCOMB.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    PAWS("paws", ModItems.BLOCK_PATTERN_PAWS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    PRISMARINE("prismarine", ModItems.BLOCK_PATTERN_PRISMARINE.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    SPROUTS("sprouts", ModItems.BLOCK_PATTERN_SPROUTS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),
    STARS("stars", ModItems.BLOCK_PATTERN_STARS.get(), Color.HSBtoRGB(0.5f, 0.5f, 0.5f)),


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

    public static BlockPattern fromId(int index) {
        return BY_ID.apply(index);
    }

    public static @Nullable BlockPattern fromItem(Item item){
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.item == item).findFirst().orElse(null);
    }

    public int getColor() {
        return color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public static @Nullable BlockPattern fromPatternspria(ItemStack stack) {
        if (stack.getOrCreateTag().contains("patternId")) {
            int patternId = stack.getTag().getInt("patternId");
            if (patternId == -1) return null;
            return fromId(patternId);
        }
        return null;
    }

    public boolean isSamePattern(ItemStack patternspria) {
        int patternId = patternspria.getOrCreateTag().getInt("patternId");
        return patternId == this.ordinal();
    }

    public ItemStack getItemStack(ItemStack patternspria) {
        int amount = patternspria.getOrCreateTag().getInt("amount");
        return item.getDefaultInstance().copyWithCount(amount);
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount) {
        setPatternToHolderStack(itemStack, patternToInsert, amount, 4);
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount, int uses) {
        if (fromItem(patternToInsert.getItem()) == null) {
            removePatternFromStack(itemStack);
            return;
        }
        int patternId = Objects.requireNonNull(fromItem(patternToInsert.getItem())).ordinal();
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("amount", amount);
        tag.putInt("patternId", patternId);
        tag.putInt("uses", uses);
        itemStack.setTag(tag);
    }

    public static void removePatternFromStack(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("amount", 0);
        tag.putInt("patternId", -1);
        tag.putInt("uses", 0);
        itemStack.setTag(tag);
    }

}