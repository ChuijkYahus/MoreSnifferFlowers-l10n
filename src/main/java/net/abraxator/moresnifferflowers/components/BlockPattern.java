package net.abraxator.moresnifferflowers.components;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public enum BlockPattern implements StringRepresentable {
    PIPES("pipes", MoreSnifferFlowers.loc("block_pattern_pipes"),0, 0x79b055, DyeColor.LIME), //lime
    BRICKS("bricks", MoreSnifferFlowers.loc("block_pattern_bricks"),1, 0xae457c, DyeColor.MAGENTA), //magenta
    FOCUS("focus", MoreSnifferFlowers.loc("block_pattern_focus"),2, 0x6a3d87, DyeColor.PURPLE), //purple
    BUBBLES("bubbles", MoreSnifferFlowers.loc("block_pattern_bubbles"),3, 0x6dbdba, DyeColor.LIGHT_BLUE), //lightblue
    CLOUDS("clouds", MoreSnifferFlowers.loc("block_pattern_clouds"),4, 0xddddd0, DyeColor.WHITE), //white
    DEEPSLATE("deepslate", MoreSnifferFlowers.loc("block_pattern_deepslate"),5, 0x413f51, DyeColor.BLACK), //black
    DIAMOND("diamond", MoreSnifferFlowers.loc("block_pattern_diamond"),6, 0x4a9887, DyeColor.CYAN), //cyan
    EYE("eye", MoreSnifferFlowers.loc("block_pattern_eye"),7, 0x98a5a7, DyeColor.LIGHT_GRAY), //lightgray
    HEARTS("hearts", MoreSnifferFlowers.loc("block_pattern_hearts"),8, 0xa63e3b, DyeColor.RED), //red
    HONEYCOMB("honeycomb", MoreSnifferFlowers.loc("block_pattern_honeycomb"),9, 0xbe7b3a, DyeColor.ORANGE), //orange
    PAWS("paws", MoreSnifferFlowers.loc("block_pattern_paws"),10, 0x7d5840, DyeColor.BROWN), //brown
    PRISMARINE("prismarine", MoreSnifferFlowers.loc("block_pattern_prismarine"),11, 0x5351ad, DyeColor.BLUE), //blue
    SPROUTS("sprouts", MoreSnifferFlowers.loc("block_pattern_sprouts"),12, 0x4e8646, DyeColor.GREEN), //green
    STARS("stars", MoreSnifferFlowers.loc("block_pattern_stars"),13, 0xeed462, DyeColor.YELLOW), //yellow
    COVER("cover", MoreSnifferFlowers.loc("block_pattern_cover"),14, 0x736979, DyeColor.GRAY), //gray
    FLOWERS("flowers", MoreSnifferFlowers.loc("block_pattern_flowers"),15, 0xb45da6, DyeColor.PINK), //pink

    FLOWER_CHARGE("flower_charge", MoreSnifferFlowers.vanillaLoc("flower_banner_pattern"),16, 0xffebb6, null),
    GLOBE("globe", MoreSnifferFlowers.vanillaLoc("globe_banner_pattern"),17, 0xffebb6, null),
    SNOUT("snout", MoreSnifferFlowers.vanillaLoc("piglin_banner_pattern"),18, 0xffebb6, null),
    CREEPER_CHARGE("creeper_charge", MoreSnifferFlowers.vanillaLoc("creeper_banner_pattern"),19, 0xffebb6, null),
    SKULL_CHARGE("skull_charge", MoreSnifferFlowers.vanillaLoc("skull_banner_pattern"),20, 0xffebb6, null),
    THING("thing", MoreSnifferFlowers.vanillaLoc("mojang_banner_pattern"),21, 0xffebb6, null),
    AMBUSH("ambush", MoreSnifferFlowers.loc("ambush_banner_pattern"),22, 0xffebb6, null),
    EVIL("evil", MoreSnifferFlowers.loc("evil_banner_pattern"),23, 0xffebb6, null),

    EMPTY("empty", null, -1, 0xddddd0, null)

    ;

    private final String name;
    private final ResourceLocation item;
    private final int id;
    private final int color;
    private final DyeColor dyeColor;

    BlockPattern(String name, ResourceLocation item, int id, int color, DyeColor dyeColor){
        this.name = name;
        this.item = item;
        this.id = id;
        this.color = color;
        this.dyeColor = dyeColor;
    }

    public static BlockPattern fromId(int index) {
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.id == index).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromItem(Item item){
        return Arrays.stream(values()).filter(blockPattern -> Objects.equals(blockPattern.item, ForgeRegistries.ITEMS.getKey(item))).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromDyeColor(DyeColor dyeColor) {
        return Arrays.stream(values()).filter(blockPattern -> blockPattern.dyeColor == dyeColor).findFirst().orElse(EMPTY);
    }

    public static BlockPattern fromState(BlockState state) {
        return state.getValue(ModStateProperties.BLOCK_PATTERN);
    }

    public static BlockPattern fromPatternspria(ItemStack stack) {
        if (stack.getOrCreateTag().contains("patternId")) {
            int patternId = stack.getTag().getInt("patternId");
            return fromId(patternId);
        }
        return EMPTY;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public Item getItem(){
        return ForgeRegistries.ITEMS.getValue(item);
    }

    public boolean isBanner(){
        return id > 15 && id < 24;
    }

    public boolean isSamePattern(ItemStack patternspria) {
        int patternId = patternspria.getOrCreateTag().getInt("patternId");
        return patternId == this.ordinal();
    }

    public ItemStack getItemStack(ItemStack patternspria) {
        int amount = patternspria.getOrCreateTag().getInt("amount");
        return getItem().getDefaultInstance().copyWithCount(amount);
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount) {
        setPatternToHolderStack(itemStack, patternToInsert, amount, PatternspriaItem.getPatternspriaUses(itemStack));
    }

    public static void setPatternToHolderStack(ItemStack itemStack, ItemStack patternToInsert, int amount, int uses) {
        if (fromItem(patternToInsert.getItem()) == EMPTY) {
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
        tag.putInt("amount", 4);
        tag.putInt("patternId", -1);
        tag.putInt("uses", 0);
        itemStack.setTag(tag);
    }

    public static boolean isEmpty(BlockState state) {
        return state.getValue(ModStateProperties.BLOCK_PATTERN) == EMPTY;
    }

/*    public static final Map<Item, Integer> itemToPatternId = Map.ofEntries(
        Map.entry(ModItems.BLOCK_PATTERN_PIPES.get(), 0),
        Map.entry(ModItems.BLOCK_PATTERN_BRICKS.get(), 1),
        Map.entry(ModItems.BLOCK_PATTERN_FOCUS.get(), 2),
        Map.entry(ModItems.BLOCK_PATTERN_BUBBLES.get(), 3),
        Map.entry(ModItems.BLOCK_PATTERN_CLOUDS.get(), 4),
        Map.entry(ModItems.BLOCK_PATTERN_DEEPSLATE.get(), 5),
        Map.entry(ModItems.BLOCK_PATTERN_DIAMOND.get(), 6),
        Map.entry(ModItems.BLOCK_PATTERN_EYE.get(), 7),
        Map.entry(ModItems.BLOCK_PATTERN_HEARTS.get(), 8),
        Map.entry(ModItems.BLOCK_PATTERN_HONEYCOMB.get(), 9),
        Map.entry(ModItems.BLOCK_PATTERN_PAWS.get(), 10),
        Map.entry(ModItems.BLOCK_PATTERN_PRISMARINE.get(), 11),
        Map.entry(ModItems.BLOCK_PATTERN_SPROUTS.get(), 12),
        Map.entry(ModItems.BLOCK_PATTERN_STARS.get(), 13),
        Map.entry(ModItems.BLOCK_PATTERN_COVER.get(), 14),
        Map.entry(ModItems.BLOCK_PATTERN_FLOWERS.get(), 15),
        Map.entry(Items.FLOWER_BANNER_PATTERN, 16),
        Map.entry(Items.GLOBE_BANNER_PATTERN, 17),
        Map.entry(Items.PIGLIN_BANNER_PATTERN, 18),
        Map.entry(Items.CREEPER_BANNER_PATTERN, 19),
        Map.entry(Items.SKULL_BANNER_PATTERN, 20),
        Map.entry(Items.MOJANG_BANNER_PATTERN, 21),
        Map.entry(ModItems.AMBUSH_BANNER_PATTERN.get(), 22),
        Map.entry(ModItems.EVIL_BANNER_PATTERN.get(), 23)

    );*/
    
}