package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncSlipperyPacket(boolean isFallen, int entityId, int fallenTicks, int maxFallenTicks) {
    public SyncSlipperyPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readInt(), buf.readInt(),  buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isFallen);
        buf.writeInt(entityId);
        buf.writeInt(fallenTicks);
        buf.writeInt(maxFallenTicks);
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

            player.getCapability(CapabilityList.SLIPPERY).ifPresent(cap -> {
                cap.isFallen = packet.isFallen;
                cap.fallenTicks = packet.fallenTicks;
                cap.maxFallenTicks = packet.maxFallenTicks;

                if (!cap.isFallen){
                    cap.getUp(player);
                }
            });



        }
    }
}
