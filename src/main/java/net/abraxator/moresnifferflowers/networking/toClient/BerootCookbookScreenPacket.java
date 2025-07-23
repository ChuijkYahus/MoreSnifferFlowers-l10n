package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.client.gui.screen.cookbook.CookbookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.function.Supplier;

public record BerootCookbookScreenPacket() {
    public static void encode (BerootCookbookScreenPacket berootCookbookScreenPacket, FriendlyByteBuf friendlyByteBuf) {}

    public static BerootCookbookScreenPacket decode (FriendlyByteBuf friendlyByteBuf) {
       return new BerootCookbookScreenPacket();
    }

    public static void handle(BerootCookbookScreenPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handlePacket(packet));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(BerootCookbookScreenPacket packet) {
        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft.getInstance().setScreen((new CookbookScreen(player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS)
                .map(NutritionCapability::getItems)
                .orElse(new HashSet<>()))));
    }
}
