package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FlyParticle extends TextureSheetParticle {
    protected FlyParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSize(0.1F, 0.1F);
        this.gravity = 0.005F;
        this.lifetime = 300 + random.nextInt(40);
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
            FlyParticle flyParticle = new FlyParticle(level, x, y, z);
            flyParticle.pickSprite(spriteSet);
            return flyParticle;
        }
    }
}
