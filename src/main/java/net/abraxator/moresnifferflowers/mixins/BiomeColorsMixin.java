package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.init.ModStatePropertiesUnsafe;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @ModifyReturnValue(method = "getAverageFoliageColor", at = @At(value = "RETURN"))
    private static int injectBlockColors(int original, @Local(argsOnly = true) BlockAndTintGetter level, @Local(argsOnly = true) BlockPos blockPos) {
        return ModColorHandler.getTransformedLeavesColor(original, level, blockPos);
    }
}
