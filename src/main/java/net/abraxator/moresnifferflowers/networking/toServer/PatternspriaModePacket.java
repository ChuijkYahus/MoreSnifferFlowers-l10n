package net.abraxator.moresnifferflowers.networking.toServer;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PatternspriaModePacket(int amount) implements MSFServerPacket {
    public static final Type<PatternspriaModePacket> TYPE = new Type<>(MoreSnifferFlowers.loc("patternspria_mode"));
    public static final StreamCodec<ByteBuf, PatternspriaModePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PatternspriaModePacket::amount,
            PatternspriaModePacket::new
    );

    @Override
    public void handlePacket(Player player, Level level) {
        var stack = player.getMainHandItem();
        if(stack.getItem() instanceof PatternspriaItem dyespriaItem) {
            dyespriaItem.changeMode((ServerPlayer) player, stack, amount);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
