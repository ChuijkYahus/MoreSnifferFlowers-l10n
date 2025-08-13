package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

import java.util.function.Supplier;

public record SaltemoneParticlePacket(Vector3f pos) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SaltemoneParticlePacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("saltemone_particle"));
    public static final StreamCodec<ByteBuf, SaltemoneParticlePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, SaltemoneParticlePacket::pos,
            SaltemoneParticlePacket::new
    );


    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            var level = Minecraft.getInstance().level;
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

        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
