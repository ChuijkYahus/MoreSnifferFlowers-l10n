package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.PatternDyeStorage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PatternDyeRenderHandler {
    public static void renderPatternOverlay(BlockRenderDispatcher dispatcher, BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, MultiBufferSource.BufferSource buffer, RandomSource random) {
        PatternDyeStorage storage = ClientRegistration.getClientPatternStorage();
        PatternDyeStorage.PatternData data = storage.getPattern(pos);
        if (data == null) return;

        // Choose texture based on pattern ID
        ResourceLocation texture = MoreSnifferFlowers.loc("textures/dye_pattern/" + data.patternId() + ".png");
        RenderType renderType = RenderType.entityTranslucent(texture);
        VertexConsumer consumer = buffer.getBuffer(renderType);

        poseStack.pushPose();
        // Render a simple quad on the top of the block
        // Add your own transform/quad drawing here (e.g. RenderHelper.renderFaceQuad)
        poseStack.translate(0, 1.01, 0); // Slightly above top face
        renderQuad(poseStack, consumer, data.color());
        poseStack.popPose();
    }

    private static void renderQuad(PoseStack stack, VertexConsumer buffer, DyeColor color) {
        Matrix4f pose = stack.last().pose();
        Matrix3f normalMatrix = stack.last().normal();

        int rgb = color.getTextColor();
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        int light = 0xF000F0; // full brightness (can be passed in)
        int overlay = OverlayTexture.NO_OVERLAY; // no overlay texture
        Vector3f normal = new Vector3f(0, 1, 0); // facing up (for top face)

        buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1.0f).uv(0, 0)
                .overlayCoords(overlay).uv2(light).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();

        buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1.0f).uv(1, 0)
                .overlayCoords(overlay).uv2(light).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();

        buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1.0f).uv(1, 1)
                .overlayCoords(overlay).uv2(light).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();

        buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1.0f).uv(0, 1)
                .overlayCoords(overlay).uv2(light).normal(normalMatrix, normal.x(), normal.y(), normal.z()).endVertex();
    }
}
