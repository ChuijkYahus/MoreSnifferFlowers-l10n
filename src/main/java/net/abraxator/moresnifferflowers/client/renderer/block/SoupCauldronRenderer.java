package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SoupCauldronBlockEntity;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
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

public class SoupCauldronRenderer<T extends SoupCauldronBlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart model;

    public SoupCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.model = context.bakeLayer(ModModelLayerLocations.BEROOT_CAULDRON);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/beroot_cauldron"));
        VertexConsumer consumer = TEXTURE.buffer(buffer, RenderType::entityCutout);
        
        if(blockEntity.getBlockState().getValue(ModStateProperties.ENTITY)) {
            poseStack.pushPose();
            poseStack.translate(-0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.XN.rotationDegrees(180));
            // poseStack.mulPose(direction.getRotation());
            model.render(poseStack, consumer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }
}
