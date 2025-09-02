package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.blockentities.CropressedCropBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.FakeRenderBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FakeBlockEntityRenderer {
    public static void render(DeltaTracker partialTick, Minecraft minecraft, Level level, Camera camera, PoseStack poseStack){
        Minecraft.getInstance().levelRenderer.iterateVisibleBlockEntities(entity -> {
            if (entity instanceof FakeRenderBlockEntity fakeRenderBlockEntity){
                fakeRenderBlockEntity.setFake();

                BlockPos pos = entity.getBlockPos();
                poseStack.pushPose();

                double camX = camera.getPosition().x;
                double camY = camera.getPosition().y;
                double camZ = camera.getPosition().z;
                poseStack.translate(pos.getX() - camX,pos.getY() - camY,pos.getZ() - camZ);

                MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
                BlockEntityRenderer<BlockEntity> entityRender = minecraft.getBlockEntityRenderDispatcher().getRenderer(entity);

                if (entityRender != null) entityRender.render(entity, partialTick.getRealtimeDeltaTicks(), poseStack, buffer, 0xFFFFFF, 53);

                poseStack.popPose();
            };
        });
    }

}
