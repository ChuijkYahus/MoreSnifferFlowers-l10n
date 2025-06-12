package net.abraxator.moresnifferflowers.init.config;


import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

;

public class ModServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec.DoubleValue CORRUPTION_SPREAD_SPEED;
    public static final ForgeConfigSpec.BooleanValue CORRUPTED_TREE_GROW_THROUGH;

    public static final ForgeConfigSpec.ConfigValue<String> REBREWING_LENGTH;
    public static final ForgeConfigSpec.ConfigValue<String> REBREWING_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<String> REBREWING_SPLASH;
    public static final ForgeConfigSpec.ConfigValue<String> REBREWING_LINGERING;



    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("corruption");

        CORRUPTION_SPREAD_SPEED = builder
                .comment("Spread speed of corrupted grass blocks, 1 = Default, 0 = Disabled")
                .translation("moresnifferflowers.configuration.corruption_spread_speed")
                .defineInRange("Corruption Spread Speed", 1D, 0D, 5D);

        CORRUPTED_TREE_GROW_THROUGH = builder
                .comment("Should the corrupted tree be able to grow through and destroy blocks? Default = true")
                .translation("moresnifferflowers.configuration.corrupted_tree_grow_through")
                .define("Corrupted Tree Grow Trough", true);

        builder.pop();

        builder.push("rebrewing");

        builder.comment("Change items for rebrewing recipe, JEI needs rejoin");
        builder.translation("moresnifferflowers.configuration.rebrew_jei");

        REBREWING_LENGTH = builder
                .translation("moresnifferflowers.configuration.rebrew_length")
                .define("Rebrewing Length", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.REDSTONE)).toString());

        REBREWING_AMPLIFIER = builder
                .translation("moresnifferflowers.configuration.rebrew_amplifier")
                .define("Rebrewing Amplifier", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.GLOWSTONE_DUST)).toString());

        REBREWING_SPLASH = builder
                .translation("moresnifferflowers.configuration.rebrew_splash")
                .define("Rebrewing Splash", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.GUNPOWDER)).toString());

        REBREWING_LINGERING = builder
                .translation("moresnifferflowers.configuration.rebrew_lingering")
                .define("Rebrewing Lingering", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.DRAGON_BREATH)).toString());


        builder.pop();


        SERVER_CONFIG = builder.build();
    }

    public static Item itemFromLoc(String loc) {
       return ForgeRegistries.ITEMS.getValue(MoreSnifferFlowers.ofLoc(loc));
    }
}
