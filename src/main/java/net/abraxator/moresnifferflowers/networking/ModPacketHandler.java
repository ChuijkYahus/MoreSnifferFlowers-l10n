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

        CHANNEL.messageBuilder(SyncNutritionPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncNutritionPacket::encode).decoder(SyncNutritionPacket::decode).consumerMainThread(SyncNutritionPacket::handle).add();

        CHANNEL.messageBuilder(BerootCauldronSuckPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BerootCauldronSuckPacket::encode).decoder(BerootCauldronSuckPacket::new).consumerMainThread(BerootCauldronSuckPacket::handle).add();

        CHANNEL.messageBuilder(SyncMouthSlotsPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncMouthSlotsPacket::encode).decoder(SyncMouthSlotsPacket::decode).consumerMainThread(SyncMouthSlotsPacket::handle).add();

        CHANNEL.messageBuilder(SyncBlockPatternsPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncBlockPatternsPacket::encode).decoder(SyncBlockPatternsPacket::decode).consumerMainThread(SyncBlockPatternsPacket::handle).add();

        CHANNEL.messageBuilder(PatternspriaModePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PatternspriaModePacket::encode).decoder(PatternspriaModePacket::new).consumerMainThread(PatternspriaModePacket::handle).add();

        CHANNEL.messageBuilder(SaltemoneParticlePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SaltemoneParticlePacket::encode).decoder(SaltemoneParticlePacket::new).consumerMainThread(SaltemoneParticlePacket::handle).add();

        CHANNEL.messageBuilder(BerootCookbookScreenPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(BerootCookbookScreenPacket::encode).decoder(BerootCookbookScreenPacket::decode).consumerMainThread(BerootCookbookScreenPacket::handle).add();

        CHANNEL.messageBuilder(SyncGluedPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncGluedPacket::encode).decoder(SyncGluedPacket::new).consumerMainThread(SyncGluedPacket::handle).add();

        CHANNEL.messageBuilder(CorruptionParticlePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CorruptionParticlePacket::encode).decoder(CorruptionParticlePacket::new).consumerMainThread(CorruptionParticlePacket::handle).add();

        CHANNEL.messageBuilder(SyncSlipperyPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncSlipperyPacket::encode).decoder(SyncSlipperyPacket::new).consumerMainThread(SyncSlipperyPacket::handle).add();

    }
}
