package net.abraxator.moresnifferflowers.init.cofig;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_X;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_Y;
    public static final ForgeConfigSpec.IntValue DYE_PATTERN_RENDER_DISTANCE;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("hardened_mouth_effect");

        builder.comment("Hardened mouth effect GUI location, REQUIRES GAME RESTART!");
        HARDENED_MOUTH_X = builder
                .comment("Move extra slots from the Hardened mouth effect left to right")
                .translation("config.moresnifferflowers.hardened_mouth_x")
                .defineInRange("Hardened Mouth X", 176, -5000, 5000);
        HARDENED_MOUTH_Y = builder
                .comment("Move extra slots from the Hardened mouth effect up and down")
                .translation("config.moresnifferflowers.hardened_mouth_y")
                .defineInRange("Hardened Mouth Y", 80, -5000, 5000);

        builder.pop();

        builder.push("dye_patterns");
        DYE_PATTERN_RENDER_DISTANCE = builder
                .comment("sdff")
                .defineInRange("Dye Pattern Render Distance", 100, -5, 1000);

        builder.pop();

        CLIENT_CONFIG = builder.build();

    }
}
