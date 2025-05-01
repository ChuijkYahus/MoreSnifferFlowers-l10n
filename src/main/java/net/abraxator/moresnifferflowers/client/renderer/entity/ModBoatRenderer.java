package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.renderer.renderstate.ModBoatRenderState;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusChestBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ModBoatRenderer extends EntityRenderer<AbstractBoat, ModBoatRenderState> {
    private final Map<ModBoatEntity.Type, Pair<ResourceLocation, BoatModel>> boatResources;
    private final Model waterPatchModel;
    private final ResourceLocation texture;
    private final BoatModel model;


    public ModBoatRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation location, boolean pChestBoat) {
        super(pContext);
        this.boatResources = Stream.of(ModBoatEntity.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type,
               type -> Pair.of(ResourceLocation.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, getTextureLocation(type, pChestBoat)), this.createBoatModel(pContext, type, pChestBoat))));
        this.texture = location.model().withPath(p_375447_ -> "textures/entity/" + p_375447_ + ".png");
        this.waterPatchModel = new Model.Simple(pContext.bakeLayer(ModelLayers.BOAT_WATER_PATCH), p_359275_ -> RenderType.waterMask());
        this.model = new BoatModel(pContext.bakeLayer(location));

    }

    @Override
    public void render(ModBoatRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.375F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        float f = state.hurtTime;
        if (f > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * state.damageTime / 10.0F * (float)state.hurtDir));
        }

        if (!Mth.equal(state.bubbleAngle, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(state.bubbleAngle * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        BoatModel entitymodel = this.model;
        entitymodel.setupAnim(state);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.texture));
        entitymodel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, state.color);
        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
    }

    @Override
    public ModBoatRenderState createRenderState() {
        return new ModBoatRenderState();
    }
    
    private static String getTextureLocation(ModBoatEntity.Type pType, boolean pChestBoat) {
        return pChestBoat ? "textures/entity/chest_boat/" + pType.getName() + ".png" : "textures/entity/boat/" + pType.getName() + ".png";
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context pContext, ModBoatEntity.Type pType, boolean pChestBoat) {
        ModelLayerLocation modellayerlocation = pChestBoat ? ModBoatRenderer.createChestBoatModelName(pType) : ModBoatRenderer.createBoatModelName(pType);
        ModelPart modelpart = pContext.bakeLayer(modellayerlocation);
        return new BoatModel(modelpart);
    }

    public static ModelLayerLocation createBoatModelName(ModBoatEntity.Type pType) {
        return createLocation("boat/" + pType.getName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(ModBoatEntity.Type pType) {
        return createLocation("chest_boat/" + pType.getName(), "main");
    }

    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, pPath), pModel);
    }

    @Override
    public void extractRenderState(AbstractBoat entity, ModBoatRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = entity.getYRot(partialTick);
        state.hurtTime = (float)entity.getHurtTime() - partialTick;
        state.hurtDir = entity.getHurtDir();
        state.damageTime = Math.max(entity.getDamage() - partialTick, 0.0F);
        state.bubbleAngle = entity.getBubbleAngle(partialTick);
        state.isUnderWater = entity.isUnderWater();
        state.rowingTimeLeft = entity.getRowingTime(0, partialTick);
        state.rowingTimeRight = entity.getRowingTime(1, partialTick);
        if (entity instanceof VivicusBoatEntity vivicusBoatEntity){
         state.color = vivicusBoatEntity.colorValues().get(vivicusBoatEntity.getColor());
        } else if (entity instanceof VivicusChestBoatEntity vivicusChestBoatEntity){
            state.color = vivicusChestBoatEntity.colorValues().get(vivicusChestBoatEntity.getColor());
        }
    }
}
