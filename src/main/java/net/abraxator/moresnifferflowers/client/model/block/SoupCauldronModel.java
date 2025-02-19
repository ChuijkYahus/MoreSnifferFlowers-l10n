package net.abraxator.moresnifferflowers.client.model.block;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SoupCauldronModel {
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -2.0F, -8.0F, 32.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(6.0F, -32.0F, -8.0F, 2.0F, 30.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(68, 34).addBox(-24.0F, -32.0F, -8.0F, 2.0F, 30.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(60, 96).addBox(-3.0F, -30.0F, -2.0F, 2.0F, 30.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -2.0F, 21.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 96).addBox(-3.0F, -30.0F, -2.0F, 2.0F, 30.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -2.0F, -9.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}
}