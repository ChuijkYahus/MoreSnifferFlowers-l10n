package net.abraxator.moresnifferflowers.init.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_X;
    public static final ForgeConfigSpec.IntValue HARDENED_MOUTH_Y;
    public static final ForgeConfigSpec.IntValue BLOCK_PATTERN_RENDER_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue BLOCK_PATTERN_OFFSET;
    public static final ForgeConfigSpec.BooleanValue BLOCK_PATTERN_SMOOTH_LIGHTING;
    public static final ForgeConfigSpec.BooleanValue BLOCK_PATTERN_TRANSPARENCY;




    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("A lot of these require a game restart seemingly at random. Restart your game before reporting it as a bug!");
        builder.translation("moresnifferflowers.configuration.restart_required");

        builder.push("hardened_mouth_effect");

        HARDENED_MOUTH_X = builder
                .comment("Move extra slots from the Hardened mouth effect left to right")
                .translation("moresnifferflowers.configuration.hardened_mouth_x")
                .defineInRange("Hardened Mouth X", 176, -5000, 5000);
        HARDENED_MOUTH_Y = builder
                .comment("Move extra slots from the Hardened mouth effect up and down")
                .translation("moresnifferflowers.configuration.hardened_mouth_y")
                .defineInRange("Hardened Mouth Y", 80, -5000, 5000);

        builder.pop();

        builder.push("block_patterns");
        BLOCK_PATTERN_RENDER_DISTANCE = builder
                .comment("Input in blocks. Negative values use a division of your current render distance instead")
                .translation("moresnifferflowers.configuration.block_pattern_render_distance")
                .defineInRange("Block Pattern Render Distance", -2, -5, 1000);

        BLOCK_PATTERN_OFFSET = builder
                .comment("How far block patterns are from their block, low values Z-fight, high values look detached")
                .translation("moresnifferflowers.configuration.block_pattern_offset")
                .defineInRange("Block Pattern Offset", 0.001, 0, 0.2);

        BLOCK_PATTERN_SMOOTH_LIGHTING = builder
                .comment("Enables smooth lighting for block patterns")
                .translation("moresnifferflowers.configuration.block_pattern_smooth_lighting")
                .define("Block Pattern Smooth Lighting", true);

        BLOCK_PATTERN_TRANSPARENCY = builder
                .comment("Enables transparency for block patters (noticeable only with resource packs)")
                .translation("moresnifferflowers.configuration.block_pattern_transparency")
                .define("Block Pattern Transparency", false);

        builder.pop();

        CLIENT_CONFIG = builder.build();

    }

    public static int getBlockPatternRenderDistance() {
        Minecraft minecraft = Minecraft.getInstance();
        int renderDistancePlayer = minecraft.options.getEffectiveRenderDistance();
        int configuredRenderDistance = ModClientConfig.BLOCK_PATTERN_RENDER_DISTANCE.get();
        return configuredRenderDistance < 0 ? renderDistancePlayer*16 / Math.abs(configuredRenderDistance) : configuredRenderDistance;
    }

}
