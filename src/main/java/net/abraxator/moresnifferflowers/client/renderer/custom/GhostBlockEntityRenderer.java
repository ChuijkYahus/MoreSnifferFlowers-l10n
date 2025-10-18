package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GhostBlockEntityRenderer extends GhostRenderer{
    BlockEntity blockEntity;
    protected int packedLight = LightTexture.FULL_BRIGHT;
    protected int packedOverlay = OverlayTexture.NO_OVERLAY;

    public GhostBlockEntityRenderer(BlockPos pos, int ticksRemaining, BlockEntity blockEntity) {
        super(pos, ticksRemaining);
        this.blockEntity = blockEntity;
    }

    @Override
    public void render(float partialTick, Frustum frustum, Camera camera, Level level, PoseStack poseStack, MultiBufferSource.BufferSource buffer) {
        BlockEntityRenderer<BlockEntity> entityRender = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);

        if (entityRender != null)
            entityRender.render(blockEntity, partialTick, poseStack, buffer, packedLight, packedOverlay);
    }

    public GhostBlockEntityRenderer setLight(int packedLight){
        this.packedLight = packedLight;
        return this;
    }

    public GhostBlockEntityRenderer setOverlay(int packedOverlay){
        this.packedOverlay = packedOverlay;
        return this;
    }
}
