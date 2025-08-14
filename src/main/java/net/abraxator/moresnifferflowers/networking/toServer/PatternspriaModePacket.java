package net.abraxator.moresnifferflowers.networking.toServer;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record PatternspriaModePacket(int amount) implements IMSFPacket {
    public static final Type<PatternspriaModePacket> TYPE = new Type<>(MoreSnifferFlowers.loc("patternspria_mode"));
    public static final StreamCodec<ByteBuf, PatternspriaModePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PatternspriaModePacket::amount,
            PatternspriaModePacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            var player = context.player();
            var stack = player.getMainHandItem();
            if(stack.getItem() instanceof PatternspriaItem dyespriaItem) {
                dyespriaItem.changeMode((ServerPlayer) player, stack, amount);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
