package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncGluedPacket(boolean isGlued, int entityId) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncGluedPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("send_sludge_particle"));
    public static final StreamCodec<ByteBuf, SyncGluedPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncGluedPacket::isGlued,
            ByteBufCodecs.INT, SyncGluedPacket::entityId,
            SyncGluedPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity entity = level.getEntity(entityId);

            if (entity instanceof LivingEntity living) {
                GluedCapability.playSound(level, entity);
                living.getCapability(CapabilityList.GLUED).ifPresent(cap -> {
                    cap.isGlued = isGlued;
                });
            }
        });
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
