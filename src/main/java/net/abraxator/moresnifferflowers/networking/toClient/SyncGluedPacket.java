package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncGluedPacket(boolean isGlued, int entityId) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncGluedPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_glued"));
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
                living.getData(ModDataAttachments.GLUED).isGlued = isGlued;
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
