package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class GhostModelRenderer extends GhostRenderer{
    protected final ModelPart MODEL_PART;
    protected final Material MATERIAL;
    protected Function<ResourceLocation, RenderType> renderTypeFunction = RenderType::entityTranslucentCull;
    protected int packedLight = LightTexture.FULL_BRIGHT;
    protected int packedOverlay = OverlayTexture.NO_OVERLAY;

    public GhostModelRenderer(BlockPos pos, int ticksRemaining, ModelPart modelPart, Material material) {
        super(pos, ticksRemaining);
        this.MODEL_PART = modelPart;
        this.MATERIAL = material;
    }

    @Override
    public void render(float partialTick, Frustum frustum, Camera camera, Level level, PoseStack poseStack, MultiBufferSource.BufferSource buffer) {
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
        MODEL_PART.render(poseStack, MATERIAL.buffer(buffer, renderTypeFunction), packedLight, packedOverlay, FastColor.ARGB32.colorFromFloat(alpha, red, green, blue));
    }

    public GhostModelRenderer setRenderType(Function<ResourceLocation, RenderType> renderTypeFunction){
        this.renderTypeFunction = renderTypeFunction;
        return this;
    }

    public GhostModelRenderer setLight(int packedLight){
        this.packedLight = packedLight;
        return this;
    }

    public GhostModelRenderer setOverlay(int packedOverlay){
        this.packedOverlay = packedOverlay;
        return this;
    }
}
