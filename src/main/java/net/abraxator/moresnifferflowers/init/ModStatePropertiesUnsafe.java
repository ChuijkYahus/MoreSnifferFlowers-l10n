package net.abraxator.moresnifferflowers.init;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ModStatePropertiesUnsafe {


    public static final BooleanProperty NOT_CURED = BooleanProperty.create("not_cured");
    public static final BooleanProperty NOT_CORRUPTED = BooleanProperty.create("not_corrupted");

    public static boolean hasCustomLeavesProperties(BlockState state){
        return state.getOptionalValue(ModStatePropertiesUnsafe.NOT_CORRUPTED).isPresent() && state.getOptionalValue(ModStatePropertiesUnsafe.NOT_CORRUPTED).isPresent();
    }
}
