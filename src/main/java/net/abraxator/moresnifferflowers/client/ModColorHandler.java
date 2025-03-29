package net.abraxator.moresnifferflowers.client;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModColorHandler {
    @SubscribeEvent
    public static void onRegisterBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if(tintIndex == 0 && state.getValue(ModStateProperties.FULLNESS) > 0) {
                return state.getValue(ModStateProperties.CROP).tint;
            }

            return -1;
        }, ModBlocks.CROPRESSOR_CENTER.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
            Colorable colorable = ((Colorable) pState.getBlock());
            Dye dye = colorable.getDyeFromBlock(pState);
            int color = Dye.colorForDye(colorable, dye.color());
            if(!dye.isEmpty()) {
                if (pTintIndex == 0) {
                    int startRed = (color >> 16) & 0xFF;
                    int startGreen = (color >> 8) & 0xFF;
                    int startBlue = color & 0xFF;
                    float[] colorHSB = Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                    return Color.HSBtoRGB(colorHSB[0], Math.max(colorHSB[1] / 1.7F, 0), Math.max(colorHSB[2], 0));
                }
                if (pTintIndex == 1) {
                    int startRed = (color >> 16) & 0xFF;
                    int startGreen = (color >> 8) & 0xFF;
                    int startBlue = color & 0xFF;
                    float[] colorHSB = Color.RGBtoHSB(startRed, startGreen, startBlue, null);
                    colorHSB[0] += ((float) Minecraft.getInstance().level.getGameTime() / 255);

                    return Color.HSBtoRGB(colorHSB[0], colorHSB[1], colorHSB[2]);

                }
            }
            return -1;
        }, ModBlocks.CAULORFLOWER.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
                    var colorable = ((ColorableVivicusBlock) pState.getBlock());
                    if(pTintIndex == 0) {
                        var dyedValue = Dye.colorForDye(colorable, pState.getValue(colorable.getColorProperty()));
                        var color = colorable.getDyeFromBlock(pState).color();

                        if(pState.is(ModBlocks.VIVICUS_LEAVES.get()) || pState.is(ModBlocks.VIVICUS_LEAVES_SPROUT.get()) ||
                                pState.is(ModBlocks.VIVICUS_LOG.get()) || pState.is(ModBlocks.VIVICUS_WOOD.get()) || pState.is(ModBlocks.STRIPPED_VIVICUS_LOG.get()) || pState.is(ModBlocks.STRIPPED_VIVICUS_LOG.get()) ||
                                pState.is(ModBlocks.STRIPPED_VIVICUS_WOOD.get()) || pState.is(ModBlocks.VIVICUS_PLANKS.get()) || pState.is(ModBlocks.VIVICUS_STAIRS.get()) ||
                                pState.is(ModBlocks.VIVICUS_SLAB.get()) || pState.is(ModBlocks.VIVICUS_FENCE.get()) || pState.is(ModBlocks.VIVICUS_FENCE_GATE.get()) ||
                                pState.is(ModBlocks.VIVICUS_DOOR.get()) || pState.is(ModBlocks.VIVICUS_TRAPDOOR.get()) || pState.is(ModBlocks.VIVICUS_PRESSURE_PLATE.get()) ||
                                pState.is(ModBlocks.VIVICUS_BUTTON.get()) || pState.is(ModBlocks.VIVICUS_LEAVES.get()) || pState.is(ModBlocks.VIVICUS_SAPLING.get()) ||
                                pState.is(ModBlocks.VIVICUS_LEAVES_SPROUT.get()) || pState.is(ModBlocks.VIVICUS_SIGN.get()) || pState.is(ModBlocks.VIVICUS_HANGING_SIGN.get())) {

                            assert pPos != null;
                           // float hue = colorHSB[0] + ((1+ Mth.sin((float)pPos.getX() + (float)pPos.getY() + (float)pPos.getZ())) / 15);
                            String hex = new String();
                            int finalColor = 0;


                            //gay
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.CYAN)){
                                switch (Math.abs(pPos.getY() % 7)){
                                    case 0 -> hex = "402B76";
                                    case 1 -> hex = "5455A5";
                                    case 2 -> hex = "7EACDD";
                                    case 3 -> hex = "F2F1F9";
                                    case 4 -> hex = "A1D7BD";
                                    case 5 -> hex = "49BEA3";
                                    case 6 -> hex = "048D73";
                                }
                            }

                            //lesbian
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.ORANGE)){
                                switch (Math.abs(pPos.getY() % 5)){
                                    case 0 -> hex = "A41E63";
                                    case 1 -> hex = "D461A4";
                                    case 2 -> hex = "FFFFFF";
                                    case 3 -> hex = "F99756";
                                    case 4 -> hex = "D43027";
                                }
                            }

                            //bi
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.PURPLE)){
                                switch (Math.abs(pPos.getY() % 5)){
                                    case 0, 1 -> hex = "D81770";
                                    case 2 -> hex = "734F95";
                                    case 3, 4 -> hex = "1E439C";
                                }
                            }

                            //trans
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.PINK) || colorable.getDyeFromBlock(pState).color().equals(DyeColor.LIGHT_BLUE)){
                                switch (Math.abs(pPos.getY() % 5)){
                                    case 0, 4 -> hex = "62CCF6";
                                    case 1, 3 -> hex = "F9A8B9";
                                    case 2 -> hex = "FFFFFF";
                                }
                            }

                            //nonbin
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.ORANGE)){
                                switch (Math.abs(pPos.getY() % 5)){
                                    case 0 -> hex = "A41E63";
                                    case 1 -> hex = "D461A4";
                                    case 2 -> hex = "FFFFFF";
                                    case 3 -> hex = "F99756";
                                    case 4 -> hex = "D43027";
                                }
                            }

                            //ace
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.GRAY) || colorable.getDyeFromBlock(pState).color().equals(DyeColor.BLACK)){
                                switch (Math.abs(pPos.getY() % 4)){
                                    case 0 -> hex = "7C267D";
                                    case 1 -> hex = "FFFFFF";
                                    case 2 -> hex = "A2A2A2";
                                    case 3 -> hex = "010101";
                                }
                            }

                            //aromantic
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.GREEN) || colorable.getDyeFromBlock(pState).color().equals(DyeColor.LIGHT_GRAY)){
                                switch (Math.abs(pPos.getY() % 5)){
                                    case 0 -> hex = "010101";
                                    case 1 -> hex = "ABABA9";
                                    case 2 -> hex = "FFFFFF";
                                    case 3 -> hex = "B1D185";
                                    case 4 -> hex = "5AA24E";
                                }
                            }

                            //pan
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.YELLOW)){
                                switch (Math.abs(pPos.getY() % 3)){
                                    case 0 -> hex = "4CAAE0";
                                    case 1 -> hex = "FCD800";
                                    case 2 -> hex = "ED2F87";

                                }
                            }

                            //French
                            if (colorable.getDyeFromBlock(pState).color().equals(DyeColor.BLUE)){
                                switch (Math.abs((pPos.getZ() + pPos.getX()) % 6)){
                                    case 0,1 -> hex = "CE1126";
                                    case 2,3 -> hex = "FFFFFF";
                                    case 4,5 -> hex = "002654";

                                }
                            }

                            //lgbt
                            if (hex.isBlank()){
                                switch (Math.abs(pPos.getY() % 6)){
                                    case 0 -> hex = "732A84";
                                    case 1 -> hex = "2A348B";
                                    case 2 -> hex = "2EB34A";
                                    case 3 -> hex = "E0E21C";
                                    case 4 -> hex = "EF9D20";
                                    case 5 -> hex = "D42127";

                                }
                            }


                            if (!hex.isBlank()) finalColor = HexToAssRGB(hex);

                            return finalColor;
                        }

                        return dyedValue;
                    }

                    return -1;
                }, ModBlocks.VIVICUS_LOG.get(), ModBlocks.VIVICUS_WOOD.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(), ModBlocks.STRIPPED_VIVICUS_LOG.get(),
                ModBlocks.STRIPPED_VIVICUS_WOOD.get(), ModBlocks.VIVICUS_PLANKS.get(), ModBlocks.VIVICUS_STAIRS.get(),
                ModBlocks.VIVICUS_SLAB.get(), ModBlocks.VIVICUS_FENCE.get(), ModBlocks.VIVICUS_FENCE_GATE.get(),
                ModBlocks.VIVICUS_DOOR.get(), ModBlocks.VIVICUS_TRAPDOOR.get(), ModBlocks.VIVICUS_PRESSURE_PLATE.get(),
                ModBlocks.VIVICUS_BUTTON.get(), ModBlocks.VIVICUS_LEAVES.get(), ModBlocks.VIVICUS_SAPLING.get(),
                ModBlocks.VIVICUS_LEAVES_SPROUT.get(), ModBlocks.VIVICUS_SIGN.get(), ModBlocks.VIVICUS_HANGING_SIGN.get());
        event.register((pState, pLevel, pPos, pTintIndex) -> {
            Colorable colorable = ((Colorable) pState.getBlock());
            Dye dye = colorable.getDyeFromBlock(pState);
            int color = Dye.colorForDye(colorable, dye.color());

            if (pState.is(ModBlocks.MORE_SNIFFER_FLOWER_UPPER.get())){
                //  lower = 1 upper = 2 leaves = 3
                if (pTintIndex == 3){
                    return color;
                }
                Colorable colorableLow = colorable;
                BlockState stateLow = pLevel.getBlockState(pPos.below());
                if (stateLow.is(ModBlocks.MORE_SNIFFER_FLOWER_LOWER.get())) colorableLow = ((Colorable) stateLow.getBlock());
                Dye dyeLow = colorable.getDyeFromBlock(stateLow);
                int colorLow = Dye.colorForDye(colorableLow, dyeLow.color());


                int startRed = (color >> 16) & 0xFF;
                int startGreen = (color >> 8) & 0xFF;
                int startBlue = color & 0xFF;
                float[] colorHSB = Color.RGBtoHSB(startRed, startGreen, startBlue, null);

                int startRedLow = (colorLow >> 16) & 0xFF;
                int startGreenLow = (colorLow >> 8) & 0xFF;
                int startBlueLow = colorLow & 0xFF;
                float[] colorHSBLow = Color.RGBtoHSB(startRedLow, startGreenLow, startBlueLow, null);

                float finalHue = colorHSBLow[0] + colorHSB[0] -0.5F;

                return Color.HSBtoRGB(finalHue, Math.max(colorHSB[1] * 1.7F, 0.5F), Math.max(colorHSB[2], 0));

            }

            if (pState.is(ModBlocks.MORE_SNIFFER_FLOWER_LOWER.get())){

                return color;
            }

            return -1;
        }, ModBlocks.MORE_SNIFFER_FLOWER_LOWER.get(), ModBlocks.MORE_SNIFFER_FLOWER_UPPER.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> {
            Dye dye = Dye.getDyeFromDyespria(pStack);
            if(pTintIndex != 0 || dye.isEmpty()) {
                return -1;
            } else {
                return Dye.colorForDye(((DyespriaItem) pStack.getItem()), dye.color());
            }
        }, ModItems.DYESPRIA.get());
        event.register((pStack, pTintIndex) -> {
            return pTintIndex > 0 ? -1 : PotionUtils.getColor(pStack);
        }, ModItems.EXTRACTED_BOTTLE.get(), ModItems.REBREWED_POTION.get(), ModItems.REBREWED_SPLASH_POTION.get(), ModItems.REBREWED_LINGERING_POTION.get());
    }

    public static float[] hexToRGB(int hex) {
        return new float[]{(hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF};
    }

    public static int[] smartHexToRGB(String hex) {
        int[] rgb = new int[3];
        rgb[0] = Integer.valueOf(hex.substring(0, 2), 16); // Red
        rgb[1] = Integer.valueOf(hex.substring(2, 4), 16); // Green
        rgb[2] = Integer.valueOf(hex.substring(4, 6), 16); // Blue
        return rgb;
    }

    public static int HexToAssRGB(String hex){
        int[] rgb = smartHexToRGB(hex);

        float[] hsb = Color.RGBtoHSB(rgb[0],rgb[1],rgb[2], null);
        hsb[1] = Mth.clamp(hsb[1], 0F, 0.65F);
        hsb[2] = Mth.clamp(hsb[2], 0.2F, 1F);
        int rgb1 = Color.HSBtoRGB(hsb[0],hsb[1],hsb[2]);

        int ass = rgb[0];
        ass = (ass << 8) + rgb[1];
        ass = (ass << 8) + rgb[2];

        return rgb1;
    }
}
