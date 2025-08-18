package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.abraxator.moresnifferflowers.networking.MSFClientPacket;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record DyespriaDisplayModeChangePacket(int dyespriaModeId) implements MSFClientPacket {
    public static final CustomPacketPayload.Type<DyespriaDisplayModeChangePacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("display_dyespria_mode_change"));
    public static final StreamCodec<ByteBuf, DyespriaDisplayModeChangePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DyespriaDisplayModeChangePacket::dyespriaModeId,
            DyespriaDisplayModeChangePacket::new
    );
    
    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClientPacket(Player player, Level level) {
        player.displayClientMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(dyespriaModeId)), true);
    }
}
