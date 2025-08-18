package net.abraxator.moresnifferflowers.networking.toServer;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DyespriaModePacket(int amount) implements MSFServerPacket {
    public static final Type<DyespriaModePacket> TYPE = new Type<>(MoreSnifferFlowers.loc("dyespria_mode"));
    public static final StreamCodec<ByteBuf, DyespriaModePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DyespriaModePacket::amount,
            DyespriaModePacket::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handlePacket(Player player, Level level) {
        var stack = player.getMainHandItem();
        if(stack.getItem() instanceof DyespriaItem dyespriaItem && player instanceof ServerPlayer serverPlayer) {
            dyespriaItem.changeMode(serverPlayer, stack, amount);
        }
    }
}
