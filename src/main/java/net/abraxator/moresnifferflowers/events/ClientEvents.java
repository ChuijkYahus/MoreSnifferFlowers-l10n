package net.abraxator.moresnifferflowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.client.renderer.custom.MultiblockPreviewRenderer;
import net.abraxator.moresnifferflowers.entities.GluingGumEntity;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toServer.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.toServer.PatternspriaModePacket;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
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
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.PATTERNSPRIA.get())) {
            event.setCanceled(true);
            ModPacketHandler.CHANNEL.sendToServer(new PatternspriaModePacket((int) event.getScrollDelta()));
        }
    }

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event){
        RenderLevelStageEvent.Stage stage = event.getStage();
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        Matrix4f projectionMatrix = event.getProjectionMatrix();
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        Frustum frustum = event.getFrustum();


        if (stage.equals(RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)) {
            BlockPatternRenderer.cacheAndRender(frustum, camera, level, minecraft, poseStack);
        }

        if (stage.equals(RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)){
           MultiblockPreviewRenderer.renderMultiblockPreviews(event.getPartialTick(), minecraft, level, camera, poseStack);
        }


    }

    @SubscribeEvent
    public static void renderLiving(RenderLivingEvent.Post<?, ?> event) {
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
    public static void renderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        // ChatGPT code that worked first time???
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(ModEffects.GLUED.get())) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        ResourceLocation texture = MoreSnifferFlowers.loc("textures/gui/glued_overlay.png");
        RenderSystem.setShaderTexture(0, texture);

        guiGraphics.blit(texture, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PoseStack pose = event.getPoseStack();

        if (player.hasEffect(ModEffects.SLIPPERY.get())){
            player.getCapability(CapabilityList.SLIPPERY).ifPresent(cap -> {
                if (cap.isFallen){

                    pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    pose.translate(0.0D, -0.5D, 0.0D);

                }
            });
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
