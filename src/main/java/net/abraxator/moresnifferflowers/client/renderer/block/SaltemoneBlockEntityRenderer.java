package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;

public class SaltemoneBlockEntityRenderer<T extends SaltemoneBlockEntity> implements BlockEntityRenderer<T> {
    private ModelPart model;
    private static final Material SALTEMONE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/saltemone"));
    private static final Material SOURLEMON_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/sourlemon"));

    public SaltemoneBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.model = pContext.bakeLayer(ModModelLayerLocations.SALTEMONE);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(blockEntity.getBlockState().getValue(ModStateProperties.ENTITY) && blockEntity.getBlockState().getValue(ModStateProperties.AGE_2) >= 2) {
            poseStack.translate(1, 1.6, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(-180));
            Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
            switch (direction) {
                case EAST -> poseStack.translate(0, 0, -1);
                case WEST -> poseStack.translate(-1, 0, 0);
                case SOUTH -> poseStack.translate(-1, 0, -1);
            }
            Material material = blockEntity.getBlockState().is(ModBlocks.SOURLEMON.get()) ? SOURLEMON_TEXTURE : SALTEMONE_TEXTURE;
            this.model.render(poseStack, material.buffer(buffer, RenderType::entityCutout), packedLight, packedOverlay);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(pCameraPos.multiply(1.0D, 0.0D, 1.0D), this.getViewDistance());
    }
}
