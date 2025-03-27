package net.abraxator.moresnifferflowers.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BoblingModel<T extends BoblingEntity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart torso_upper;
	private final ModelPart legs;
	private final ModelPart right_feet;
	private final ModelPart left_feet;
	private final ModelPart head;
	private final ModelPart leaves;

	public BoblingModel(ModelPart root) {
		this.root = root.getChild("root");
		this.torso_upper = this.root.getChild("torso_upper");
		this.legs = this.root.getChild("legs");
		this.right_feet = this.legs.getChild("right_feet");
		this.left_feet = this.legs.getChild("left_feet");
		this.head = this.root.getChild("head");
		this.leaves = this.head.getChild("leaves");
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-4.5F, 3.9572F, -50.5712F));

		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(13.5F, 20.0428F, 27.5712F));

		PartDefinition left_feet = legs.addOrReplaceChild("left_feet", CubeListBuilder.create().texOffs(124, 33).addBox(-1.0F, -16.0F, -40.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 117).addBox(-1.0F, -22.0F, -3.0F, 4.0F, 22.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 37.0F));

		PartDefinition right_feet = legs.addOrReplaceChild("right_feet", CubeListBuilder.create().texOffs(114, 111).addBox(-1.0F, -22.0F, 34.0F, 4.0F, 22.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(124, 13).addBox(-1.0F, -16.0F, -3.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-20.0F, 0.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(68, 58).addBox(1.0F, -14.0F, -5.0F, 15.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, -6.9572F, 14.5712F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(124, 0).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 6.9572F, -14.5712F, 0.7437F, -0.0643F, -0.059F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 117).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5F, 6.9572F, -14.5712F, 0.7437F, 0.0643F, 0.059F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -2.0F, -20.0F, 4.0F, 4.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5F, 3.5793F, -10.8848F, 0.7418F, 0.0F, 0.0F));

		PartDefinition leaves = head.addOrReplaceChild("leaves", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = leaves.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(82, 111).addBox(1.0F, -12.0F, -3.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition cube_r5 = leaves.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(50, 111).addBox(-1.0F, -12.0F, -3.0F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

		PartDefinition torso_upper = root.addOrReplaceChild("torso_upper", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -23.0F, -28.0F, 27.0F, 23.0F, 35.0F, new CubeDeformation(0.0F))
				.texOffs(68, 86).addBox(-12.0F, -21.0F, -34.0F, 23.0F, 19.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 6.0428F, 57.5712F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.xRot = headPitch * (float) (Math.PI / 180.0);
		this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
		
		this.right_feet.xRot = Mth.cos(limbSwing * 0.8F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.left_feet.xRot = Mth.cos(limbSwing * 0.8F) * 1.4F * limbSwingAmount;
		this.torso_upper.zRot = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;
		this.head.zRot = Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
		
		this.animate(entity.plantingAnimationState, BoblingAnimations.PLANT, ageInTicks);
		this.animate(entity.idleAnimationState, BoblingAnimations.IDLE, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}
}