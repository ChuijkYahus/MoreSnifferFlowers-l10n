package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.function.Function;

public class ModRenderTypes extends RenderType {
    public ModRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_CRUMBLING = Util.memoize(resourceLocation -> {

        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(resourceLocation, false, true);

        return RenderType.create(
            MoreSnifferFlowers.sLoc("translucent_crumbling"),

                DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_CRUMBLING_SHADER)
                        .setTextureState(textureStateShard)
                        .setTransparencyState(CRUMBLING_TRANSPARENCY)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .setLayeringState(POLYGON_OFFSET_LAYERING)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .createCompositeState(true)
        );
    });

    public static final RenderType CRUMBLING_BLOCK = create(
            MoreSnifferFlowers.sLoc("crumbling"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_CRUMBLING_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(CRUMBLING_TRANSPARENCY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setLayeringState(POLYGON_OFFSET_LAYERING)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .createCompositeState(true)
    );


    public static RenderType translucentCrumbling(ResourceLocation location) {
        return TRANSLUCENT_CRUMBLING.apply(location);
    }

}
