package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BerootCauldronCraftPacket(BlockPos blockPos) {
    public BerootCauldronCraftPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
    }

    public static void handle(BerootCauldronCraftPacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            var level = player.level();
            BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(packet.blockPos()));
            entity.craft(player);
        });
    }
}
