package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;

public record SyncNutritionPacket(Set<Item> nutritionItems) implements IMSFPacket {

    public static void encode(SyncNutritionPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.nutritionItems.size());
        for (Item item : msg.nutritionItems) {
            buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item));
        }
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
/*            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
                    cap.setItems(msg.nutritionItems);
                });
            }*/
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
