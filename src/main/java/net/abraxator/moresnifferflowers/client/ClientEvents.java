package net.abraxator.moresnifferflowers.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.networking.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onInputMouseScrolling(MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.DYESPRIA.get())) {
            event.setCanceled(true);
            ModPacketHandler.CHANNEL.sendToServer(new DyespriaModePacket((int) event.getScrollDelta()));
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
            if (level == null || minecraft.player == null) return;
            BlockPatternRenderer.CameraTracker cameraTracker = new BlockPatternRenderer.CameraTracker();
            if (cameraTracker.hasMoved(camera)) {
                BUFFER_MANAGER.markDirty();
            }

            poseStack.pushPose();
            poseStack.translate(-camX, -camY, -camZ);

            Matrix4f view = poseStack.last().pose();

            BUFFER_MANAGER.renderPatternOverlay(level, camX, camY, camZ, view, projectionMatrix, CapabilityList.getBlockPatternCapability(), event.getFrustum());
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
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockPatternCapability storage = CapabilityList.getBlockPatternCapability();

        ClientRegistration.getBlockPatternRenderer().markDirty();

    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockPatternCapability storage = CapabilityList.getBlockPatternCapability();

        ClientRegistration.getBlockPatternRenderer().markDirty();

    }

    @SubscribeEvent
    public static void onCameraMove(ViewportEvent event){

    }

}
