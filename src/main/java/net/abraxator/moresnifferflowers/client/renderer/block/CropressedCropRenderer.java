package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.CropressedCropBlockEntity;
import net.abraxator.moresnifferflowers.client.ModRenderTypes;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CropressedCropRenderer<T extends CropressedCropBlockEntity> implements BlockEntityRenderer<T> {
    public ModelPart cube;
    public ModelPart invertedCube;

    public CropressedCropRenderer(BlockEntityRendererProvider.Context context) {
        cube = context.bakeLayer(ModModelLayerLocations.SIMPLE_CUBE);
        invertedCube = context.bakeLayer(ModModelLayerLocations.INVERTED_CUBE);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (packedOverlay != 53) return; // so it doesn't render outside the fake render

        MultiBufferSource.BufferSource crumbleBuffer = Minecraft.getInstance().renderBuffers().crumblingBufferSource();
        VertexConsumer vertexConsumer = crumbleBuffer.getBuffer(ModRenderTypes.translucentCrumbling(MoreSnifferFlowers.loc("textures/block/red_glow.png")));

        poseStack.pushPose();

        poseStack.translate(0.5,-1,0.5);


        poseStack.scale(2, 2, 2);


       // cube.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
     //   invertedCube.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        crumbleBuffer.endLastBatch();
    }

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity).inflate(3).expandTowards(0, 5, 0);
    }

}
