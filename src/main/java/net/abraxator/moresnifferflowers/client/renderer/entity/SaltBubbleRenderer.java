package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.SaltBubbleModel;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SaltBubbleRenderer extends EntityRenderer<SaltBubbleProjectile> {
    private final SaltBubbleModel model;
    public static final ResourceLocation TEXTURE_SALT = MoreSnifferFlowers.loc("textures/entity/salt_bubble.png");
    public static final ResourceLocation TEXTURE_SOUR = MoreSnifferFlowers.loc("textures/entity/sour_bubble.png");


    public SaltBubbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SaltBubbleModel(context.bakeLayer(ModModelLayerLocations.SALT_BUBBLE));
    }

    @Override
    public void render(SaltBubbleProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        if(pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25)) {
            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 180F));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
            pPoseStack.translate(0, -0.5, 0);
            pPoseStack.scale(0.6F, 0.6F, 0.6F);
            this.model.renderToBuffer(
                    pPoseStack,
                    pBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(pEntity))),
                    pPackedLight,
                    OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBufferSource, pPackedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SaltBubbleProjectile entity) {
        return entity.isCorrupted() ? TEXTURE_SOUR : TEXTURE_SALT;
    }
}
