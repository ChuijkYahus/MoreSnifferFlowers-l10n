package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.components.PreviewMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public interface MultiblockRender {

    default Function<ResourceLocation, RenderType> getRenderTypeFunction(PreviewMode previewMode) {
       return previewMode.equals(PreviewMode.PLACED) ? RenderType::entityCutout : RenderType::entityTranslucentCull;
    }

    default RenderType getRenderType(PreviewMode previewMode, ResourceLocation location) {
        return previewMode.equals(PreviewMode.PLACED) ? RenderType.entityCutout(location) : RenderType.entityTranslucentCull(location);
    }

    default VertexConsumer getConsumer(MultiBufferSource buffer, MultiBlockEntity blockEntity, Material materialBase, Material materialCorrupted, Block blockCorrupted) {
        PreviewMode previewMode = blockEntity.previewMode;

        RenderType renderTypeBase = getRenderType(previewMode, materialBase.atlasLocation());
        RenderType renderTypeCorrupted = getRenderType(previewMode, materialCorrupted.atlasLocation());

        VertexConsumer baseConsumer = materialBase.sprite().wrap(buffer.getBuffer(renderTypeBase));
        VertexConsumer corruptedConsumer = materialCorrupted.sprite().wrap(buffer.getBuffer(renderTypeCorrupted));

        return blockEntity.getBlockState().is(blockCorrupted) ? corruptedConsumer : baseConsumer;
    }


    default Level level(){
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null){
            throw new IllegalStateException("Blockentity Level is null");
        }
        return level;
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, PreviewMode previewMode) {
        render(modelPart, poseStack, vertexConsumer, packedLight, packedOverlay, 0xffffffff, previewMode);
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int r, int g, int b, int alpha, PreviewMode previewMode) {
        render(modelPart, poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(alpha, r, g, b), previewMode);
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, PreviewMode previewMode) {
        float r = FastColor.ARGB32.red(color);
        float g = FastColor.ARGB32.green(color);
        float b = FastColor.ARGB32.blue(color);
        float a = FastColor.ARGB32.alpha(color);

        switch (previewMode) {
            case PREVIEW -> a *= PreviewMode.PREVIEW.alpha;

            case INVALID -> {
                r *= PreviewMode.INVALID.red;
                g *= PreviewMode.INVALID.green;
                b *= PreviewMode.INVALID.blue;
                a *= PreviewMode.INVALID.alpha;
            }
        }

        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color((int) a, (int) r, (int) g, (int) b));
    }
}
