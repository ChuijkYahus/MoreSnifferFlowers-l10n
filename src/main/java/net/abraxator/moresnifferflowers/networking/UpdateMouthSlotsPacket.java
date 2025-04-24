package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateMouthSlotsPacket(NonNullList<ItemStack> itemStacks) {

    public static void encode(UpdateMouthSlotsPacket msg, FriendlyByteBuf buffer) {
        for (ItemStack itemStack : msg.itemStacks) {
            buffer.writeItem(itemStack);
        }
    }

    public static UpdateMouthSlotsPacket decode(FriendlyByteBuf buffer) {
        NonNullList<ItemStack> itemStacks1 = NonNullList.of(buffer.readItem(), buffer.readItem());
        return new UpdateMouthSlotsPacket(itemStacks1);
    }

    public static void handle(UpdateMouthSlotsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();
            if (player != null) {
                player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
                    cap.setAllItems(msg.itemStacks);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
