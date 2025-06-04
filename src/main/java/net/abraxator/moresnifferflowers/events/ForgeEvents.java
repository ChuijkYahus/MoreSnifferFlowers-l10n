package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blocks.MultiBlock;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.gui.slot.HardenedMouthSlot;
import net.abraxator.moresnifferflowers.events.custom.SlotTakeEvent;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new NutritionLoader());
    }

    @SubscribeEvent
    public static void onSlotTake(SlotTakeEvent event){
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkDataEvent.Load event) {
        ChunkPos pos = event.getChunk().getPos();
        CompoundTag tag = event.getData().getCompound("patternStorage");
        ChunkAccess chunkAccess = event.getChunk();
        if (tag.isEmpty()) {
            return;
        }

        if(chunkAccess instanceof LevelChunk chunk) {
            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> {
                blockPatternCapability.load(tag);
            });
        }
    }

    @SubscribeEvent
    public static void onChunkSave(ChunkDataEvent.Save event) {
        CompoundTag tag = event.getData().getCompound("PatternStorage");
        ChunkPos pos = event.getChunk().getPos();
        if (event.getLevel() instanceof ServerLevel level) {
            LevelChunk chunk = level.getChunkSource().getChunk(pos.x, pos.z, false);

            if (chunk == null) {
                return;
            }

            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> {
                blockPatternCapability.save(tag);
                event.getData().put("patternStorage", tag);
            });
        }
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event) {
        Player player = event.getPlayer();
        if (!event.getLevel().isClientSide){
            LevelChunk chunk = event.getChunk();
            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.sync(chunk.getPos()));
        }

    }


    @SubscribeEvent
    public static void onEffectExpiration(MobEffectEvent event){
        MobEffectInstance effectInstance = event.getEffectInstance();
        LivingEntity entity = event.getEntity();
        if (effectInstance.getEffect().equals(ModMobEffects.HARDENED_MOUTH.get()) && entity instanceof Player player){
            player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(hardenedMouthCapability -> {
                hardenedMouthCapability.getMouthSlotItems().forEach(itemStack -> {
                    if (HardenedMouthSlot.moveToPlayerInventory(player.inventoryMenu, itemStack)) return;
                    if (itemStack.isEmpty()) return;
                    player.drop(itemStack, true);
                });
                hardenedMouthCapability.clear();
            });
        };
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
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (player.hasEffect(ModMobEffects.STICKY.get())) {
            double pullRange = 8.0D;
            List<ItemEntity> nearbyItems = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(pullRange), item -> !item.hasPickUpDelay());
            for (ItemEntity item : nearbyItems) {
                pullItemTowardPlayer(player, item);
            }
        }

        if (event.phase == TickEvent.Phase.END && player.hasEffect(ModMobEffects.OLD_NEGATIVE_SWEET.get())) {
            MobEffectInstance instance = player.getEffect(ModMobEffects.OLD_NEGATIVE_SWEET.get());
            int amplifier = instance.getAmplifier();
            if(player.getRandom().nextDouble() > 0.01 * amplifier) {
                return;
            }
            if(player.getRandom().nextBoolean()) {
                Vec3 oldMovement = player.getDeltaMovement();
                Vec3 laggyMovement = new Vec3(-oldMovement.x * (0.5 * amplifier), oldMovement.y, -oldMovement.z * (0.8 * amplifier));
                player.setDeltaMovement(laggyMovement);
            } else {
                Vec3 jitter = new Vec3(
                        (Math.random() - 0.5) * (0.2 * amplifier),
                        0,
                        (Math.random() - 0.5) * (0.2 * amplifier)
                );
                player.setDeltaMovement(player.getDeltaMovement().add(jitter));
            }
        }

        if (event.phase == TickEvent.Phase.END || event.player.level().isClientSide) return;

        event.player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
            cap.tick(event.player);
        });

    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModMobEffects.STICKY.get()) && !player.isCrouching()) {
            event.setCanceled(true);
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
    public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemStack = event.getItemStack();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        var state = level.getBlockState(pos);
        var item = player.getItemInHand(hand).getItem().getDefaultInstance();


        if (event.isCanceled()) return;
        if(item.getItem() instanceof JarOfBonmeelItem item2 && state.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            event.setCancellationResult(item2.useOn(new UseOnContext(player, hand, event.getHitVec())));

        } else
            if((item.is(ModItems.REBREWED_POTION.get()) || item.is(ModItems.EXTRACTED_BOTTLE.get())) && state.is(Blocks.DIRT)) {
            event.setCanceled(true);
        } else {
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

                } else
                if (((item.is(ModItems.JAR_OF_BONMEEL.get()) || item.is(ModItems.JAR_OF_ACID.get())) && state.is(Blocks.CAULDRON))){
                    var cauldronType = item.is(ModItems.JAR_OF_BONMEEL.get()) ? ModBlocks.BONMEEL_FILLED_CAULDRON.get() :  ModBlocks.ACID_FILLED_CAULDRON.get();
                    var state1 = cauldronType.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
                    level.setBlock(pos, state1, 3);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    if (!player.isCreative()) player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
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

        if (state.is(Blocks.TORCHFLOWER) && item.is(Items.FLINT_AND_STEEL)){
            level.setBlock(pos, ModBlocks.TORCHFLOWER_AFLAME.get().defaultBlockState(), 3);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }



    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor levelAccessor = event.getLevel();
        BlockEntity blockEntity = levelAccessor.getBlockEntity(pos);
        Level level = event.getPlayer().level();
        BlockState state = event.getState();

     //   blockBreakEventWithoutPlayer(pos, levelAccessor);
    }

    public static boolean blockBreakEventWithoutPlayer(BlockPos pos, LevelAccessor levelAccessor) {
        BlockEntity blockEntity = levelAccessor.getBlockEntity(pos);
        Level level = (Level) levelAccessor;
        BlockState blockState = level.getBlockState(pos);

        if (blockState.getBlock() instanceof MultiBlock multiBlock){
            multiBlock.destroy(multiBlock.getCenter(level, pos), level, blockState);
        }

        if (BlockPatternCapability.hasPattern(pos, level)) {
            BlockPatternCapability.removePattern(pos, level);
            return true;
        }

        return false;
    }

    private static void pullItemTowardPlayer(Player player, ItemEntity item) {
        Vec3 playerPos = player.position().add(0, 1, 0); // Aim for player's chest, not feet
        Vec3 itemPos = item.position();
        Vec3 direction = playerPos.subtract(itemPos);


        // Don't do anything if very close already
        if (direction.lengthSqr() < 0.5) {
            return;
        }

        direction = direction.normalize().scale(0.05); // SMALL pull per tick

        Vec3 currentVelocity = item.getDeltaMovement();

        // Slightly steer the velocity toward the player
        Vec3 newVelocity = currentVelocity.add(direction).scale(0.95); // Dampen a bit

        item.setDeltaMovement(newVelocity);
    }
}
