package net.abraxator.moresnifferflowers.client.model.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.entities.CrabEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrabRender extends MobRenderer<CrabEntity, CrabModel<CrabEntity>> {
    public CrabRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CrabModel<>(pContext.bakeLayer(ModModelLayerLocations.CRAB)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(CrabEntity pEntity) {
        return new ResourceLocation(MoreSnifferFlowers.MOD_ID, "textures/entity/crab.png");
    }

}
