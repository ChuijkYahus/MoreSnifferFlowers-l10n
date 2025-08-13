package net.abraxator.moresnifferflowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.blocks.ModCropBlock;
import net.abraxator.moresnifferflowers.blocks.MultiBlock;
import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.abraxator.moresnifferflowers.capability.SlipperyCapability;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.components.PreviewState;
import net.abraxator.moresnifferflowers.entities.GluingGumEntity;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.config.ModClientConfig;
import net.abraxator.moresnifferflowers.networking.toServer.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.toServer.PatternspriaModePacket;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onInputMouseScrolling(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.DYESPRIA.get())) {
            event.setCanceled(true);
            PacketDistributor.sendToServer(new DyespriaModePacket((int) event.getScrollDeltaY()));
        }
        if(player.isCrouching() && player.getMainHandItem().is(ModItems.PATTERNSPRIA.get())) {
            event.setCanceled(true);
            PacketDistributor.sendToServer(new PatternspriaModePacket((int) event.getScrollDeltaY()));
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


        if (stage.equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            renderBlockPatterns(frustum, camera, level, minecraft, poseStack, projectionMatrix);
        }

        if (stage.equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)){
            LocalPlayer player = minecraft.player;
            if (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof MultiBlock multiBlock && blockItem.getBlock() instanceof EntityBlock block) {
               HitResult hitResult = minecraft.hitResult;

               if (hitResult instanceof BlockHitResult blockHitResult){
                   boolean placeOnWater = false;

                   if (blockItem instanceof PlaceOnWaterBlockItem) {
                       blockHitResult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
                       placeOnWater = level.isWaterAt(blockHitResult.getBlockPos());
                   };

                   Direction hitDirection = blockHitResult.getDirection();
                   BlockPos hitPos = blockHitResult.getBlockPos();
                   BlockPos pos =  hitPos.relative(hitDirection);

                   BlockState state = blockItem.getBlock().defaultBlockState()
                           .trySetValue(HorizontalDirectionalBlock.FACING, player.getDirection())
                           .trySetValue(ModStateProperties.CENTER, true);
                   if (blockItem.getBlock() instanceof ModCropBlock cropBlock)
                       state = state.trySetValue(cropBlock.getAgeProperty(), cropBlock.getMaxAge());

                   BlockEntity entity = block.newBlockEntity(pos, state);

                   if (entity instanceof MultiBlockEntity multiBlockEntity && level.getBlockState(pos).canBeReplaced() && (!level.getBlockState(hitPos).isAir() || placeOnWater)) {

                       boolean canPlace = multiBlock.canPlace(level, pos, state);

                       double camX = camera.getPosition().x;
                       double camY = camera.getPosition().y;
                       double camZ = camera.getPosition().z;

                       multiBlockEntity.previewState = canPlace ? PreviewState.PREVIEW : PreviewState.INVALID;
                       if (level.getBlockState(hitPos).canBeReplaced() && !placeOnWater) pos = pos.relative(hitDirection.getOpposite());

                       poseStack.pushPose();

                       poseStack.translate(pos.getX() - camX,pos.getY() - camY,pos.getZ() - camZ);

                       MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
                       BlockEntityRenderer<BlockEntity> entityRender = minecraft.getBlockEntityRenderDispatcher().getRenderer(entity);

                       if (entityRender != null)
                           entityRender.render(entity, event.getPartialTick().getRealtimeDeltaTicks(), poseStack, buffer, 0xFFFFFF, OverlayTexture.NO_OVERLAY);

                       poseStack.popPose();

                   }
               }
            }
        }


    }

    private static void renderBlockPatterns(Frustum frustum, Camera camera, Level level, Minecraft minecraft, PoseStack poseStack, Matrix4f projectionMatrix) {
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        BlockPatternRenderer BUFFER_MANAGER = ClientRegistration.getBlockPatternRenderer();
        BlockPatternRenderer.CameraTracker cameraTracker = new BlockPatternRenderer.CameraTracker();
        if (level == null || minecraft.player == null) return;

        if (cameraTracker.hasMoved(camera)) {
            BUFFER_MANAGER.markDirty();
        }
        int chunkRenderDistance = Math.min(ModClientConfig.getBlockPatternRenderDistance(), minecraft.options.getEffectiveRenderDistance());

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

        BUFFER_MANAGER.cachePatterns(level, camX, camY, camZ, view, projectionMatrix, levelChunks, frustum);
        BUFFER_MANAGER.render(poseStack, Minecraft.getInstance().renderBuffers().bufferSource());

        poseStack.popPose();
    }

    @SubscribeEvent
    public static void renderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();

        GluedCapability cap = entity.getData(ModDataAttachments.GLUED);
        if (cap.isGlued) {
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
            minecraft.getEntityRenderDispatcher().render(gum, 0, yOff, 0, entity.yBodyRot, event.getPartialTick(), poseStack, event.getMultiBufferSource(), event.getPackedLight());
            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void renderGuiOverlay(RenderGuiLayerEvent.Pre event) {
        // ChatGPT code that worked first time???
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.hasEffect(ModEffects.GLUED)) return;

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

        if (player.hasEffect(ModEffects.SLIPPERY)){
            SlipperyCapability cap = player.getData(ModDataAttachments.SLIPPERY);

            if (cap.isFallen){
                pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
                pose.translate(0.0D, -0.5D, 0.0D);
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        Player player = event.getEntity();
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
