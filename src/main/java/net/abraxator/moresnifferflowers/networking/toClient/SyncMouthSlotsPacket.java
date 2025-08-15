package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.HardenedMouthCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncMouthSlotsPacket(HardenedMouthCapability capability) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncMouthSlotsPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_mouth"));
    public static final StreamCodec<ByteBuf, SyncMouthSlotsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(HardenedMouthCapability.CODEC), SyncMouthSlotsPacket::capability,
            SyncMouthSlotsPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                HardenedMouthCapability cap = player.getData(ModDataAttachments.HARDENED_MOUTH);

                cap.setAllItems(capability.getMouthSlotItems());
                cap.setCooldown(capability.getCooldown());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
