package net.abraxator.moresnifferflowers.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.SaltemoneBlockEntity;
import net.abraxator.moresnifferflowers.blocks.SaltemoneBlock;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.PatternDyeRenderHandler;
import net.abraxator.moresnifferflowers.client.gui.slot.HardenedMouthSlot;
import net.abraxator.moresnifferflowers.events.custom.SlotTakeEvent;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.abraxator.moresnifferflowers.nutrition.NutritionLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

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
    public static void renderLevelStage(RenderLevelStageEvent event){
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS)) {
            PoseStack poseStack = event.getPoseStack();
            double camX = event.getCamera().getPosition().x;
            double camY = event.getCamera().getPosition().y;
            double camZ = event.getCamera().getPosition().z;
            Matrix4f projectionMatrix = event.getProjectionMatrix();

            PatternDyeRenderHandler BUFFER_MANAGER = new PatternDyeRenderHandler();
            Minecraft minecraft = Minecraft.getInstance();
            Level level = minecraft.level;
            if (level == null || minecraft.player == null) return;

            poseStack.pushPose();
            poseStack.translate(-camX, -camY, -camZ);

            Matrix4f view = poseStack.last().pose();

            BUFFER_MANAGER.renderPatternOverlay(level, camX, camY, camZ, view, projectionMatrix, ClientRegistration.getClientPatternStorage());
            BUFFER_MANAGER.render(view, projectionMatrix);

            poseStack.popPose();
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
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();

        if(level.getBlockEntity(pos) instanceof GiantCropBlockEntity entity) {
            BlockPos.withinManhattanStream(entity.center, 1, 1, 1).forEach(blockPos -> {
               if (level.getBlockState(blockPos).is(ModTags.ModBlockTags.GIANT_CROPS)) level.destroyBlock(blockPos, true);
            });
        }

        if(level.getBlockEntity(pos) instanceof SaltemoneBlockEntity entity) {
            SaltemoneBlock.blockPosStream(entity.center, level.getBlockState(entity.center)).forEach(pos1 -> {
                if (level.getBlockState(pos1).is(ModBlocks.SALTEMONE.get()) || level.getBlockState(pos1).is(ModBlocks.SOURLEMONE.get())) level.destroyBlock(pos1, true);
            });
        }


        if(level.getBlockEntity(pos) instanceof BerootCauldronBlockEntity entity && level.getBlockState(entity.center).getBlock().equals(ModBlocks.BEROOT_CAULDRON.get())) {
            var entityState = level.getBlockState(entity.center);
            var entityPos = entity.center;
            Direction direction = entityState.getValue(HorizontalDirectionalBlock.FACING);
            BlockPos relative = entityPos.relative(direction).relative(direction.getClockWise()).above();
            BlockPos.betweenClosedStream(new AABB(entityPos, relative)).forEach(blockPos -> {
                if (level.getBlockState(blockPos).is(ModBlocks.BEROOT_CAULDRON.get())) level.destroyBlock(blockPos, true);
            });
        }
        
        if (level.getBlockEntity(pos) instanceof BondripiaBlockEntity entity) {
            Direction.Plane.HORIZONTAL.forEach(direction -> {
                BlockPos blockPos = entity.center.relative(direction);

                level.destroyBlock(blockPos, true);
            });
            level.destroyBlock(entity.center, true);
        }
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

        item.setDeltaMovement(newVelocity);    }
}
