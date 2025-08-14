package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.components.PatternspriaMode;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DyespriaDisplayModeChangePacket(int dyespriaModeId) implements IMSFPacket {
    public static final CustomPacketPayload.Type<DyespriaDisplayModeChangePacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("display_dyespria_mode_change"));
    public static final StreamCodec<ByteBuf, DyespriaDisplayModeChangePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DyespriaDisplayModeChangePacket::dyespriaModeId,
            DyespriaDisplayModeChangePacket::new
    );
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.displayClientMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(dyespriaModeId)), true);
        });
    }
}
