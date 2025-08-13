package net.abraxator.moresnifferflowers.networking.toServer;

import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record PatternspriaModePacket(int amount) implements IMSFPacket {
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            var player = context.player();
            var stack = player.getMainHandItem();
            if(stack.getItem() instanceof PatternspriaItem dyespriaItem) {
                dyespriaItem.changeMode((ServerPlayer) player, stack, amount);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
