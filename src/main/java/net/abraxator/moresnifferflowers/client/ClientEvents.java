package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.config.ModClientConfig;
import net.abraxator.moresnifferflowers.networking.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.PatternspriaModePacket;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onInputMouseScrolling(MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.DYESPRIA.get())) {
            event.setCanceled(true);
            ModPacketHandler.CHANNEL.sendToServer(new DyespriaModePacket((int) event.getScrollDelta()));
        }
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.PATTERNSPRIA.get())) {
            event.setCanceled(true);
            ModPacketHandler.CHANNEL.sendToServer(new PatternspriaModePacket((int) event.getScrollDelta()));
        }
    }

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event){
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            PoseStack poseStack = event.getPoseStack();
            Camera camera = event.getCamera();
            double camX = camera.getPosition().x;
            double camY = camera.getPosition().y;
            double camZ = camera.getPosition().z;
            Matrix4f projectionMatrix = event.getProjectionMatrix();

            BlockPatternRenderer BUFFER_MANAGER = ClientRegistration.getBlockPatternRenderer();
            Minecraft minecraft = Minecraft.getInstance();
            Level level = minecraft.level;
            BlockPatternRenderer.CameraTracker cameraTracker = new BlockPatternRenderer.CameraTracker();
            if (level == null || minecraft.player == null) return;

            if (cameraTracker.hasMoved(camera)) {
                BUFFER_MANAGER.markDirty();
            }
            int chunkRenderDistance = Math.min(Mth.ceil((double) ModClientConfig.getBlockPatternRenderDistance() / 16D) + 1, minecraft.options.getEffectiveRenderDistance());

            poseStack.pushPose();
            poseStack.translate(-camX, -camY, -camZ);
            Matrix4f view = poseStack.last().pose();

            List<LevelChunk> levelChunks = new ArrayList<>();

            ChunkPos playerChunkPos = minecraft.player.chunkPosition();
            for (int x = -chunkRenderDistance; x < chunkRenderDistance ; x++) {
                for (int z = -chunkRenderDistance; z < chunkRenderDistance ; z++) {
                    levelChunks.add(level.getChunk(x + playerChunkPos.x,z + playerChunkPos.z));
                }
            }

            BUFFER_MANAGER.renderPatternOverlay(level, camX, camY, camZ, view, projectionMatrix, levelChunks, event.getFrustum());
            BUFFER_MANAGER.render(poseStack, Minecraft.getInstance().renderBuffers().bufferSource());

            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.getDeltaMovement() != Vec3.ZERO && player.level().getGameTime() % 10 == 0){
            ClientRegistration.getBlockPatternRenderer().markDirty();
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ClientRegistration.getBlockPatternRenderer().markDirty();

    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        ClientRegistration.getBlockPatternRenderer().markDirty();

    }

}
