package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.items.PatternspriaItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PatternspriaModePacket(int amount) {
    public PatternspriaModePacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(amount);
    }

    // Im not making a new interface until theres at least 3
    public static void handle(PatternspriaModePacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            var stack = player.getMainHandItem();
            if(stack.getItem() instanceof PatternspriaItem dyespriaItem) {
                dyespriaItem.changeMode(player, stack, packet.amount);
            }
        });
    }
}
