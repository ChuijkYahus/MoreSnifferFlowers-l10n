package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class GiantCropParticle extends TextureSheetParticle {
    protected GiantCropParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.scale(5);
        this.setLifetime(25);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GiantCropParticle giantCropParticle = new GiantCropParticle(level, x, y, z);
            giantCropParticle.pickSprite(spriteSet);
            return giantCropParticle;
        }
    }
}
