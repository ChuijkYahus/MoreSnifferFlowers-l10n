package net.abraxator.moresnifferflowers.networking;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface MSFClientPacket extends MSFPacket {
    @Override
    default void handle(IPayloadContext context) {
        context.enqueueWork(() -> handleClientPacket(context.player(), context.player().level()));
    }

    @OnlyIn(Dist.CLIENT)
    void handleClientPacket(Player player, Level level);
}


