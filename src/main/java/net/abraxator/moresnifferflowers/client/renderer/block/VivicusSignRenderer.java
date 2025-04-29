package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VivicusSignRenderer extends SignRenderer {
    public VivicusSignRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void renderSignWithText(SignBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BlockState state, SignBlock sign, WoodType woodType, Model model) {
        poseStack.pushPose();
        this.translateSign(poseStack, -sign.getYRotationDegrees(state), state);
        renderVivicusSign(blockEntity,poseStack, bufferSource, packedLight, packedOverlay, woodType, model, state);
        this.renderSignText(
                blockEntity.getBlockPos(),
                blockEntity.getFrontText(),
                poseStack,
                bufferSource,
                packedLight,
                blockEntity.getTextLineHeight(),
                blockEntity.getMaxTextLineWidth(),
                true
        );
        this.renderSignText(
                blockEntity.getBlockPos(),
                blockEntity.getBackText(),
                poseStack,
                bufferSource,
                packedLight,
                blockEntity.getTextLineHeight(),
                blockEntity.getMaxTextLineWidth(),
                false
        );
        poseStack.popPose();
    }

    private void renderVivicusSign(SignBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, WoodType woodType, Model model, BlockState state) {
        poseStack.pushPose();
        float f = this.getSignModelRenderScale();
        poseStack.scale(f, -f, -f);
        Material material = Sheets.getHangingSignMaterial(woodType);
        VertexConsumer vertexconsumer = material.buffer(bufferSource, model::renderType);
        var color = -1;
        if(state.getBlock() instanceof ColorableVivicusBlock colorableVivicusBlock) {
            var dyeColor = state.getValue(ModStateProperties.COLOR);
            color = colorableVivicusBlock.colorValues().get(dyeColor);
            vertexconsumer.setColor(color);
        }
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
