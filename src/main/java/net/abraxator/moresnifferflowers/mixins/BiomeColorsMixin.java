package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStatePropertiesUnsafe;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @Inject(method = "getAverageFoliageColor", at = @At(value = "RETURN"), cancellable = true)
    private static void injectBlockColors(BlockAndTintGetter level, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
        BlockState blockState = level.getBlockState(blockPos);

        if (ModStatePropertiesUnsafe.hasCustomLeavesProperties(blockState) && !blockState.getValue(ModStatePropertiesUnsafe.NOT_CORRUPTED)) {
            float[] colorHSB = ModColorHandler.getColorHSB(cir.getReturnValue());
            cir.setReturnValue(Color.HSBtoRGB(-colorHSB[0] / 1.5F, colorHSB[1] - 0.25F, colorHSB[2] - 0.23F));
        }

    }
}
