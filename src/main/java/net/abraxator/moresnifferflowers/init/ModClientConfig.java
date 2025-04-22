package net.abraxator.moresnifferflowers.init;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ClientConfigData CLIENT;

    static {
        final Pair<ClientConfigData, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfigData::new);
        CLIENT_CONFIG = specPair.getRight();
        CLIENT = specPair.getLeft();

    }

    public static class ClientConfigData {
        public final ForgeConfigSpec.IntValue HARDENED_MOUTH_X;
        public final ForgeConfigSpec.IntValue HARDENED_MOUTH_Y;

        public ClientConfigData(ForgeConfigSpec.Builder builder) {
            builder.push("More Sniffer Flowers Client Config");
            HARDENED_MOUTH_X = builder
                    .comment("Move extra slots from the Hardened mouth effect left to right")
                    .translation("config.moresnifferflowers.hardened_mouth_x")
                    .defineInRange("Hardened Mouth X", 176, -400, 400);

            HARDENED_MOUTH_Y = builder
                    .comment("Move extra slots from the Hardened mouth effect up and down")
                    .translation("config.moresnifferflowers.hardened_mouth_y")
                    .defineInRange("Hardened Mouth Y", 80, -400, 400);

            builder.pop();
        }
    }
}
