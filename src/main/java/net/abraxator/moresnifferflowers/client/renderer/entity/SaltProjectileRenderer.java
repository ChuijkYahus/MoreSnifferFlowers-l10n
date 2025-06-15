package net.abraxator.moresnifferflowers.client.renderer.entity;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SaltProjectileRenderer extends CoolProjectileRenderer<SaltProjectile>{
    public static final ResourceLocation TEXTURE_SALT = MoreSnifferFlowers.loc("textures/entity/salt_projectile.png");
    public static final ResourceLocation TEXTURE_SOUR = MoreSnifferFlowers.loc("textures/entity/sour_projectile.png");


    protected SaltProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SaltProjectile entity) {
        return entity.isCorrupted() ? TEXTURE_SOUR : TEXTURE_SALT;
    }
}
