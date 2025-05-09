package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.PatternDyeStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PatternDyeRenderHandler {
    public static void renderPatternOverlay(BlockRenderDispatcher dispatcher, BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, MultiBufferSource.BufferSource buffer, RandomSource random) {
        PatternDyeStorage storage = ClientRegistration.getClientPatternStorage();
        PatternDyeStorage.PatternData data = storage.getPattern(pos);
        if (data == null) return;
        BakedModel model = dispatcher.getBlockModel(state);

        ResourceLocation resourceLocation = MoreSnifferFlowers.loc("block/dye_pattern/" + data.patternId());
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(resourceLocation);
        RenderType renderType = RenderType.cutoutMipped();

        VertexConsumer consumer = buffer.getBuffer(renderType);

        ModelData modelData = model.getModelData(level, pos, state, ModelData.EMPTY);
        float scale = 0.001f;


        int overlay = OverlayTexture.NO_OVERLAY;

        for (Direction dir : Direction.values()) {
            if (state.isFaceSturdy(level, pos, dir) && !level.getBlockState(pos.relative(dir)).isFaceSturdy(level, pos.relative(dir), dir.getOpposite())) {
                int packedLight = LevelRenderer.getLightColor(level, state, pos.offset(dir.getNormal()));
                poseStack.pushPose();
                translateToFace(poseStack, dir, pos);
                poseStack.scale(1+scale, 1+scale, 1+scale);
                drawPatternQuad(poseStack, consumer, data.color(), packedLight, overlay, dir, sprite);
                poseStack.popPose();
            }
        }
    }

    private static void drawPatternQuad(PoseStack poseStack, VertexConsumer buffer, DyeColor color, int packedLight, int overlay, Direction direction, TextureAtlasSprite sprite) {
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        int rgb = color.getTextColor();
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8) & 0xFF) / 255f;
        float b = (rgb & 0xFF) / 255f;

        Vec3i n = direction.getNormal();


       // System.out.println(n + direction.toString() + direction.getRotation().getEulerAnglesXYZ(new Vector3f()));

        float nx = n.getX(), ny = n.getY(), nz = n.getZ();

        if (direction == Direction.UP || direction == Direction.DOWN) {
            buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

        } else {
            buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        }
    }

    private static void translateToFace(PoseStack stack, Direction face, BlockPos pos) {
        float distance = 0.002f * (Math.abs((pos.getX() + pos.getY() + pos.getZ()) % 4) + 1);
        float scale = distance*2;
        switch (face) {
            case UP -> {
              //  stack.mulPose(Axis.XP.rotationDegrees(90));
              //  stack.translate(0, 0, -1 - distance);
                stack.translate(0, 1 + distance, 0);
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance, 0, -distance);
            }
            case DOWN -> {
                stack.translate(0, 0 - distance, 1);
                stack.mulPose(Axis.XP.rotationDegrees(180));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance, 0, -distance);

            }
            case NORTH -> {
                stack.translate(0, 1, -0 - distance);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
            case SOUTH -> {
                stack.translate(0, 0, 1 + distance);
                stack.mulPose(Axis.XN.rotationDegrees(90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);
            }
            case WEST -> {
                stack.translate(0 - distance, 1, 1);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.mulPose(Axis.ZP.rotationDegrees(-90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
            case EAST -> {
                stack.translate(1 + distance, 1, 0);
                stack.mulPose(Axis.XP.rotationDegrees(90));
                stack.mulPose(Axis.ZP.rotationDegrees(90));
                stack.scale(1+scale, 1, 1+scale);
                stack.translate(-distance,0, -distance);

            }
        }
    }
}
