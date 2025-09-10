package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VivicusHangingSignRenderer extends HangingSignRenderer {
    public VivicusHangingSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void renderSignWithText(SignBlockEntity pSignEntity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, BlockState state, SignBlock pSignBlock, WoodType pWoodType, Model model) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.9375, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-pSignBlock.getYRotationDegrees(state)));
        poseStack.translate(0.0F, -0.3125F, 0.0F);
        renderVivicusSign(poseStack, buffer, packedLight, packedOverlay, pWoodType, model, state);
        this.renderSignText(
                pSignEntity.getBlockPos(),
                pSignEntity.getFrontText(),
                poseStack,
                buffer,
                packedLight,
                pSignEntity.getTextLineHeight(),
                pSignEntity.getMaxTextLineWidth(),
                true
        );
        this.renderSignText(
                pSignEntity.getBlockPos(),
                pSignEntity.getBackText(),
                poseStack,
                buffer,
                packedLight,
                pSignEntity.getTextLineHeight(),
                pSignEntity.getMaxTextLineWidth(),
                false
        );
        poseStack.popPose();
    }
    
    private void renderVivicusSign(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, WoodType pWoodType, Model model, BlockState state) {
        poseStack.pushPose();
        float f = this.getSignModelRenderScale();
        poseStack.scale(f, -f, -f);
        Material material = Sheets.getHangingSignMaterial(pWoodType);
        VertexConsumer vertexconsumer = material.buffer(buffer, model::renderType);
        var color = -1;
        if(state.getBlock() instanceof ColorableVivicusBlock colorableVivicusBlock) {
            var dyeColor = state.getValue(ModStateProperties.COLOR);
            color = colorableVivicusBlock.colorValues().get(dyeColor);
            vertexconsumer.color(color);
        }
        this.renderSignModel(poseStack, packedLight, packedOverlay, model, vertexconsumer, color);
        poseStack.popPose();
    }

    void renderSignModel(PoseStack poseStack, int packedLight, int packedOverlay, Model model, VertexConsumer pVertexConsumer, int color) {
        HangingSignRenderer.HangingSignModel hangingsignrenderer$hangingsignmodel = (HangingSignRenderer.HangingSignModel)model;
        hangingsignrenderer$hangingsignmodel.root.render(poseStack, pVertexConsumer, packedLight, packedOverlay,  1-((color >> 16) & 0xFF), 1-((color >> 8) & 0xFF), 1-(color & 0xFF), 1);
    }
}
