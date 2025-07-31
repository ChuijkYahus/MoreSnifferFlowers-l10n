package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.GluedCapability;
import net.abraxator.moresnifferflowers.capability.UntouchableCapability;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new NutritionLoader());
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event){
        MobEffect effect = Objects.requireNonNull(event.getEffectInstance()).getEffect();
        LivingEntity entity = event.getEntity();

        if (effect.equals(ModEffects.GLUED.get())){
            GluedCapability.setAndSync(entity, true, true);
        }
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event){
        MobEffect effect = Objects.requireNonNull(event.getEffectInstance()).getEffect();
        LivingEntity entity = event.getEntity();

        onEffectEnd(effect, entity);
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event){
        MobEffect effect = Objects.requireNonNull(event.getEffectInstance()).getEffect();
        LivingEntity entity = event.getEntity();

        onEffectEnd(effect, entity);
    }

    public static void onEffectEnd(MobEffect effect, LivingEntity entity) {

        if (entity instanceof Player player) {
            if (effect.equals(ModEffects.HARDENED_MOUTH.get()))
                player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> cap.onEffectEnd(player));

            if (effect.equals(ModEffects.SLIPPERY.get()))
                player.getCapability(CapabilityList.SLIPPERY).ifPresent(cap -> cap.onEffectEnd(player));

            if (effect.equals(ModEffects.COMBO_MEAL.get()))
                player.getCapability(CapabilityList.COMBO_MEAL).ifPresent(cap -> cap.onEffectEnd(player));

            if (effect.equals(ModEffects.UNTOUCHABLE.get()))
                player.getCapability(CapabilityList.UNTOUCHABLE).ifPresent(cap -> cap.onEffectEnd(player));

        }

        if (effect.equals(ModEffects.GLUED.get()))
            GluedCapability.setAndSync(entity, false, true);


    }

    @SubscribeEvent
    public static void itemEntity(ItemEvent event){
        ItemEntity itemEntity = event.getEntity();
        ItemStack item = itemEntity.getItem();

       if (item.is(ModItems.BURNED_SLOT.get())){
           itemEntity.discard();
       }

    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        boolean isCharged = player.getAttackStrengthScale(0.5f) > 0.9f;
        Entity entity = event.getTarget();
        Level level = player.level();

        if (player.hasEffect(ModEffects.COMBO_MEAL.get()) && stack.is(ItemTags.TOOLS))
            player.getCapability(CapabilityList.COMBO_MEAL).ifPresent(cap -> cap.onAttack(player, isCharged));


        if(player.hasEffect(ModEffects.GLUING_TOUCH.get()) && isCharged && entity instanceof LivingEntity livingEntity && !level.isClientSide) {
            int amplifier = Objects.requireNonNull(player.getEffect(ModEffects.GLUING_TOUCH.get())).getAmplifier();

            if (level.random.nextFloat() < ((amplifier + 2) / 12f)) {
                livingEntity.addEffect(new MobEffectInstance(ModEffects.GLUED.get(), (5 + amplifier*2) * 20, 0));
            }

        }
    }

    @SubscribeEvent
    public static void onAttacked(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModEffects.UNTOUCHABLE.get())){
            player.getCapability(CapabilityList.UNTOUCHABLE).ifPresent(UntouchableCapability::onAttacked);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();

        // if (event.phase == TickEvent.Phase.END || event.player.level().isClientSide) return;

    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        ItemEntity itemEntity = event.getItem();

        if (player.hasEffect(ModEffects.STICKY.get())) {
           if (!player.isCrouching()) {
               event.setCanceled(true);
           } else {
               int amplifier = player.getEffect(ModEffects.STICKY.get()).getAmplifier();
               int slowdown = 5 + amplifier*2;

               if (event.getEntity().level().getGameTime() % slowdown != 0) {
                   event.setCanceled(true);
                   return;
               }
                ItemStack stack = itemEntity.getItem();
                ItemStack retStack = stack.split(1);

                player.addItem(retStack);

                itemEntity.setItem(stack);
                event.setCanceled(true);
           }

        }
    }
    
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        Vec3 loc = livingEntity.position();
        BlockPos blockPos = BlockPos.containing(loc);
        
        if(level.getBlockState(blockPos).is(ModBlocks.CORRUPTED_SLIME_LAYER.get()) || level.getBlockState(blockPos.below()).is(ModBlocks.CORRUPTED_SLIME_LAYER.get())) {
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().multiply(1, 0.3, 1));
        }

    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
       BlockState state = event.getPlacedBlock();
       LevelAccessor badLevel = event.getLevel();

       if (state.is(ModTags.ModBlockTags.CORRUPTION_SHIELDING) && badLevel instanceof Level level){
           LevelChunk chunk = level.getChunkAt(event.getPos());
           chunk.getCapability(CapabilityList.CORRUPTION).ifPresent(cap ->{
               cap.resistance++;
               cap.isSource = false;
               cap.flowers.add(event.getPos());
           });
       }
    }


    @SubscribeEvent
    public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemStack = event.getItemStack();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack item = player.getItemInHand(hand).getItem().getDefaultInstance();

        {
            //This is the DEBUG BLOCK, remember to delete everything from the DEBUG BLOCK later

        }

        if (event.isCanceled()) return;

        if(item.getItem() instanceof JarOfBonmeelItem jar && state.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            event.setCancellationResult(jar.useOn(new UseOnContext(player, hand, event.getHitVec())));

        }

        if((item.is(ModItems.REBREWED_POTION.get()) || item.is(ModItems.EXTRACTED_BOTTLE.get())) && state.is(Blocks.DIRT)) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);

        }

        if(item.is(ItemTags.AXES) && (state.is(ModBlocks.VIVICUS_LOG.get()) || state.is(ModBlocks.VIVICUS_WOOD.get()))) {
            var strippedBlock = AxeItem.STRIPPABLES.get(state.getBlock());
            var state1 = strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))
                    .setValue(ModStateProperties.COLOR, state.getValue(ModStateProperties.COLOR));

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, item);
            }
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state1, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state1));
            itemStack.hurtAndBreak(1, player, player1 -> {
                player1.broadcastBreakEvent(player1.getUsedItemHand());
            });

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);

        }

        if (item.is(ModItems.JAR_OF_BONMEEL.get()) || item.is(ModItems.JAR_OF_ACID.get()) && state.is(Blocks.CAULDRON)){
            var cauldronType = item.is(ModItems.JAR_OF_BONMEEL.get()) ? ModBlocks.BONMEEL_FILLED_CAULDRON.get() :  ModBlocks.ACID_FILLED_CAULDRON.get();
            var state1 = cauldronType.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
            level.setBlock(pos, state1, 3);
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);

            if (!player.isCreative()) player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);

        }

        if (BlockPatternCapability.hasPattern(pos, level) && itemStack.is(Items.GLOW_INK_SAC)){
            BlockPatternCapability.PatternData data = BlockPatternCapability.getPattern(pos, level);
            if (!data.isGlowing()){
                BlockPatternCapability.enableGlowing(level, pos);
                if (!player.isCreative()) itemStack.shrink(1);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }

        }

        if (itemStack.is(Items.FLINT_AND_STEEL) && state.is(Blocks.TORCHFLOWER)){
            itemStack.hurtAndBreak(1, player, player1 -> {});
            player.setItemInHand(hand, itemStack);
            level.setBlock(pos, ModBlocks.TORCHFLOWER_AFLAME.get().defaultBlockState().setValue(ModStateProperties.AGE_2, 1), 3);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);

        }


    }



}
