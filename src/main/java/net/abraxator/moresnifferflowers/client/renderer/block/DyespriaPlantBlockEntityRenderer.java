package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.blockentities.DyespriaPlantBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DyespriaPlantBlockEntityRenderer implements BlockEntityRenderer<DyespriaPlantBlockEntity> {
    private final EntityRenderDispatcher entityRenderDispatcher;
    
    public DyespriaPlantBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        entityRenderDispatcher = context.getEntityRenderer();
    }

    @Override
    public void render(DyespriaPlantBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var isGrown = blockEntity.getBlockState().getValue(ModStateProperties.AGE_3) >= 3;
        var hasDye = !blockEntity.dye.isEmpty();
        
        if(isGrown && hasDye && !blockEntity.getBlockState().getValue(ModStateProperties.SHEARED)) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            DyeItem dyeItem = DyeItem.byColor(blockEntity.getBlockState().getValue(ModStateProperties.COLOR));
            poseStack.pushPose();
            poseStack.translate(0.5, 0.9375, 0.5);
            poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
            poseStack.scale(0.35F, 0.35F, 0.35F);
            itemRenderer.renderStatic(new ItemStack(dyeItem), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), ((int) blockEntity.getBlockPos().asLong()));
            poseStack.popPose();
        }
    }
}
