package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.HardenedMouthCapability;
import net.abraxator.moresnifferflowers.capability.SlipperyCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncSlipperyPacket(SlipperyCapability capability, int entityId) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncSlipperyPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_slippery"));
    public static final StreamCodec<ByteBuf, SyncSlipperyPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(SlipperyCapability.CODEC), SyncSlipperyPacket::capability,
            ByteBufCodecs.INT, SyncSlipperyPacket::entityId,
            SyncSlipperyPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity entity = level.getEntity(entityId);

            if (entity instanceof Player player) {

                SlipperyCapability cap = player.getData(ModDataAttachments.SLIPPERY);

                cap.isFallen = capability.isFallen;
                cap.fallenTicks = capability.fallenTicks;
                cap.maxFallenTicks = capability.maxFallenTicks;

                if (!cap.isFallen){
                    cap.getUp(player);
                }
            }
        });
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
