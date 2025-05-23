package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelBlockRenderer.AmbientOcclusionFace.class)
public abstract class AmbientOcclusionFaceMixin implements BlockPatternRenderer.AmbientOcclusionFaceAccessor {

    @Shadow @Final
    float[] brightness;
    @Shadow @Final
    int[] lightmap;

    @Override
    public float[] moreSnifferFlowers$getBrightness() {
        return brightness;
    }

    @Override
    public int[] moreSnifferFlowers$getLightmap() {
        return lightmap;
    }
}
