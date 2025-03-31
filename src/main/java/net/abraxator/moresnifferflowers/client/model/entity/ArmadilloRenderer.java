package net.abraxator.moresnifferflowers.client.model.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.entities.ArmadilloEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArmadilloRenderer extends MobRenderer<ArmadilloEntity, ArmadilloModel<ArmadilloEntity>> {
    public ArmadilloRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ArmadilloModel<>(pContext.bakeLayer(ModModelLayerLocations.ARMADILLO)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(ArmadilloEntity pEntity) {
        return new ResourceLocation(MoreSnifferFlowers.MOD_ID, "textures/entity/armadillo.png");
    }

}
