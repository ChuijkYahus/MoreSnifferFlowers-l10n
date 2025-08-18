package net.abraxator.moresnifferflowers.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface MSFPacket extends CustomPacketPayload {
    void handle(IPayloadContext context);
}
