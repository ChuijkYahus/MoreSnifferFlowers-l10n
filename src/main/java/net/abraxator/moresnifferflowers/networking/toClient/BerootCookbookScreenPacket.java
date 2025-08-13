package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.client.gui.screen.cookbook.CookbookScreen;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;

public record BerootCookbookScreenPacket() implements IMSFPacket {
    public static final CustomPacketPayload.Type<BerootCookbookScreenPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("cauldron_screen"));
    public static final StreamCodec<FriendlyByteBuf, BerootCookbookScreenPacket> STREAM_CODEC = StreamCodec.of(
                    (buf, pkt) -> {},
                    buf -> new BerootCookbookScreenPacket());

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            LocalPlayer player = Minecraft.getInstance().player;
            Minecraft.getInstance().setScreen((new CookbookScreen(player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS)
                    .map(NutritionCapability::getItems)
                    .orElse(new HashSet<>()))));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
