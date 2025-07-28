package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.components.PreviewState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public interface MultiblockRender {

    default Function<ResourceLocation, RenderType> getRenderType(PreviewState previewState) {
       return previewState.equals(PreviewState.PLACED) ? RenderType::entityCutout : RenderType::entityTranslucentCull;
    }


    default Level level(){
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null){
            throw new IllegalStateException("Blockentity Level is null");
        }
        return level;
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, PreviewState previewState) {
        float r = 1f;
        float g = 1f;
        float b = 1f;
        float alpha = 1f;

        render(modelPart, poseStack, vertexConsumer, packedLight, packedOverlay, r, g, b, alpha, previewState);
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float alpha, PreviewState previewState) {

        switch (previewState) {
            case PREVIEW -> alpha *= 0.5f;

            case INVALID -> {
                r = 1f;
                g *= 0.4f;
                b *= 0.4f;
                alpha *= 0.5f;
            }
        }

        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, r, g, b, alpha);
    }
}
