package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.FakeRenderBlockEntity;
import net.abraxator.moresnifferflowers.capability.FakeRenderingCapability;
import net.abraxator.moresnifferflowers.client.ModRenderTypes;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;

import java.util.HashSet;
import java.util.Set;

public class FakeBlockRenderer {
    public static void render(Minecraft minecraft, Level level, Camera camera, PoseStack poseStack){
        BlockPatternRenderer.getVisibleChunks().forEach(chunk -> {

            Set<BlockPos> blockPosSet = FakeRenderingCapability.getCopy(chunk);

            blockPosSet.forEach(pos -> {

                BlockState state = level.getBlockState(pos);

                if (!state.is(ModTags.ModBlockTags.FAKE_RENDER)){
/*
                    blockPosSet.remove(pos);
                    FakeRenderingCapability.set(chunk, blockPosSet);

                    MoreSnifferFlowers.LOGGER.error("Invalid fake renderer pos = {}", pos);
*/
                    return;
                }
                MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();

                poseStack.pushPose();

                double camX = camera.getPosition().x;
                double camY = camera.getPosition().y;
                double camZ = camera.getPosition().z;
                poseStack.translate(pos.getX() - camX,pos.getY() - camY,pos.getZ() - camZ);

                minecraft.getBlockRenderer().renderBatched(state, pos, level, poseStack, buffer.getBuffer(ModRenderTypes.CRUMBLING_BLOCK), true, minecraft.level.getRandom(), ModelData.EMPTY, ModRenderTypes.CRUMBLING_BLOCK);

                buffer.endLastBatch();
                poseStack.popPose();

            });
        });
    }
}
