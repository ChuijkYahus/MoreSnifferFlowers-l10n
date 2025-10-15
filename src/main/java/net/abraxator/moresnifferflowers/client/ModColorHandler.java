package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModStatePropertiesUnsafe;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModColorHandler {
    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            Colorable colorable = ((Colorable) state.getBlock());
            Dye dye = colorable.getDyeFromBlock(state);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                if (tintIndex == 0) {
                    float[] colorHSB = getColorHSB(color);

                    return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
                }
                if (tintIndex == 1) {
                    return color;
                }
            }
            return -1;
        }, ModBlocks.CAULORFLOWER.get());
        event.register((state, level, pos, tintIndex) -> {
            int color = state.getValue(ModStateProperties.BLOCK_PATTERN).getColor();
            if (state.getValue(ModStateProperties.EMPTY)) color = 0xFFFFFF;
            if (tintIndex == 0) {
                float[] colorHSB = getColorHSB(color);
                return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
            }
            if (tintIndex == 1) {
                float[] colorHSB = getColorHSB(color);
                return Color.HSBtoRGB(colorHSB[0], Math.min(colorHSB[1] * 1.1F, 1), Math.min(colorHSB[2] * 1.2F, 1));
            }

            return -1;
        }, ModBlocks.PATTERNFLOWER.get());
        event.register((state, level, pos, tintIndex) -> {
                    var colorable = ((ColorableVivicusBlock) state.getBlock());
                    if(tintIndex == 0) {
                        int dyedValue = Dye.colorForDye(colorable, state.getValue(colorable.getColorProperty()));
                        DyeColor color = colorable.getDyeFromBlock(state).color();
                        float[] colorHSB = getColorHSB(dyedValue);

                        if (Colorable.isModdedDye(color)) {
                            colorHSB[1] = colorHSB[1] / 1.5f;
                            colorHSB[2] = colorHSB[2] * 1.6f;

                            if (colorHSB[2] > 1) colorHSB[2] = 1f;
                        }

                        if(state.is(ModBlocks.VIVICUS_LEAVES.get()) || state.is(ModBlocks.VIVICUS_LEAVES_SPROUT.get())) {
                            if (pos == null) pos = new BlockPos(0,0,0);
                            float hue = colorHSB[0] + ((1+ Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 15);

                            if (colorHSB[1] < 0.3 && colorHSB[2] < 0.8){
                                colorHSB[2] = colorHSB[2] - ((1+Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 15);
                            }

                            if (colorHSB[1] < 0.3){
                                colorHSB[1] = colorHSB[1] + ((1+Mth.sin((float)pos.getX() + (float)pos.getY() + (float)pos.getZ())) / 12);
                            }


                            return Color.HSBtoRGB(hue, colorHSB[1], colorHSB[2]);
                        }

                        return Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]);
                    }

                    return -1;
                }, ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(),
                ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.VIVICUS_PLANKS.get(), ModBlocks.VIVICUS_STAIRS.get(),
                ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(),
                ModBlocks.VIVICUS_DOOR.get(), ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get(),
                ModBlocks.VIVICUS_BUTTON.get(), ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.VIVICUS_SAPLING.get(),
                ModBlocks.VIVICUS_LEAVES_SPROUT.get(), ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_HANGING_SIGN.get(),
                ModBlocks.VIVICUS_SAPLING.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            Dye dye = Dye.getDyeFromDyespria(stack);
            if(tintIndex != 0 || dye.isEmpty()) {
                return -1;
            } else {
                return Dye.colorForDye(((DyespriaItem) stack.getItem()), dye.color());
            }
        }, ModItems.DYESPRIA.get());

        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : PotionUtils.getColor(stack),
                ModItems.EXTRACTED_BOTTLE.get(), ModItems.REBREWED_POTION.get(), ModItems.REBREWED_SPLASH_POTION.get(), ModItems.REBREWED_LINGERING_POTION.get());

        event.register(((stack, tintIndex) ->{
           BlockPattern pattern = BlockPattern.fromPatternspria(stack);
           if(tintIndex != 0 || pattern == BlockPattern.EMPTY) return -1;
           if (stack.getOrCreateTag().contains("color")) {
               return stack.getOrCreateTag().getInt("color");
           }
           return pattern.getColor();
        }), ModItems.PATTERNSPRIA.get());

        event.register(((stack, tintIndex) ->{
            if (stack.getOrCreateTag().contains("color") && tintIndex != 0) {
                return stack.getOrCreateTag().getInt("color");
            }
            return 0xffffff;
        }), ModItems.ROOTED_SOUP.get());


        event.register(((stack, tintIndex) ->{
            if (stack.getOrCreateTag().contains(Colorable.TAG_HEX)) {
                int color = stack.getOrCreateTag().getInt(Colorable.TAG_HEX);
                int colorId = stack.getOrCreateTag().getInt(Colorable.TAG_ID);

                if (Colorable.isModdedDye(DyeColor.byId(colorId))) {
                    float[] colorHSB = getColorHSB(color);

                    colorHSB[1] = colorHSB[1] / 1.5f;
                    colorHSB[2] = colorHSB[2] * 1.6f;

                    if (colorHSB[2] > 1) colorHSB[2] = 1f;

                    return Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]);
                }

                return color;
            }
            return 0xffffff;
        }), ModBlocks.VIVICUS_LOG.get(),  ModBlocks.VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(),  ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.VIVICUS_PLANKS.get(),
                ModBlocks.VIVICUS_STAIRS.get(), ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(), ModBlocks.VIVICUS_DOOR.get(),
                ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get(), ModBlocks.VIVICUS_BUTTON.get(), ModBlocks.VIVICUS_LEAVES.get(),
                ModBlocks.VIVICUS_LEAVES_SPROUT.get(), ModItems.VIVICUS_SIGN.get(), ModItems.VIVICUS_HANGING_SIGN.get(), ModItems.VIVICUS_BOAT.get(), ModItems.VIVICUS_CHEST_BOAT.get());

    }

    public static float @NotNull [] getColorHSB(int originalColor) {
        int startRed = (originalColor >> 16) & 0xFF;
        int startGreen = (originalColor >> 8) & 0xFF;
        int startBlue = originalColor & 0xFF;
        return Color.RGBtoHSB(startRed, startGreen, startBlue, null);
    }

    public static float[] hexToRGBLarge(int hex) {
        return new float[] {(hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF};
    }

    public static float[] hexToRGB(int hex) {
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;
        return new float[] {r / 255f, g/ 255f, b/ 255f};
    }


    public static int RGBtoInt(Vec3 color) {
        int r = (int) color.x;
        int g = (int) color.y;
        int b = (int) color.z;

        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;

        return rgb;
    }

    public static int barColorHelper(int input, int maxInput){
        int lowColor = 0x8c1111;
        int highColor = 0x179529;

        return barColorHelper(input, maxInput, lowColor, highColor);
    }


    public static int barColorHelper(int input, int maxInput, int lowColor, int highColor){
        int lowRed = (lowColor >> 16) & 0xFF;
        int lowGreen = (lowColor >> 8) & 0xFF;
        int lowBlue = lowColor & 0xFF;

        int highRed = (highColor >> 16) & 0xFF;
        int highGreen = (highColor >> 8) & 0xFF;
        int highBlue = highColor & 0xFF;

        float[] lowHSB =  Color.RGBtoHSB(lowRed, lowGreen, lowBlue, null);
        float[] highHSB =  Color.RGBtoHSB(highRed, highGreen, highBlue, null);


        float finalHue = ((lowHSB[0] * (Math.abs(input - maxInput))) + (highHSB[0] * input)) / maxInput;
        float finalSat = ((lowHSB[1] * (Math.abs(input - maxInput))) + (highHSB[1] * input)) / maxInput;
        float finalValue = ((lowHSB[2] * (Math.abs(input - maxInput))) + (highHSB[2] * input)) / maxInput;

        return Mth.hsvToRgb(finalHue, 1.0F, 1.0F);
    }
}
