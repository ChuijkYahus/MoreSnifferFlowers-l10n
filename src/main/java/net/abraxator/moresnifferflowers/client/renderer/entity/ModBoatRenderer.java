package net.abraxator.moresnifferflowers.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ModBoatRenderer extends BoatRenderer {
    private final Map<ModBoatEntity.Type, Pair<ResourceLocation, BoatModel>> boatResources;

    public ModBoatRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation location, boolean pChestBoat) {
        super(pContext, location);
        this.boatResources = Stream.of(ModBoatEntity.Type.values()).collect(ImmutableMap.toImmutableMap(type -> type,
               type -> Pair.of(ResourceLocation.fromNamespaceAndPath(MoreSnifferFlowers.MOD_ID, getTextureLocation(type, pChestBoat)), this.createBoatModel(pContext, type, pChestBoat))));
    }

    @Override
    public void render(BoatRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
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
        EntityModel<BoatRenderState> entitymodel = this.model();
        entitymodel.setupAnim(state);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.renderType());
        entitymodel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        this.renderTypeAdditions(state, poseStack, bufferSource, packedLight);
        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
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
/*
    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return this.boatResources.get(modBoat.getModVariant());
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return this.boatResources.get(modChestBoatEntity.getModVariant());
        } else {
            return null;
        }
    }
    
    private ModBoatEntity.Type getType(Boat boat) {
        if(boat instanceof ModBoatEntity modBoat) {
            return modBoat.getModVariant();
        } else if(boat instanceof ModChestBoatEntity modChestBoatEntity) {
            return modChestBoatEntity.getModVariant();
        } else {
            return null;
        }
    }
    
    private int boatColor(Boat boat) {
        if(this.getType(boat).equals(ModBoatEntity.Type.VIVICUS)) {
            if(boat instanceof VivicusBoatEntity vivicusBoat) {
                return vivicusBoat.colorValues().get(vivicusBoat.getColor());
            } else if(boat instanceof VivicusChestBoatEntity vivicusChestBoat) {
                return vivicusChestBoat.colorValues().get(vivicusChestBoat.getColor());
            }
        }
        
        return -1;
    }*/
}
