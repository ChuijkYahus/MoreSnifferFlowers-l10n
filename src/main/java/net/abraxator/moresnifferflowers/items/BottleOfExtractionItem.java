package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BottleOfExtractionItem extends Item {
    public BottleOfExtractionItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if (livingEntity instanceof Player player && !level.isClientSide) {

            if (livingEntity instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
                serverplayer.awardStat(Stats.ITEM_USED.get(this));
            }

            if (player.hasEffect(ModEffects.EXTRACTED.get())) {
                doCheaterEasterEgg(level, player);
                return new ItemStack(Items.POISONOUS_POTATO);
            }

            stack = initPotion(player);
            livingEntity.removeAllEffects();
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!canExtract(level, player)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
    }

    private ItemStack initPotion(Player player) {
        ItemStack itemStack1 = ModItems.EXTRACTED_BOTTLE.get().getDefaultInstance();
        PotionUtils.setCustomEffects(itemStack1, player.getActiveEffects());
        return itemStack1;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    private boolean canExtract(Level level, Player player) {
        return !level.isClientSide && player.getActiveEffects() != null && !player.getActiveEffects().isEmpty() && !player.hasEffect(ModEffects.EXTRACTED.get());
    }

    private static void doCheaterEasterEgg(Level level, Player player) {
        player.setAbsorptionAmount(0);
        player.setHealth(0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 800, 2));
        player.setSwimming(true);
        player.setJumping(true);
        player.setXRot(0F);
        player.setYHeadRot(0f);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.0F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 1.3F);
        level.playSound(null, player, SoundEvents.ENDERMAN_SCREAM, SoundSource.PLAYERS, 1.2F, 0.8F);
        level.playSound(null, player, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.4F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatableWithFallback("tooltip.bottle_of_extraction.usage", "Drink to extract all effects into single potion").withStyle(ChatFormatting.GOLD));
    }
}
