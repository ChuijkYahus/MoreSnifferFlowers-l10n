package net.abraxator.moresnifferflowers.init.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_X;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_Y;
    public static final ForgeConfigSpec.IntValue BLOCK_PATTERN_RENDER_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue BLOCK_PATTERN_OFFSET;
    public static final ForgeConfigSpec.BooleanValue BLOCK_PATTERN_SMOOTH_LIGHTING;



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
        BLOCK_PATTERN_RENDER_DISTANCE = builder
                .comment("sdff")
                .defineInRange("Block Pattern Render Distance", 100, -5, 1000);

        BLOCK_PATTERN_OFFSET = builder
                .comment("sdff")
                .defineInRange("Block Pattern Offset", 0.001, 0, 1);

        BLOCK_PATTERN_SMOOTH_LIGHTING = builder
                .comment("enables smooth lighting for block patterns")
                .define("Block Pattern Smooth Lighting", true);

        builder.pop();

        CLIENT_CONFIG = builder.build();

    }
}
