package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;

public class ModItemProperties {
    public static void register() {
        ItemProperties.register(ModItems.DYESPRIA.get(), MoreSnifferFlowers.loc("color"), (stack, level, entity, pSeed) -> {
            if(!Dye.getDyeFromDyespria(stack).isEmpty()) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.DRAGONFLY.get(), MoreSnifferFlowers.loc("og"), (stack, level, entity, pSeed) -> {
            Component component = stack.getDisplayName();
            String s = component.getString();
            if(s.equals("[og]")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.PATTERNSPRIA.get(), MoreSnifferFlowers.loc("patternspria"), (stack, level, entity, pSeed) -> {
            if(!BlockPattern.fromPatternspria(stack).equals(BlockPattern.EMPTY)) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }
}
