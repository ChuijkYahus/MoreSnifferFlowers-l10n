package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.HardenedMouthCapability;
import net.abraxator.moresnifferflowers.components.BetterNonNullList;
import net.abraxator.moresnifferflowers.networking.StreamCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record SyncMouthSlotsPacket(List<ItemStack> itemStacks, int cooldown) {
    public static final StreamCodec<SyncMouthSlotsPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.ITEM_STACK.list(), SyncMouthSlotsPacket::itemStacks,
            StreamCodec.INT, SyncMouthSlotsPacket::cooldown,
            SyncMouthSlotsPacket::new
    );

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
                cap.setAllItems(new BetterNonNullList<>(msg.itemStacks, ItemStack.EMPTY));
                cap.setCooldown(msg.cooldown);
            });
        }
    }

}
