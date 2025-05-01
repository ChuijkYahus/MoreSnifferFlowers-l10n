package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.CorruptedProjectileModel;
import net.abraxator.moresnifferflowers.client.renderer.renderstate.ProjectileRenderState;
import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CorruptedProjectileRenderer extends EntityRenderer<CorruptedProjectile, ProjectileRenderState> {
    public static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/entity/corrupted_projectile.png");
    private final CorruptedProjectileModel model;
    
    public CorruptedProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CorruptedProjectileModel(pContext.bakeLayer(ModModelLayerLocations.CORRUPTED_PROJECTILE));
    }

    @Override
    public ProjectileRenderState createRenderState() {
        return new ProjectileRenderState();
    }

    @Override
    public void render(ProjectileRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        poseStack.translate(0, -0.5, 0);
        poseStack.scale(0.6F, 0.6F, 0.6F);
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(renderState))),
                packedLight,
                OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    public ResourceLocation getTextureLocation(ProjectileRenderState state) {
        return TEXTURE;
    }
}
