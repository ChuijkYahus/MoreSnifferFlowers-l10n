package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.init.*;
import net.abraxator.moresnifferflowers.items.JarOfBonmeelItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
    public static void onUseItemOnBlock(UseItemOnBlockEvent event) {
        var itemStack = event.getPlayer().getItemInHand(event.getHand()).getItem().getDefaultInstance();
        var block = event.getLevel().getBlockState(event.getPos());

        if(itemStack.getItem() instanceof JarOfBonmeelItem && block.is(ModTags.ModBlockTags.BONMEELABLE)) {
            event.setCanceled(true);
            ((JarOfBonmeelItem) itemStack.getItem()).useOn(event.getUseOnContext());
        } else if((itemStack.is(ModItems.REBREWED_POTION) || itemStack.is(ModItems.EXTRACTED_BOTTLE)) && block.is(Blocks.DIRT) && event.getUsePhase() == UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) {
            event.setCanceled(true);
        } else if(itemStack.is(ItemTags.AXES) && (block.is(ModBlocks.VIVICUS_LOG) || block.is(ModBlocks.VIVICUS_WOOD))) {
            var strippedBlock = AxeItem.STRIPPABLES.get(block.getBlock());
            var state = strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, block.getValue(RotatedPillarBlock.AXIS))
                    .setValue(ModStateProperties.COLOR, block.getValue(ModStateProperties.COLOR));

            if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, event.getPos(), itemStack);
            }

            event.getLevel().setBlock(event.getPos(), state, 3);
            event.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, event.getPos(), GameEvent.Context.of(event.getPlayer(), state));
            itemStack.hurtAndBreak(1, event.getPlayer(), LivingEntity.getSlotForHand(event.getHand()));

            event.cancelWithResult(ItemInteractionResult.sidedSuccess(event.getLevel().isClientSide));
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
