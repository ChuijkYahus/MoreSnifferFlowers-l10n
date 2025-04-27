package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.DragonflyModel;
import net.abraxator.moresnifferflowers.client.renderstate.ProjectileRenderState;
import net.abraxator.moresnifferflowers.entities.DragonflyProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DragonflyRenderer extends EntityRenderer<DragonflyProjectile, ProjectileRenderState> {
    public static final ResourceLocation TEXTURE = MoreSnifferFlowers.loc("textures/entity/dragonfly.png");
    private final DragonflyModel model;

    public DragonflyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new DragonflyModel(pContext.bakeLayer(ModModelLayerLocations.DRAGONFLY));
    }

    @Override
    public ProjectileRenderState createRenderState() {
        return null;
    }

    @Override
    public void render(ProjectileRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        poseStack.translate(0, -1, 0.5);
        this.model.renderToBuffer(
                poseStack,
                bufferSource.getBuffer(this.model.renderType(this.getTextureLocation())),
                packedLight,
                OverlayTexture.NO_OVERLAY);
        model.animate(renderState.partialTick);
        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    public ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
