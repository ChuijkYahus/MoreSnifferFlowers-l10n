package net.abraxator.moresnifferflowers.client.model.block;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SaltemoneModel{

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-23.925F, 0.0F, -7.9922F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(-13.0F, -5.025F, 3.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition cube_r1 = root.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-1, 46).mirror().addBox(-16.0F, 0.0F, -9.5F, 32.0F, 0.0F, 19.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.925F, -3.6788F, -0.725F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r2 = root.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 64).addBox(-16.0F, 0.0F, -2.5F, 32.0F, 0.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.925F, -1.0F, 10.2578F, 0.3927F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

}
