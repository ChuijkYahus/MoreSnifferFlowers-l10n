package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateBlockPatternsPacket(CompoundTag tag) {

    public static void encode(UpdateBlockPatternsPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static UpdateBlockPatternsPacket decode(FriendlyByteBuf buf) {
        return new UpdateBlockPatternsPacket(buf.readNbt());
    }

    public static void handle(UpdateBlockPatternsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CapabilityList.getBlockPatterns().load(msg.tag);
            ClientRegistration.getBlockPatternRenderer().markDirty();
        });
        ctx.get().setPacketHandled(true);
    }
}
