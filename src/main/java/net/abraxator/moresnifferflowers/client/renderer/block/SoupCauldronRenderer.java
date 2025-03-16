package net.abraxator.moresnifferflowers.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.SoupCauldronBlockEntity;
import net.abraxator.moresnifferflowers.client.model.ModModelLayerLocations;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.crypto.interfaces.PBEKey;

public class SoupCauldronRenderer<T extends SoupCauldronBlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart cauldron;
    private final ModelPart spoon;

    public SoupCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.cauldron = context.bakeLayer(ModModelLayerLocations.BEROOT_CAULDRON);
        this.spoon = context.bakeLayer(ModModelLayerLocations.BEROOT_SPOON);
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        final Material CAULDRON_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/beroot_cauldron"));
        final Material SPOON_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MoreSnifferFlowers.loc("block/beroot_spoon"));
        final ResourceLocation SOUP_TEXTURE = MoreSnifferFlowers.loc("textures/block/beroot_soup.png");
        final VertexConsumer cauldron_consumer = CAULDRON_TEXTURE.buffer(buffer, RenderType::entityCutout);
        final VertexConsumer spoon_consumer = SPOON_TEXTURE.buffer(buffer, RenderType::entitySolid);
        final RandomSource randomSource = blockEntity.getLevel().getRandom();
        
        if(blockEntity.getBlockState().getValue(ModStateProperties.ENTITY)) {
            //CAULDRON
            poseStack.pushPose();
            poseStack.translate(1, 1.5, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(-180));
            cauldron.render(poseStack, cauldron_consumer, packedLight, packedOverlay);
            poseStack.popPose();

            //SOUP
            poseStack.pushPose();
            poseStack.translate(1, 0.6, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(-180));
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            int soupCount = blockEntity.soupCount;
            float size = 1.5F;
            float halfSize = size / 2.0F;
            float minX = -halfSize;
            float maxX = halfSize;
            float minZ = -halfSize;
            float maxZ = halfSize;
            float y = -((float) 1 / 8 * soupCount);
            
            renderFace(matrix4f, matrix3f, buffer.getBuffer(RenderType.beaconBeam(SOUP_TEXTURE, false)),
                    1.0F, 1.0F, 1.0F, 1.0F,
                    minX, maxX, y, minZ, maxZ);
            poseStack.popPose();

            //SPOON
            {
                float rot = blockEntity.getSpoonRotation(partialTick);
                poseStack.pushPose();
                poseStack.translate(1, 1.5, 0);
                poseStack.mulPose(Axis.XN.rotationDegrees(-180));
                poseStack.mulPose((new Quaternionf()).rotationY((float) (rot * (Math.PI / 180))));
                this.spoon.render(poseStack, spoon_consumer, packedLight, packedOverlay);
                poseStack.popPose();
            }
            
            //ITEMS
            for (int i = 0; i < blockEntity.ingredients.size(); i++) {
                poseStack.pushPose();
                ItemStack itemStack = blockEntity.ingredients.get(i);
                float speed = (float) randomSource.nextIntBetweenInclusive(50, 100) / 100;
                float rot = (float) (blockEntity.getItemsRotation(partialTick) * ((i + 1) * 0.1));
                //poseStack.translate(i / 0.2 + 0.1, absoluteY, i / 0.2 + 0.1);
                float a = (float) (i * 0.05);
                poseStack.translate(0.8 + a, (float) 1 / 16 * soupCount, 0.0 - a);
                poseStack.mulPose(new Quaternionf().rotationY((float) (rot * (Math.PI / 180))));
                poseStack.translate(0.25, 0, 0.25);
                poseStack.mulPose(new Quaternionf().rotationY((float) ((rot * 0.2) * (Math.PI / 180))));
                //poseStack.translate(randomSource.nextFloat(), 0, randomSource.nextFloat());
                poseStack.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, blockEntity.getLevel(), (int) (blockEntity.getBlockPos().asLong() + i));
                poseStack.popPose();
            }
        }
    }

    private void renderFace(Matrix4f pose, Matrix3f normal, VertexConsumer consumer, float red, float green, float blue, float alpha, float x0, float x1, float y, float z0, float z1) {
        consumer.vertex(pose, x1, y, z0).color(red, green, blue, alpha).uv(1.0F / 32.0F * 24.0F, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, x1, y, z1).color(red, green, blue, alpha).uv(1.0F / 32.0F * 24.0F, 1.0F / 32.0F * 24.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, x0, y, z1).color(red, green, blue, alpha).uv(0, 1.0F / 32.0F * 24.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, x0, y, z0).color(red, green, blue, alpha).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
