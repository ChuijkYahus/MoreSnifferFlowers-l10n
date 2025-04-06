package net.abraxator.moresnifferflowers.client.model.block;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SaltemoneModel{

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-15.9813F, 2.4706F, -15.9941F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(-5.0563F, -2.5544F, -5.002F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0563F, 21.5294F, 0.002F));

        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-1, 46).mirror().addBox(-16.0F, 0.0F, -9.5F, 32.0F, 0.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0187F, -1.2081F, -8.727F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 64).addBox(-16.0F, 0.0F, -2.5F, 32.0F, 0.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0187F, 1.4706F, 2.2559F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -9.5F, 0.0F, 9.0F, 19.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.5F, 0.0F, -3.1416F, 0.7854F, 3.1416F));

        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -9.5F, 0.0F, 9.0F, 19.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

}
