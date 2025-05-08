package net.abraxator.moresnifferflowers.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.capability.PatternDyeStorage;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.PatternDyeRenderHandler;
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

       Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;

        if (level == null || mc.player == null) return;

        PatternDyeStorage storage = ClientRegistration.getClientPatternStorage();

        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();

        int renderDistance = 16;

        // For demo: iterate visible chunks near the player (limited area)
        BlockPos playerPos = mc.player.blockPosition();
        BlockPos.betweenClosedStream(playerPos.offset(-renderDistance, -5, -renderDistance), playerPos.offset(renderDistance, 5, renderDistance)).forEach(pos -> {
            double dx = pos.getX() - camX;
            double dy = pos.getY() - camY;
            double dz = pos.getZ() - camZ;

            poseStack.translate(dx, dy, dz);
            BlockState state = level.getBlockState(pos);
            if (storage.hasPattern(pos)) {
                PatternDyeRenderHandler.renderPatternOverlay(dispatcher, state, pos, level, poseStack,
                        mc.renderBuffers().bufferSource(), level.getRandom());
            }
            poseStack.translate(-dx, -dy, -dz);

        });
    }
}
