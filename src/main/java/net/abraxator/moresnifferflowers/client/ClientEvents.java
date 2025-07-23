package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.entities.GluingGumEntity;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.config.ModClientConfig;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toServer.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.toServer.PatternspriaModePacket;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
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
    public static void renderGumModel(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();

        entity.getCapability(CapabilityList.GLUED).ifPresent(cap -> {
            if (!cap.isGlued) return;
            Vec3 pos = entity.position();
            Minecraft minecraft = Minecraft.getInstance();
            PoseStack poseStack = event.getPoseStack();

            GluingGumEntity gum = new GluingGumEntity(entity.level());
            gum.setPos(pos.x, pos.y, pos.z);

            poseStack.pushPose();
            float yOff = 0;
            if (entity instanceof Player player && player.isCrouching()) {
                yOff += 0.13f;
            }
            minecraft.getEntityRenderDispatcher().render(gum, 0, yOff ,0, entity.yBodyRot, event.getPartialTick(), poseStack, event.getMultiBufferSource(), event.getPackedLight());
            poseStack.popPose();
        });

    }

    @SubscribeEvent
    public static void renderGumOverlay(RenderGuiOverlayEvent.Pre event) {
        // ChatGPT code that worked first time???
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(ModEffects.GLUED.get())) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Your custom overlay texture
        ResourceLocation texture = MoreSnifferFlowers.loc("textures/gui/glued_overlay.png");
        RenderSystem.setShaderTexture(0, texture);

        guiGraphics.blit(texture, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
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
