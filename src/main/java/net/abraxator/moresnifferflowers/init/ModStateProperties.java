package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.blockentities.CropressorBlockEntity;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.*;

public class ModStateProperties {
    public static final IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);
    public static final IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);
    public static final IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);
    public static final IntegerProperty AGE_8 = IntegerProperty.create("age", 0, 8);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty SHEARED = BooleanProperty.create("sheared");
    public static final BooleanProperty VIVICUS_CURED = BooleanProperty.create("vivicus_cured");
    public static final IntegerProperty LAYER = BlockStateProperties.LAYERS;
    public static final BooleanProperty EMPTY = BooleanProperty.create("empty");
    public static final IntegerProperty FULLNESS = IntegerProperty.create("fullness", 0, 8);
    public static final IntegerProperty USES_4 = IntegerProperty.create("uses", 0, 3);
    public static final BooleanProperty CURED = BooleanProperty.create("cured");
    public static final IntegerProperty AMOUNT_4 = IntegerProperty.create("amount", 1, 4);
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final BooleanProperty HYDRATED = BooleanProperty.create("hydrated");
    public static final EnumProperty<BlockPattern> BLOCK_PATTERN = EnumProperty.create("block_pattern", BlockPattern.class);
    public static final IntegerProperty FIRE_TICKS = IntegerProperty.create("fire_ticks", 0, 5);
    public static final BooleanProperty CROWDED = BooleanProperty.create("crowded");



}
