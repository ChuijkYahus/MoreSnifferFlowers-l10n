package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public record SyncNutritionPacket(Set<Item> nutritionItems) {

    public static void encode(SyncNutritionPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.nutritionItems.size());
        for (Item item : msg.nutritionItems) {
            buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item));
        }
    }

    public static SyncNutritionPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        Set<Item> nutritionItems = new HashSet<>();

        for (int i = 0; i < size; i++) {
            ResourceLocation itemId = buffer.readResourceLocation();
            Item item = BuiltInRegistries.ITEM.get(itemId);
            if (item != Items.AIR) {
                nutritionItems.add(item);
            }
        }

        return new SyncNutritionPacket(nutritionItems);
    }

    public static void handle(SyncNutritionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handlePacket(msg, context));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(SyncNutritionPacket msg, NetworkEvent.Context context) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
                cap.setItems(msg.nutritionItems);
            });
        }
    }

}
