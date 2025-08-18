package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.HardenedMouthCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.MSFClientPacket;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncMouthSlotsPacket(HardenedMouthCapability capability) implements MSFClientPacket {
    public static final CustomPacketPayload.Type<SyncMouthSlotsPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_mouth"));
    public static final StreamCodec<ByteBuf, SyncMouthSlotsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(HardenedMouthCapability.CODEC), SyncMouthSlotsPacket::capability,
            SyncMouthSlotsPacket::new
    );

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClientPacket(Player player, Level level) {
        HardenedMouthCapability cap = player.getData(ModDataAttachments.HARDENED_MOUTH);

        cap.setAllItems(capability.getMouthSlotItems());
        cap.setCooldown(capability.getCooldown());

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
