package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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

        if (pLivingEntity instanceof Player player && !pLevel.isClientSide) {

            if (pLivingEntity instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
                serverplayer.awardStat(Stats.ITEM_USED.get(this));
            }

            if (player.hasEffect(ModMobEffects.EXTRACTED)) {
                doCheaterEasterEgg(pLevel, player);
                return new ItemStack(Items.POISONOUS_POTATO);
            }

            pStack = initPotion(player);
            pLivingEntity.removeAllEffects();
        }
        return pStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!canExtract(pLevel, pPlayer)) {
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        } else {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }
    }

    private ItemStack initPotion(Player player) {
        var stack = ModItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
        var effects = player.getActiveEffects();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(PotionContents.getColor(effects)), new ArrayList<>(effects)));
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    private boolean canExtract(Level level, Player player) {
        return !level.isClientSide && player.getActiveEffects() != null && !player.getActiveEffects().isEmpty() && !player.hasEffect(ModMobEffects.EXTRACTED);
    }

    private static void doCheaterEasterEgg(Level pLevel, Player player) {
        player.setAbsorptionAmount(0);
        player.setHealth(0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 800, 2));
        player.setSwimming(true);
        player.setJumping(true);
        player.setXRot(0F);
        player.setYHeadRot(0f);
        pLevel.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.0F);
        pLevel.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        pLevel.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.3F);
        pLevel.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        pLevel.playSound(null, player, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.4F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatableWithFallback("tooltip.bottle_of_extraction.usage", "Drink to extract all effects into single potion").withStyle(ChatFormatting.GOLD));
    }
}
