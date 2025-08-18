package net.abraxator.moresnifferflowers.networking.toServer;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BerootCauldronCraftPacket(BlockPos blockPos) implements MSFServerPacket {
    public static final CustomPacketPayload.Type<BerootCauldronCraftPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("cauldron_craft"));
    public static final StreamCodec<ByteBuf, BerootCauldronCraftPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BerootCauldronCraftPacket::blockPos,
            BerootCauldronCraftPacket::new
    );


    @Override
    public void handlePacket(Player player, Level level) {
        BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(blockPos()));
        entity.craft();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
