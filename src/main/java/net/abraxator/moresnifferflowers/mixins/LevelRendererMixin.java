package net.abraxator.moresnifferflowers.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.capability.PatternDyeStorage;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.PatternDyeRenderHandler;
import net.abraxator.moresnifferflowers.init.cofig.ModClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderChunkLayer", at = @At("TAIL")) // after the whole chunk layer is rendered)
    private void afterRenderChunkLayer(RenderType renderType, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix, CallbackInfo ci) {

       Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level == null || minecraft.player == null) return;

        PatternDyeStorage storage = ClientRegistration.getClientPatternStorage();

        BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();

        int renderDistancePlayer = minecraft.options.getEffectiveRenderDistance();
        int configuredRenderDistance = ModClientConfig.DYE_PATTERN_RENDER_DISTANCE.get();
        int renderDistance = configuredRenderDistance < 0
                ? renderDistancePlayer*16 / Math.abs(configuredRenderDistance)
                : configuredRenderDistance;

        BlockPos playerPos = minecraft.player.blockPosition();

        if (renderType == RenderType.translucent()) {

            storage.getPatternPositions().forEach(pos -> {
                if (pos.closerThan(playerPos, renderDistance)) {
                    double dx = pos.getX() - camX;
                    double dy = pos.getY() - camY;
                    double dz = pos.getZ() - camZ;

                    poseStack.translate(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (storage.hasPattern(pos)) {
                        PatternDyeRenderHandler.renderPatternOverlay(dispatcher, state, pos, level, poseStack,
                                minecraft.renderBuffers().bufferSource(), level.getRandom());
                    }
                    poseStack.translate(-dx, -dy, -dz);

                }
            });
        }


    }
}
