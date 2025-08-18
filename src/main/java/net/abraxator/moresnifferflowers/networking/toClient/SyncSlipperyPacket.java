package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.SlipperyCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.MSFClientPacket;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SyncSlipperyPacket(SlipperyCapability capability, int entityId) implements MSFClientPacket {
    public static final CustomPacketPayload.Type<SyncSlipperyPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_slippery"));
    public static final StreamCodec<ByteBuf, SyncSlipperyPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(SlipperyCapability.CODEC), SyncSlipperyPacket::capability,
            ByteBufCodecs.INT, SyncSlipperyPacket::entityId,
            SyncSlipperyPacket::new
    );

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClientPacket(Player player, Level level) {
        Entity entity = level.getEntity(entityId);

        if (entity instanceof Player player1) {

            SlipperyCapability cap = player1.getData(ModDataAttachments.SLIPPERY);

            cap.isFallen = capability.isFallen;
            cap.fallenTicks = capability.fallenTicks;
            cap.maxFallenTicks = capability.maxFallenTicks;

            if (!cap.isFallen){
                cap.getUp(player1);
            }
        }

    }


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
