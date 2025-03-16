package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.blockentities.SoupCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SoupCauldronCraftPacket(BlockPos blockPos) {
    public SoupCauldronCraftPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
    }

    public static void handle(SoupCauldronCraftPacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            var level = player.level();
            SoupCauldronBlockEntity entity = ((SoupCauldronBlockEntity) level.getBlockEntity(packet.blockPos()));
            entity.craft(player);
        });
    }
}
