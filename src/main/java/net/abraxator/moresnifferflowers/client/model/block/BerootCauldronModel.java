package net.abraxator.moresnifferflowers.client.model.block;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BerootCauldronModel {
	private final ModelPart root;
	private final ModelPart bottom;
	private final ModelPart roots;
	private final ModelPart base;
	private final ModelPart spoon;

	public BerootCauldronModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bottom = this.root.getChild("bottom");
		this.roots = this.bottom.getChild("roots");
		this.base = this.root.getChild("base");
		this.spoon = this.base.getChild("spoon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bottom = root.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(142, 32).addBox(-29.8448F, -6.7645F, 7.8964F, 7.0F, 11.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(142, 32).mirror().addBox(-4.8448F, -6.7645F, 7.8964F, 7.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(13.8448F, -4.2355F, -13.8964F));

		PartDefinition copypasteish_r1 = bottom.addOrReplaceChild("copypasteish_r1", CubeListBuilder.create().texOffs(57, 116).addBox(-5.5F, 0.0F, -9.0F, 11.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 21.0F, 1.5708F, -0.5672F, 1.5708F));

		PartDefinition copypasteish_r2 = bottom.addOrReplaceChild("copypasteish_r2", CubeListBuilder.create().texOffs(57, 116).addBox(-5.5F, 0.0F, -9.0F, 11.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 7.0F, 1.5708F, 0.5672F, 1.5708F));

		PartDefinition roots = bottom.addOrReplaceChild("roots", CubeListBuilder.create().texOffs(100, 25).addBox(-21.0F, 0.0F, -2.0F, 24.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(100, 1).addBox(2.0F, 0.0F, -3.0F, 0.0F, 6.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(100, 25).addBox(-21.0F, 0.0F, 20.0F, 24.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(100, 1).addBox(-20.0F, 0.0F, -3.0F, 0.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.8448F, -3.7645F, 4.8964F));

		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 1).addBox(-21.0F, -14.0F, -3.0F, 24.0F, 14.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(40, 43).addBox(1.0F, -14.0F, -1.0F, -20.0F, 13.0F, 20.0F, new CubeDeformation(0.0F))
				.texOffs(0, 78).mirror().addBox(3.0F, -19.0F, -5.0F, 2.0F, 8.0F, 28.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 138).addBox(-21.0F, -19.0F, -5.0F, 24.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 138).addBox(-21.0F, -19.0F, 21.0F, 24.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 78).addBox(-23.0F, -19.0F, -5.0F, 2.0F, 8.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -8.0F, -9.0F));

		PartDefinition spoon = base.addOrReplaceChild("spoon", CubeListBuilder.create(), PartPose.offsetAndRotation(8.75F, -17.0F, 9.5F, -0.1745F, 0.0F, 0.1309F));

		PartDefinition cube_r1 = spoon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(113, 149).addBox(-1.0F, -11.5F, -1.0F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(122, 149).addBox(-1.0F, 9.5F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 9.0F, -0.1122F, -0.4677F, 0.2449F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}