package net.abraxator.moresnifferflowers.components;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

public enum BlockPattern implements StringRepresentable {
    PIPES("pipes", ModItems.BLOCK_PATTERN_PIPES.get(), 0x79b055, DyeColor.LIME), //lime
    BRICKS("bricks", ModItems.BLOCK_PATTERN_BRICKS.get(), 0xae457c, DyeColor.MAGENTA), //magenta
    FOCUS("focus", ModItems.BLOCK_PATTERN_FOCUS.get(), 0x6a3d87, DyeColor.PURPLE), //purple
    BUBBLES("bubbles", ModItems.BLOCK_PATTERN_BUBBLES.get(), 0x6dbdba, DyeColor.LIGHT_BLUE), //lightblue
    CLOUDS("clouds", ModItems.BLOCK_PATTERN_CLOUDS.get(), 0xddddd0, DyeColor.WHITE), //white
    DEEPSLATE("deepslate", ModItems.BLOCK_PATTERN_DEEPSLATE.get(), 0x413f51, DyeColor.BLACK), //black
    DIAMOND("diamond", ModItems.BLOCK_PATTERN_DIAMOND.get(), 0x4a9887, DyeColor.CYAN), //cyan
    EYE("eye", ModItems.BLOCK_PATTERN_EYE.get(), 0x98a5a7, DyeColor.LIGHT_GRAY), //lightgray
    HEARTS("hearts", ModItems.BLOCK_PATTERN_HEARTS.get(), 0xa63e3b, DyeColor.RED), //red
    HONEYCOMB("honeycomb", ModItems.BLOCK_PATTERN_HONEYCOMB.get(), 0xbe7b3a, DyeColor.ORANGE), //orange
    PAWS("paws", ModItems.BLOCK_PATTERN_PAWS.get(), 0x7d5840, DyeColor.BROWN), //brown
    PRISMARINE("prismarine", ModItems.BLOCK_PATTERN_PRISMARINE.get(), 0x5351ad, DyeColor.BLUE), //blue
    SPROUTS("sprouts", ModItems.BLOCK_PATTERN_SPROUTS.get(), 0x4e8646, DyeColor.GREEN), //green
    STARS("stars", ModItems.BLOCK_PATTERN_STARS.get(), 0xeed462, DyeColor.YELLOW), //yellow
    COVER("cover", ModItems.BLOCK_PATTERN_COVER.get(), 0x736979, DyeColor.GRAY), //gray
    FLOWERS("flowers", ModItems.BLOCK_PATTERN_FLOWERS.get(), 0xb45da6, DyeColor.PINK), //pink

    FLOWER_CHARGE("flower_charge", Items.FLOWER_BANNER_PATTERN, 0xffebb6, null),
    GLOBE("globe", Items.GLOBE_BANNER_PATTERN, 0xffebb6, null),
    SNOUT("snout", Items.PIGLIN_BANNER_PATTERN, 0xffebb6, null),
    CREEPER_CHARGE("creeper_charge", Items.CREEPER_BANNER_PATTERN, 0xffebb6, null),
    SKULL_CHARGE("skull_charge", Items.SKULL_BANNER_PATTERN, 0xffebb6, null),
    THING("thing", Items.MOJANG_BANNER_PATTERN, 0xffebb6, null),
    AMBUSH("ambush", ModItems.AMBUSH_BANNER_PATTERN.get(), 0xffebb6, null),
    EVIL("evil", ModItems.EVIL_BANNER_PATTERN.get(), 0xffebb6, null),


    ;

    public static final IntFunction<BlockPattern> BY_ID = ByIdMap.continuous(BlockPattern::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final String name;
    private final Item item;
    private final int color;
    private final DyeColor dyeColor;

    BlockPattern(String name, Item item, int color, DyeColor dyeColor){
        this.name = name;
        this.item = item;
        this.color = color;
        this.dyeColor = dyeColor;
    }

    public static BlockPattern fromId(int index) {
        return BY_ID.apply(index);
    }

    public static @Nullable BlockPattern fromItem(Item item){
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.item == item).findFirst().orElse(null);
    }

    public static BlockPattern fromDyeColor(DyeColor dyeColor) {
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.dyeColor == dyeColor).findFirst().orElse(null);
    }

    public int getColor() {
        return color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public Item getItem(){
        return item;
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