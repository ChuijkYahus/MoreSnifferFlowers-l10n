package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

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
        CHANNEL.registerMessage(id++, CorruptedSludgePacket.class, CorruptedSludgePacket::encode, CorruptedSludgePacket::new, CorruptedSludgePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DyespriaDisplayModeChangePacket.class, DyespriaDisplayModeChangePacket::encode, DyespriaDisplayModeChangePacket::new, DyespriaDisplayModeChangePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DyespriaModePacket.class, DyespriaModePacket::encode, DyespriaModePacket::new, DyespriaModePacket::handle);
        CHANNEL.messageBuilder(PlaySoundPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder((msg, buf) -> {
                    buf.writeResourceLocation(ForgeRegistries.SOUND_EVENTS.getKey(msg.sound())); // SoundEvent
                    buf.writeDouble(msg.x());
                    buf.writeDouble(msg.y());
                    buf.writeDouble(msg.z());
                })
                .decoder(buf -> new PlaySoundPacket(
                        ForgeRegistries.SOUND_EVENTS.getValue(buf.readResourceLocation()), // Decode SoundEvent
                        buf.readDouble(), buf.readDouble(), buf.readDouble()
                ))
                .consumerMainThread(PlaySoundPacket::handle)
                .add();
    }
}
