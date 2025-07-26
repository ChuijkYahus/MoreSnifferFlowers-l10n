package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncMouthSlotsPacket(NonNullList<ItemStack> itemStacks, int cooldown) {

    public static void encode(SyncMouthSlotsPacket msg, FriendlyByteBuf buffer) {
        for (ItemStack itemStack : msg.itemStacks) {
            buffer.writeItem(itemStack);
        }
        buffer.writeInt(msg.cooldown);
    }

    public static SyncMouthSlotsPacket decode(FriendlyByteBuf buffer) {
        NonNullList<ItemStack> itemStacks1 = NonNullList.of(ItemStack.EMPTY, buffer.readItem(), buffer.readItem());
        int cooldown = buffer.readInt();
        return new SyncMouthSlotsPacket(itemStacks1, cooldown);
    }

    public static void handle(SyncMouthSlotsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handlePacket(msg));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(SyncMouthSlotsPacket msg) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
                cap.setAllItems(msg.itemStacks);
                cap.setCooldown(msg.cooldown);
            });
        }
    }

}
