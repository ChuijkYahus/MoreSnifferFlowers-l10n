package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEvents {
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Level level = livingEntity.level();
        Vec3 loc = livingEntity.position();
        BlockPos blockPos = BlockPos.containing(loc);
        
        if(level.getBlockState(blockPos).is(ModBlocks.CORRUPTED_SLIME_LAYER) || level.getBlockState(blockPos.below()).is(ModBlocks.CORRUPTED_SLIME_LAYER)) {
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().multiply(1, 0.3, 1));
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickItem(UseItemOnBlockEvent event) {
        var item = event.getPlayer().getItemInHand(event.getHand()).getItem().getDefaultInstance();
        var block = event.getLevel().getBlockState(event.getPos());

        if (event.isCanceled()) return;
        if(item.getItem() instanceof JarOfBonmeelItem item2 && block.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            if (item2.useOn(event.getUseOnContext()).equals(InteractionResult.SUCCESS))event.setCancellationResult(ItemInteractionResult.SUCCESS);

        } else
            if((item.is(ModItems.REBREWED_POTION.get()) || item.is(ModItems.EXTRACTED_BOTTLE.get())) && block.is(Blocks.DIRT)) {
            event.setCanceled(true);
        } else
            if(item.is(ItemTags.AXES) && (block.is(ModBlocks.VIVICUS_LOG.get()) || block.is(ModBlocks.VIVICUS_WOOD.get()))) {
            var strippedBlock = AxeItem.STRIPPABLES.get(block.getBlock());
            var state = strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, block.getValue(RotatedPillarBlock.AXIS))
                    .setValue(ModStateProperties.COLOR, block.getValue(ModStateProperties.COLOR));

            if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, event.getPos(), item);
            }

            event.getLevel().playSound(event.getPlayer(), event.getPos(), SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

            event.getLevel().setBlock(event.getPos(), state, 3);
            event.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(event.getPlayer(), state));

            event.getItemStack().hurtAndBreak(1, event.getPlayer(), LivingEntity.getSlotForHand(event.getHand()));


            event.setCancellationResult(ItemInteractionResult.SUCCESS);
            event.setCanceled(true);

            } else
            if (((item.is(ModItems.JAR_OF_BONMEEL.get()) || item.is(ModItems.JAR_OF_ACID.get())) && block.is(Blocks.CAULDRON))){
                var cauldronType = item.is(ModItems.JAR_OF_BONMEEL.get()) ? ModBlocks.BONMEEL_FILLED_CAULDRON.get() :  ModBlocks.ACID_FILLED_CAULDRON.get();
                var state = cauldronType.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
                event.getLevel().setBlock(event.getPos(), state, 3);
                event.getLevel().playSound(null, event.getPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                event.getLevel().gameEvent(null, GameEvent.FLUID_PLACE, event.getPos());
                if (!event.getPlayer().isCreative()) event.getPlayer().setItemInHand(event.getHand(), ItemUtils.createFilledResult(event.getItemStack(), event.getPlayer(), new ItemStack(Items.GLASS_BOTTLE)));
                event.setCancellationResult(ItemInteractionResult.SUCCESS);
                event.setCanceled(true);
            }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

        if(event.getLevel().getBlockEntity(event.getPos()) instanceof GiantCropBlockEntity entity) {
            BlockPos.withinManhattanStream(entity.center, 1, 1, 1).forEach(blockPos -> {
               if (event.getLevel().getBlockState(blockPos).is(ModTags.ModBlockTags.GIANT_CROPS)) event.getLevel().destroyBlock(blockPos, true);
            });
        }
        
        if (event.getLevel().getBlockEntity(event.getPos()) instanceof BondripiaBlockEntity entity) {
            Direction.Plane.HORIZONTAL.forEach(direction -> {
                BlockPos blockPos = entity.center.relative(direction);

                event.getLevel().destroyBlock(blockPos, true);
            });
            event.getLevel().destroyBlock(entity.center, true);
        }
    }
}
