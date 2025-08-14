package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.blockentities.ModBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.components.PreviewState;
import net.abraxator.moresnifferflowers.init.ModBlocks;
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
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public interface MultiblockRender {

    default Function<ResourceLocation, RenderType> getRenderTypeFunction(PreviewState previewState) {
       return previewState.equals(PreviewState.PLACED) ? RenderType::entityCutout : RenderType::entityTranslucentCull;
    }

    default RenderType getRenderType(PreviewState previewState, ResourceLocation location) {
        return previewState.equals(PreviewState.PLACED) ? RenderType.entityCutout(location) : RenderType.entityTranslucentCull(location);
    }

    default VertexConsumer getConsumer(MultiBufferSource buffer, MultiBlockEntity blockEntity, Material materialBase, Material materialCorrupted, Block blockCorrupted) {
        PreviewState previewState = blockEntity.previewState;

        RenderType renderTypeBase = getRenderType(previewState, materialBase.atlasLocation());
        RenderType renderTypeCorrupted = getRenderType(previewState, materialCorrupted.atlasLocation());

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

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, PreviewState previewState) {
        render(modelPart, poseStack, vertexConsumer, packedLight, packedOverlay, 0xffffffff, previewState);
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int r, int g, int b, int alpha, PreviewState previewState) {
        render(modelPart, poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(alpha, r, g, b), previewState);
    }

    default void render(ModelPart modelPart, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, PreviewState previewState) {
        float r = FastColor.ARGB32.red(color);
        float g = FastColor.ARGB32.green(color);
        float b = FastColor.ARGB32.blue(color);
        float a = FastColor.ARGB32.alpha(color);

        switch (previewState) {
            case PREVIEW -> a *= 0.5f;

            case INVALID -> {
                r = 255;
                g *= 0.4f;
                b *= 0.4f;
                a *= 0.5f;
            }
        }

        modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color((int) a, (int) r, (int) g, (int) b));
    }
}
