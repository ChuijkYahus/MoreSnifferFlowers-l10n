package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BerootCauldronSuckPacket(ItemStack itemStack, BlockPos pos) implements IMSFPacket {
    public static final CustomPacketPayload.Type<BerootCauldronSuckPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("cauldron_suck"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BerootCauldronSuckPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, BerootCauldronSuckPacket::itemStack,
            BlockPos.STREAM_CODEC, BerootCauldronSuckPacket::pos,
            BerootCauldronSuckPacket::new
    );


    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level.isClientSide) {
                BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(pos));
                entity.addItem(itemStack, null);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
