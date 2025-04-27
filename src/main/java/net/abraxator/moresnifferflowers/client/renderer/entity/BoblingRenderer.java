package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.client.model.entity.BoblingModel;
import net.abraxator.moresnifferflowers.client.renderstate.BoblingRenderState;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoblingRenderer extends MobRenderer<BoblingEntity,BoblingRenderState, BoblingModel> {
    public static final ResourceLocation CORRUPTED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/corrupted_bobling.png");
    public static final ResourceLocation CURED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bobling.png");
    public static final ResourceLocation BONMEELED_TEXTURE = MoreSnifferFlowers.loc("textures/entity/bobling/bonmeeled_bobling.png");
    
    public BoblingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BoblingModel(pContext.bakeLayer(ModModelLayerLocations.BOBLING)), 0.4F);
    }

    @Override
    public BoblingRenderState createRenderState() {
        return new BoblingRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(BoblingRenderState renderState) {
        if (!renderState.isCured) {
            return CORRUPTED_TEXTURE;
        } else {
            return CURED_TEXTURE;
        }
    }

    @Override
    public void extractRenderState(BoblingEntity entity, BoblingRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.idleAnimationState = entity.idleAnimationState;
        state.plantingAnimationState = entity.plantingAnimationState;
        state.isCured = entity.isCured();

    }

}
