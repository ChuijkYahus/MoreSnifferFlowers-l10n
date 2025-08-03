package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.components.PreviewState;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class SaltemoneBlockEntityRenderer<T extends SaltemoneBlockEntity> implements BlockEntityRenderer<T>, MultiblockRender {
    private final ModelPart body;
    private final ModelPart top;
    private static final Material SALTEMONE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/saltemone"));
    private static final Material SOURLEMON_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/sourlemon"));

    public SaltemoneBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.body = pContext.bakeLayer(ModModelLayerLocations.SALTEMONE);
        this.top = pContext.bakeLayer(ModModelLayerLocations.SALTEMONE_TOP);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(blockEntity.getBlockState().getValue(ModStateProperties.CENTER) && blockEntity.getBlockState().getValue(ModStateProperties.AGE_2) >= 2) {
            PreviewState previewState = blockEntity.previewState;
            VertexConsumer consumer = getConsumer(buffer, blockEntity, SALTEMONE_TEXTURE, SOURLEMON_TEXTURE, ModBlocks.SOURLEMONE.get());

            poseStack.pushPose();
            Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
            poseStack.mulPose(direction.getCounterClockWise().getRotation());
            poseStack.mulPose(Axis.XN.rotationDegrees(-90));
            poseStack.translate(0, -1.4, 0);

            switch (direction) {
                case EAST -> poseStack.translate(-1, 0, 1);
                case WEST -> poseStack.translate(0, 0, 0);
                case SOUTH -> poseStack.translate(-1, 0, 0);
                case NORTH -> poseStack.translate(0, 0, 1);
            }

            render(body, poseStack, consumer, packedLight, packedOverlay, previewState);

            float time = (level().getGameTime() + partialTick) / 20f;
            float scale = 1.0f + 0.3f * Mth.sin(time / 2 * Mth.TWO_PI + blockEntity.center.getX() + blockEntity.center.getZ());
            poseStack.scale(scale, scale / 1.5f + 0.4f, scale);
            poseStack.translate(0, -scale + 2.32, 0);

            render(top, poseStack, consumer, packedLight, packedOverlay, previewState);
            poseStack.popPose();
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
