package net.abraxator.moresnifferflowers.networking.toClient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public record CorruptedSludgePacket(Vector3f start, Vector3f target, Vector3f direction) {
    public CorruptedSludgePacket(FriendlyByteBuf buf) {
        this(buf.readVector3f(), buf.readVector3f(), buf.readVector3f());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVector3f(start);
        buf.writeVector3f(target);
        buf.writeVector3f(direction);
    }
    
    public static void handle(CorruptedSludgePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(CorruptedSludgePacket packet) {
        var level = Minecraft.getInstance().level;
        if (level.isClientSide()) {
            float distance = packet.start.distance(packet.target);

            for (int i = 0; i < 15; i++) {
                double progress = (double) i / 15;
                Vector3f pos = new Vector3f(packet.start).add(new Vector3f(packet.direction).mul((float) (distance * progress)));
                level.addParticle(new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
