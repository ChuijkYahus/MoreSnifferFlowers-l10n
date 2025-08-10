package net.abraxator.moresnifferflowers.networking.toClient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public record CorruptionParticlePacket (BlockPos pos, boolean isPositive, boolean isFlower) {
    public CorruptionParticlePacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isPositive);
        buf.writeBoolean(isFlower);
    }

    public static void handle(CorruptionParticlePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(CorruptionParticlePacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        RandomSource random = level.random;
        BlockPos pos = packet.pos;
        boolean isPositive = packet.isPositive;
        boolean isFlower = packet.isFlower;
        BlockState state = level.getBlockState(pos);

        Vec3 vec = pos.getCenter();

        if(isFlower){
            Vec3 offset = state.getOffset(level, pos).add(vec);
            vec.add(offset);
            for (int i = 0; i < 10; i++){
                double inaccuracy = 0.7;
                double xOff = (random.nextFloat() - 0.5f) * inaccuracy;
                double zOff = (random.nextFloat() - 0.5f) * inaccuracy;
                double yOff = random.nextFloat() / 2 ;

                level.addParticle(ParticleTypes.HAPPY_VILLAGER, offset.x + xOff, offset.y + yOff, offset.z + zOff, 0, 0.2, 0);

            }

        } else {
            for (int i = 0; i < 5; i++) {
                double xOff = random.nextFloat() - 0.5f;
                double zOff = random.nextFloat() - 0.5f;
                double yOff = random.nextFloat() / 3 + 0.5f ;

                double slowDown = 3;

                ParticleOptions particle = isPositive ? ParticleTypes.HAPPY_VILLAGER : new DustParticleOptions(new Vector3f(107f / 255f, 62f /255f , 122f / 255f), random.nextFloat() /2 + 0.5f);
                level.addParticle(particle, vec.x + xOff, vec.y + yOff, vec.z + zOff, xOff / slowDown, 0.2, zOff / slowDown);

            }
        }

    }
}
