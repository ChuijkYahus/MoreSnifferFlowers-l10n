package net.abraxator.moresnifferflowers.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

import javax.annotation.Nullable;

//TODO: Finish this
public class GhostMultiBlockRenderer extends GhostRenderer{
    BlockState blockState;
    @Nullable
    AbstractMultiBlockEntity multiBlockEntity;

    public GhostMultiBlockRenderer(BlockPos pos, int ticksRemaining, Block block) {
        super(pos, ticksRemaining);
        blockState = block.defaultBlockState();
    }

    @Override
    public void render(float partialTick, Frustum frustum, Camera camera, Level level, PoseStack poseStack, MultiBufferSource.BufferSource buffer) {
/*        ((IMultiBlock) blockState.getBlock()).getFullBlockShapeNoCache(pos, blockState).forEach(pos1 -> {

        });*/
    }
}
