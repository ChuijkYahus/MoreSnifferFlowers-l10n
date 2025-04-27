package net.abraxator.moresnifferflowers.client.tints;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record DyespriaTint(int defaultColor) implements ItemTintSource {
    public static final MapCodec<DyespriaTint> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(DyespriaTint::defaultColor)).apply(instance, DyespriaTint::new)
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        Dye dye = Dye.getDyeFromDyespria(itemStack);
        if(dye.isEmpty()) {
            return defaultColor;
        } else {
            return Dye.colorForDye(((DyespriaItem) itemStack.getItem()), dye.color());
        }
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
