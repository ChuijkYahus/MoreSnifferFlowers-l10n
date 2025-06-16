package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public record SaltemoneParticlePacket(Vector3f pos) {
    public SaltemoneParticlePacket(FriendlyByteBuf buf) {
        this(buf.readVector3f());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVector3f(pos);
    }

    public static void handle(SaltemoneParticlePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(SaltemoneParticlePacket packet) {
        var level = Minecraft.getInstance().level;
        Vector3f pos = packet.pos;
        RandomSource random = level.random;

        for (int i = 0; i < 5; i++) {
            Particle particle = Minecraft.getInstance().particleEngine.createParticle(ModParticles.BUBBLE.get(),
                    pos.x + random.nextDouble() - 0.5, pos.y + random.nextDouble() - 0.5, pos.z + random.nextDouble() - 0.5, (random.nextDouble()  - 0.5 )/2, (random.nextDouble()  - 0.5 )*2, (random.nextDouble()  - 0.5 )/2);
            if (particle != null) {
                particle.scale(0.5F + random.nextFloat());
                particle.setLifetime(random.nextIntBetweenInclusive(15, 25));
            }
           // level.addParticle(ParticleTypes.BUBBLE, pos.x + random.nextDouble(), pos.y + random.nextDouble(), pos.z + random.nextDouble(), random.nextDouble()/2, random.nextDouble()*2, random.nextDouble()/2);
        }
    }

}
