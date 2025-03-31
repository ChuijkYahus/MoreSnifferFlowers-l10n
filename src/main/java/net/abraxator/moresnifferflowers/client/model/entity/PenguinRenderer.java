package net.abraxator.moresnifferflowers.client.model.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.entities.PenguinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PenguinRenderer extends MobRenderer<PenguinEntity, PenguinModel<PenguinEntity>> {
    public PenguinRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PenguinModel<>(pContext.bakeLayer(ModModelLayerLocations.PENGUIN)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(PenguinEntity pEntity) {
        return new ResourceLocation(MoreSnifferFlowers.MOD_ID, "textures/entity/penguin.png");
    }

}
