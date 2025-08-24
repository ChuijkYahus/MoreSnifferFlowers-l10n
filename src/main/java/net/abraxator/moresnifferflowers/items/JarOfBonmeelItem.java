package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.blocks.Bonmeelable;
import net.abraxator.moresnifferflowers.blocks.GiantCropBlock;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public class JarOfBonmeelItem extends Item {
    public JarOfBonmeelItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = pContext.getLevel().getBlockState(blockPos);
        Player player = pContext.getPlayer();
    
        if(blockState.is(ModTags.ModBlockTags.BONMEELABLE)) {
            Bonmeelable bonmeelable = (Bonmeelable) GiantCropBlock.getCropMap().get(blockState.getBlock()).getA();
            if(bonmeelable.canBonmeel(blockPos,blockState,level) && player != null) {
                bonmeelable.performBonmeel(blockPos, blockState, level, player);
                if (!player.isCreative()) player.setItemInHand(pContext.getHand(), ItemUtils.createFilledResult(player.getItemInHand(pContext.getHand()), player, new ItemStack(Items.GLASS_BOTTLE)));

                return InteractionResult.SUCCESS;

            }

        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        Component component = Component.translatableWithFallback("tooltip.jar_of_bonmeel.usage", "Can be applied to a 3x3 grid of the following crops: carrot, potato, wheat, beetroot and nether wart").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(component.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> pTooltipComponents.add(Component.literal(s).withStyle(ChatFormatting.GOLD)));

    }
}
