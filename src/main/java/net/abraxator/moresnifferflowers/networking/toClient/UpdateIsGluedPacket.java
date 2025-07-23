package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateIsGluedPacket(boolean isGlued, int entityId) {
    public UpdateIsGluedPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isGlued);
        buf.writeInt(entityId);
    }

    public static void handle(UpdateIsGluedPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(UpdateIsGluedPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Entity entity = level.getEntity(packet.entityId);

        if (entity instanceof LivingEntity living) {
            living.getCapability(CapabilityList.GLUED).ifPresent(cap -> {
                cap.isGlued = packet.isGlued;
            });
        }
    }

}
