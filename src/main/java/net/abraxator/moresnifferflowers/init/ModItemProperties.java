package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ModItemProperties {
    public static void register() {
        ItemProperties.register(ModItems.DYESPRIA.get(), MoreSnifferFlowers.loc("color"), (pStack, pLevel, pEntity, pSeed) -> {
            if(!Dye.getDyeFromDyespria(pStack).isEmpty()) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.DRAGONFLY.get(), MoreSnifferFlowers.loc("og"), (pStack, pLevel, pEntity, pSeed) -> {
            Component component = pStack.get(DataComponents.CUSTOM_NAME);
            if(component != null && component.getString().equals("og")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.PATTERNSPRIA.get(), MoreSnifferFlowers.loc("patternspria"), (pStack, pLevel, pEntity, pSeed) -> {
            if(!BlockPattern.fromPatternspria(pStack).equals(BlockPattern.EMPTY)) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }
}
