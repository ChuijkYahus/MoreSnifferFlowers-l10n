package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BottleOfExtractionItem extends Item {
    public BottleOfExtractionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        var ret = pStack;
        
        if (!(pLivingEntity instanceof Player player) && pLevel.isClientSide) {
            return pStack;
        }
        
        if (pLivingEntity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }
        
        ret = initPotion(((Player) pLivingEntity));
        pLivingEntity.removeAllEffects();
        return ret;
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!canExtract(pLevel, pPlayer)) {
            return InteractionResult.PASS;
        } else {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResult.CONSUME;
        }
    }

    private ItemStack initPotion(Player player) {
        var stack = ModItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
        var effects = player.getActiveEffects();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(PotionContents.getColorOptional(effects).orElse(14058905)), new ArrayList<>(effects), Optional.of("mixed")));
        return stack;
    }
    
    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }

    private boolean canExtract(Level level, Player player) {
        return !level.isClientSide && player.getActiveEffects() != null && !player.getActiveEffects().isEmpty() && !player.hasEffect(ModMobEffects.EXTRACTED);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatableWithFallback("tooltip.bottle_of_extraction.usage", "Drink to extract all effects into single potion").withStyle(ChatFormatting.GOLD));
    }
}
