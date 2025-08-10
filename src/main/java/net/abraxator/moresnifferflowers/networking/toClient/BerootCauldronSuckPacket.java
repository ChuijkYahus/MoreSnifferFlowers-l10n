package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BerootCauldronSuckPacket(ItemStack itemStack, BlockPos pos) {
    public BerootCauldronSuckPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeBlockPos(pos);
    }

    public static void handle(BerootCauldronSuckPacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> handlePacket(packet));
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(BerootCauldronSuckPacket packet) {
        var level = Minecraft.getInstance().level;
        ItemStack itemStack = packet.itemStack;
        if (level.isClientSide) {
            BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(packet.pos));

            entity.addItem(itemStack, null);
        }
    }
}
