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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

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


        storage.getPatternPositionsNear(camPos, renderDistance, level).forEach(pos -> {
            BlockPatternCapability.PatternData data = storage.getPattern(pos, level);

            if (data != null && frustum.isVisible(new AABB(pos))) {

                ResourceLocation resourceLocation = MoreSnifferFlowers.loc("block/block_pattern/" + BlockPattern.byIndex(data.patternId()).getSerializedName());
                TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(resourceLocation);
                BlockState state = level.getBlockState(pos);

                for (Direction dir : Direction.values()) {
                    if (state.isFaceSturdy(level, pos, dir) && !level.getBlockState(pos.relative(dir)).isFaceSturdy(level, pos.relative(dir), dir.getOpposite())) {
                        int packedLight = level.getBrightness(LightLayer.BLOCK, pos.relative(dir));
                        int skyLight = level.getBrightness(LightLayer.SKY, pos.relative(dir));

                        packedLight = Math.min(packedLight, 15);
                        skyLight = Math.min(skyLight, 15);

                        int packed = LightTexture.pack(packedLight, skyLight);

                        cachedQuads.add(RenderQuad.create(pos, dir, data.color(), sprite, packed));
                    }
                }
            }
        });
    }


    public record RenderQuad(BlockPos pos, Direction direction, DyeColor color, TextureAtlasSprite sprite, int packedLight) {

        public static RenderQuad create(BlockPos pos, Direction face, DyeColor color, TextureAtlasSprite sprite, int light) {
            return new RenderQuad(pos.immutable(), face, color, sprite, light);
        }

        private void render(PoseStack poseStack, VertexConsumer buffer) {
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            translateToFace(poseStack, direction, pos);

            int rgb = color.getTextColor();
            float r = ((rgb >> 16) & 0xFF) / 255f;
            float g = ((rgb >> 8) & 0xFF) / 255f;
            float b = (rgb & 0xFF) / 255f;

            Matrix4f pose = poseStack.last().pose();
            Matrix3f normal = poseStack.last().normal();
            Vec3i n = direction.getNormal();
            float nx = n.getX(), ny = n.getY(), nz = n.getZ();


            /*  if (direction == Direction.UP) {
                   r = 1F;
                   g = 1F;
                   b = 1F;
               }*/

            if (direction == Direction.UP || direction == Direction.DOWN) {
                buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV0()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV0()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV1()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV1()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
            } else {
                buffer.vertex(pose, 0, 0, 0).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV0()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 0).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV0()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 1, 0, 1).color(r, g, b, 1f).uv(sprite.getU1(), sprite.getV1()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
                buffer.vertex(pose, 0, 0, 1).color(r, g, b, 1f).uv(sprite.getU0(), sprite.getV1()).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
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
