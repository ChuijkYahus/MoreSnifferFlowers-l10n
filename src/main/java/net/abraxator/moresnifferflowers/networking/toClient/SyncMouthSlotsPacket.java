package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncMouthSlotsPacket(NonNullList<ItemStack> itemStacks, int cooldown) implements IMSFPacket {

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
/*            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
                    cap.setAllItems(msg.itemStacks);
                    cap.setCooldown(msg.cooldown);
                });
            }*/
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
