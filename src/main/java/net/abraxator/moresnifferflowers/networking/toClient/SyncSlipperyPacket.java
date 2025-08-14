package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncSlipperyPacket(boolean isFallen, int entityId, int fallenTicks, int maxFallenTicks) implements IMSFPacket {

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
/*            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity entity = level.getEntity(entityId);

            if (entity instanceof Player player) {

                player.getCapability(CapabilityList.SLIPPERY).ifPresent(cap -> {
                    cap.isFallen = packet.isFallen;
                    cap.fallenTicks = packet.fallenTicks;
                    cap.maxFallenTicks = packet.maxFallenTicks;

                    if (!cap.isFallen){
                        cap.getUp(player);
                    }
                });
            }*/
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
