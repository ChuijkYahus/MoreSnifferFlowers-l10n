package net.abraxator.moresnifferflowers.blocks;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.init.ModDataComponents;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ColorableVivicusBlock extends Colorable {
    default EnumProperty<DyeColor> getColorProperty() {
        return ModStateProperties.COLOR;
    }
    
    default void addDye(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if(!blockState.hasProperty(getColorProperty())) {
            return;
        }
        
        var stack = player.getMainHandItem();
        Dye dye = Dye.getDyeFromDyespria(stack);
        RandomSource randomSource = level.random;

        if(blockState.getValue(getColorProperty()).equals(dye.color()) || dye.isEmpty()) {
            return;
        }

        level.setBlockAndUpdate(blockPos, blockState.setValue(getColorProperty(), dye.color()));
        ItemStack itemStack = Dye.stackFromDye(new Dye(dye.color(), dye.amount() - randomSource.nextIntBetweenInclusive(0, 1)));
        Dye.setDyeToDyeHolderStack(stack, itemStack, itemStack.getCount());
        
        if(!level.isClientSide()) {
            particles(randomSource, ((ServerLevel) level), dye, blockPos);
        }
    }

    default int getColorId(BlockPlaceContext context){
       return context.getItemInHand().getOrDefault(ModDataComponents.COLOR_ID, 0);
    }

    default @Nullable BlockState stateForPlacementHelper(BlockState state, BlockPlaceContext context) {
        if (state != null) {
            return state.setValue(ModStateProperties.COLOR, DyeColor.byId(getColorId(context)));
        }
        return null;
    }

    default @NotNull ItemStack cloneItemStackHelper(BlockState state, ItemStack stack) {
        int colorId = state.getValue(ModStateProperties.COLOR).getId();
        int color = colorValues().get(DyeColor.byId(colorId));

        stack.set(ModDataComponents.COLOR, color);
        stack.set(ModDataComponents.COLOR_ID, colorId);

        return stack;
    }

    @Override
    default Map<DyeColor, Integer> colorValues() {
        return Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
            dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFFFFFFF);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFFd2cad8);
            dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFFb0a4be);
            dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF837e9b);
            dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFFd6a27c);
            dyeColorHexFormatMap.put(DyeColor.RED, 0xFFffaca6);
            dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFffd180);
            dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFfff07a);
            dyeColorHexFormatMap.put(DyeColor.LIME, 0xFFddff97);
            dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFFa2ffb2);
            dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF9bffda);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFFbefffa);
            dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFFa7cdff);
            dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFFccb0ff);
            dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFe9adff);
            dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFffd7f7);
        });
    }
}
