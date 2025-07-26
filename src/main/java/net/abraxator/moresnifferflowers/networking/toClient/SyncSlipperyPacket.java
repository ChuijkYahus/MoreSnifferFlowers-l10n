package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncSlipperyPacket(boolean isFallen, int entityId) {
    public SyncSlipperyPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isFallen);
        buf.writeInt(entityId);
    }

    public static void handle(SyncSlipperyPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(SyncSlipperyPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Entity entity = level.getEntity(packet.entityId);

        if (entity instanceof Player player) {
            GluedCapability.playSound(level, entity);
            player.getCapability(CapabilityList.SLIPPERY).ifPresent(cap -> {
                cap.isFallen = packet.isFallen;
            });
        }
    }
}
