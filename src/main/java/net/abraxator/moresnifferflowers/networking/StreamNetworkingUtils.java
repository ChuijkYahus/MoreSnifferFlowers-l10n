package net.abraxator.moresnifferflowers.networking;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class StreamNetworkingUtils {

    public static <MSG2> void registerMessage(
            SimpleChannel channel, int index, Class<MSG2> messageType,
            StreamCodec<MSG2> codec,
            BiConsumer<MSG2, Supplier<NetworkEvent.Context>> messageConsumer
    ) {
        channel.registerMessage(index, messageType, codec::encode, codec::decode, messageConsumer);
    }

}
