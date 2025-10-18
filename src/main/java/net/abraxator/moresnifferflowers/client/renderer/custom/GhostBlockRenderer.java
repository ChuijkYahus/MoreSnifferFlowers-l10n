package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.client.VertexConsumerWrapper;

public class GhostBlockRenderer extends GhostRenderer{
    protected BlockState state;
    protected RenderType renderType = RenderType.translucent();
    protected boolean checkSides = true;

    public GhostBlockRenderer(BlockPos pos, int ticksRemaining, BlockState state){
        super(pos, ticksRemaining);
        this.state = state;
    }

    @Override
    public void render(float partialTick, Frustum frustum, Camera camera, Level level, PoseStack poseStack, MultiBufferSource.BufferSource buffer) {
        Minecraft minecraft = Minecraft.getInstance();
        GhostRenderer ghostRenderer = this;

        VertexConsumerWrapper tintedConsumer = new VertexConsumerWrapper(buffer.getBuffer(renderType)) {
            @Override
            public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] colorMuls, float red, float green, float blue, int[] combinedLights, int combinedOverlay, boolean mulColor) {
                super.putBulkData(pose, quad, colorMuls, red, green, blue, combinedLights, combinedOverlay, mulColor);
            }

            @Override
            public VertexConsumer color(float red, float green, float blue, float alpha) {
                return super.color(ghostRenderer.red, ghostRenderer.green, ghostRenderer.blue, ghostRenderer.alpha);
            }
        };

        minecraft.getBlockRenderer().renderBatched(state, pos, level, poseStack, tintedConsumer, checkSides, minecraft.level.getRandom());
    }

    public GhostBlockRenderer setCheckSides(boolean checkSides) {
        this.checkSides = checkSides;
        return this;
    }

    public GhostBlockRenderer setRenderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }
}
