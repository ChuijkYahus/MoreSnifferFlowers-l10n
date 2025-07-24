package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.toClient.*;
import net.abraxator.moresnifferflowers.networking.toServer.BerootCauldronCraftPacket;
import net.abraxator.moresnifferflowers.networking.toServer.DyespriaModePacket;
import net.abraxator.moresnifferflowers.networking.toServer.PatternspriaModePacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            MoreSnifferFlowers.loc("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    public static void init() {
        int id = 0;

        // REMEMBER:
        // ctx.getSender for Servers
        // Minecraft.getInstance.level for Client
        CHANNEL.messageBuilder(CorruptedSludgePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CorruptedSludgePacket::encode).decoder(CorruptedSludgePacket::new).consumerMainThread(CorruptedSludgePacket::handle).add();

        CHANNEL.messageBuilder(DyespriaModePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(DyespriaModePacket::encode).decoder(DyespriaModePacket::new).consumerMainThread(DyespriaModePacket::handle).add();

        CHANNEL.messageBuilder(BerootCauldronCraftPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(BerootCauldronCraftPacket::encode).decoder(BerootCauldronCraftPacket::new).consumerMainThread(BerootCauldronCraftPacket::handle).add();

        CHANNEL.messageBuilder(UpdateNutritionPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateNutritionPacket::encode).decoder(UpdateNutritionPacket::decode).consumerMainThread(UpdateNutritionPacket::handle).add();

        CHANNEL.messageBuilder(BerootCauldronSuckPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BerootCauldronSuckPacket::encode).decoder(BerootCauldronSuckPacket::new).consumerMainThread(BerootCauldronSuckPacket::handle).add();

        CHANNEL.messageBuilder(UpdateMouthSlotsPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateMouthSlotsPacket::encode).decoder(UpdateMouthSlotsPacket::decode).consumerMainThread(UpdateMouthSlotsPacket::handle).add();

        CHANNEL.messageBuilder(UpdateBlockPatternsPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateBlockPatternsPacket::encode).decoder(UpdateBlockPatternsPacket::decode).consumerMainThread(UpdateBlockPatternsPacket::handle).add();

        CHANNEL.messageBuilder(PatternspriaModePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PatternspriaModePacket::encode).decoder(PatternspriaModePacket::new).consumerMainThread(PatternspriaModePacket::handle).add();

        CHANNEL.messageBuilder(SaltemoneParticlePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SaltemoneParticlePacket::encode).decoder(SaltemoneParticlePacket::new).consumerMainThread(SaltemoneParticlePacket::handle).add();

        CHANNEL.messageBuilder(BerootCookbookScreenPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BerootCookbookScreenPacket::encode).decoder(BerootCookbookScreenPacket::decode).consumerMainThread(BerootCookbookScreenPacket::handle).add();

        CHANNEL.messageBuilder(UpdateGluedPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateGluedPacket::encode).decoder(UpdateGluedPacket::new).consumerMainThread(UpdateGluedPacket::handle).add();

    }
}
