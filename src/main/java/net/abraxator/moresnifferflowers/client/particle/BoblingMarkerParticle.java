package net.abraxator.moresnifferflowers.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import net.abraxator.moresnifferflowers.client.particle.options.BoblingMarkerOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class BoblingMarkerParticle extends TextureSheetParticle {
    protected final BoblingMarkerOptions options;
    
    protected BoblingMarkerParticle(ClientLevel level, double x, double y, double z, BoblingMarkerOptions options) {
        super(level, x, y, z);
        this.options = options;
        
        if(this.options.variation().equals(BoblingMarkerVariation.BLOCK)) {
            
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
    }
}
