package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.init.config.ModClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.lighting.QuadLighter;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BlockPatternRenderer {

    private VertexBuffer vertexBuffer;
    private boolean dirty = true;
    private final List<RenderQuad> cachedQuads = new ArrayList<>();

    public void markDirty() {
        this.dirty = true;
    }

    public void renderPatternOverlay(Level level, double camX, double camY, double camZ, Matrix4f viewMatrix, Matrix4f projectionMatrix, BlockPatternCapability storage, Frustum frustum) {
        if (!dirty) return;
        dirty = false;
        cachedQuads.clear();

        BlockPos camPos = BlockPos.containing(camX, camY, camZ);
        frustum.prepare(camX, camY, camZ);

        Minecraft minecraft = Minecraft.getInstance();
        int renderDistancePlayer = minecraft.options.getEffectiveRenderDistance();
        int configuredRenderDistance = ModClientConfig.DYE_PATTERN_RENDER_DISTANCE.get();
        int renderDistance = configuredRenderDistance < 0 ? renderDistancePlayer*16 / Math.abs(configuredRenderDistance) : configuredRenderDistance;

        Stream<BlockPos> patternPositionsNear = storage.getPatternPositionsNear(camPos, renderDistance, level);
        if (patternPositionsNear == null) return;
        List<BlockPos> positions = patternPositionsNear.toList();

        for (BlockPos pos : positions) {
            BlockPatternCapability.PatternData data = storage.getPattern(pos, level);

            if (data != null && frustum.isVisible(new AABB(pos))) {

                ResourceLocation resourceLocation = MoreSnifferFlowers.loc("block/block_pattern/" + BlockPattern.fromId(data.patternId()).getSerializedName());
                TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(resourceLocation);
                BlockState state = level.getBlockState(pos);

                for (Direction dir : Direction.values()) {
                    if (state.isFaceSturdy(level, pos, dir) && (!level.getBlockState(pos.relative(dir)).isFaceSturdy(level, pos.relative(dir), dir.getOpposite()) || !level.getBlockState(pos.relative(dir)).canOcclude() )) {

                        int packed = getPackedLight(level, pos.relative(dir));
                        if (data.isGlowing()) packed = LightTexture.FULL_BRIGHT;

                        cachedQuads.add(RenderQuad.create(pos, dir, data.color(), sprite, packed, data.direction(), data.isGlowing()));
                    }
                }
            }
        }
    }


    public record RenderQuad(BlockPos pos, Direction direction, int color, TextureAtlasSprite sprite, int packedLight, Direction rotation, boolean isGlowing) {

        public static RenderQuad create(BlockPos pos, Direction face, int color, TextureAtlasSprite sprite, int light, Direction rotation, boolean isGlowing) {
            return new RenderQuad(pos.immutable(), face, color, sprite, light, rotation, isGlowing);
        }

        private void render(PoseStack poseStack, VertexConsumer buffer) {
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            translateToFace(poseStack, direction, pos);

            Vec3i n = direction.getNormal();
            float nx = n.getX(), ny = n.getY(), nz = n.getZ();

            float ao = QuadLighter.calculateShade(nx, ny, nz, false);

            int rgb = color;
            float r = ((rgb >> 16) & 0xFF) / 255f;
            float g = ((rgb >> 8) & 0xFF) / 255f;
            float b = (rgb & 0xFF) / 255f;

            if (!isGlowing) {
                r *= ao;
                g *= ao;
                b *= ao;
            }


            Matrix4f pose = poseStack.last().pose();
            Matrix3f normal = poseStack.last().normal();

            /*  if (direction == Direction.UP) {
                   r = 1F;
                   g = 1F;
                   b = 1F;
               }*/

            float u0 = sprite.getU0();
            float u1 = sprite.getU1();
            float u2 = sprite.getU1();
            float u3 = sprite.getU0();

            float v0 = sprite.getV1();
            float v1 = sprite.getV1();
            float v2 = sprite.getV0();
            float v3 = sprite.getV0();

            if (rotation == Direction.EAST) {
                u0 = sprite.getU0();
                u1 = sprite.getU0();
                u2 = sprite.getU1();
                u3 = sprite.getU1();
                v0 = sprite.getV1();
                v1 = sprite.getV0();
                v2 = sprite.getV0();
                v3 = sprite.getV1();
            }
            if (rotation == Direction.SOUTH) {
                u0 = sprite.getU1();
                u1 = sprite.getU0();
                u2 = sprite.getU0();
                u3 = sprite.getU1();
                v0 = sprite.getV0();
                v1 = sprite.getV0();
                v2 = sprite.getV1();
                v3 = sprite.getV1();
            }
            if (rotation == Direction.WEST) {
                u0 = sprite.getU1();
                u1 = sprite.getU1();
                u2 = sprite.getU0();
                u3 = sprite.getU0();
                v0 = sprite.getV0();
                v1 = sprite.getV1();
                v2 = sprite.getV1();
                v3 = sprite.getV0();
            }

            if (direction == Direction.UP || direction == Direction.DOWN) {
                buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(u0, v0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(u1, v1).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(u2, v2).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(u3, v3).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

            } else if (direction == Direction.SOUTH){
                buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(u0, v0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(u1, v1).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(u2, v2).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(u3, v3).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();

            } else {
                buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(u0, v3).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(u1, v2).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(u2, v1).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(u3, v0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            }

            poseStack.popPose();
        }
    }

    public void render(PoseStack stack, MultiBufferSource bufferSource) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutoutMipped());

        for (RenderQuad quad : cachedQuads) {
            quad.render(stack, buffer);
        }
    }

    private static void translateToFace(PoseStack stack, Direction face, BlockPos pos) {
        double configOffset = ModClientConfig.DYE_PATTERN_OFFSET.get();
        float distance = (float) (configOffset * (Math.abs((pos.getX() + pos.getY() + pos.getZ()) % 4) + 1));
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

    private static int getPackedLight(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    public static class CameraTracker {
        private Vec3 lastPosition = Vec3.ZERO;
        private float lastYaw = 0f;
        private float lastPitch = 0f;

        private static final double MOVE_THRESHOLD = 1.5; // blocks
        private static final float ROTATE_THRESHOLD = 5f; // degrees

        public boolean hasMoved(Camera camera) {
            Vec3 currentPos = camera.getPosition();
            float yaw = camera.getYRot();
            float pitch = camera.getXRot();

            double dx = currentPos.x - lastPosition.x;
            double dy = currentPos.y - lastPosition.y;
            double dz = currentPos.z - lastPosition.z;

            double distSq = dx * dx + dy * dy + dz * dz;
            float deltaYaw = Math.abs(yaw - lastYaw);
            float deltaPitch = Math.abs(pitch - lastPitch);

            return distSq > MOVE_THRESHOLD * MOVE_THRESHOLD ||
                    deltaYaw > ROTATE_THRESHOLD ||
                    deltaPitch > ROTATE_THRESHOLD;
        }

        public void update(Camera camera) {
            this.lastPosition = camera.getPosition();
            this.lastYaw = camera.getYRot();
            this.lastPitch = camera.getXRot();
        }
    }

}
