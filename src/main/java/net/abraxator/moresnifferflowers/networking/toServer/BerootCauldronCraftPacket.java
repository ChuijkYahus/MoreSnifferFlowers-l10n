package net.abraxator.moresnifferflowers.networking.toServer;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.abraxator.moresnifferflowers.networking.toClient.SyncGluedPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record BerootCauldronCraftPacket(BlockPos blockPos) implements IMSFPacket {
    public static final CustomPacketPayload.Type<BerootCauldronCraftPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("send_sludge_particle"));
    public static final StreamCodec<ByteBuf, BerootCauldronCraftPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BerootCauldronCraftPacket::blockPos,
            BerootCauldronCraftPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            var player = context.player();
            var level = player.level();
            BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(blockPos()));
            entity.craft();

        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
