package net.abraxator.moresnifferflowers.client.model.baked;

import net.abraxator.moresnifferflowers.client.ModRenderTypes;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmptyBakedModel implements BakedModel {
    private final BakedModel original;

    public EmptyBakedModel(BakedModel original) {
        this.original = original;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {

        if (state.is(ModTags.ModBlockTags.FAKE_RENDER) && (renderType == null || !renderType.equals(ModRenderTypes.CRUMBLING_BLOCK))) {
            return List.of();
        }

        return original.getQuads(state, side, rand, data, renderType);
    }


    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return original.getQuads(state, direction, random);
    }

    // delegate everything else
    @Override public boolean useAmbientOcclusion() { return original.useAmbientOcclusion(); }
    @Override public boolean isGui3d() { return original.isGui3d(); }
    @Override public boolean usesBlockLight() { return original.usesBlockLight(); }
    @Override public boolean isCustomRenderer() { return original.isCustomRenderer(); }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return original.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return original.getOverrides();
    }

    @Override public TextureAtlasSprite getParticleIcon(ModelData data) { return original.getParticleIcon(data); }
}