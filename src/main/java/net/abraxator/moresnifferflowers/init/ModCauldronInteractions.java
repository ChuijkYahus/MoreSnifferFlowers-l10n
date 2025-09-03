package net.abraxator.moresnifferflowers.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class ModCauldronInteractions {
    public static final Map<Item, CauldronInteraction> BONMEEL = CauldronInteraction.newInteractionMap();
    public static final Map<Item, CauldronInteraction> ACID = CauldronInteraction.newInteractionMap();
    public static final CauldronInteraction FILL_JAR_OF_BONMEEL = (state, level, pos, player, hand, stack) ->
            emptyBottle(level, pos,  player, hand, stack, ModBlocks.BONMEEL_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
    public static final CauldronInteraction FILL_JAR_OF_ACID = (state, level, pos, player, hand, stack) ->
            emptyBottle(level, pos,  player, hand, stack, ModBlocks.ACID_FILLED_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
    public static final CauldronInteraction EMPTY_JAR_OF_BONMEEL = (state, level, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, ModItems.JAR_OF_BONMEEL.get().getDefaultInstance(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL);
    public static final CauldronInteraction EMPTY_JAR_OF_ACID = (state, level, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, ModItems.JAR_OF_ACID.get().getDefaultInstance(), blockState -> blockState.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BOTTLE_FILL);


    static InteractionResult emptyBottle(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack, BlockState state) {
        if (level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) < 3) {
            if (!level.isClientSide) {
                Item item = filledStack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(filledStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.FILL_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(pos, state);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    public static void bootstrap() {
        BONMEEL.put(Items.GLASS_BOTTLE, EMPTY_JAR_OF_BONMEEL);
        BONMEEL.put(ModItems.JAR_OF_BONMEEL.get(), FILL_JAR_OF_BONMEEL);

        ACID.put(Items.GLASS_BOTTLE, EMPTY_JAR_OF_ACID);
        ACID.put(ModItems.JAR_OF_ACID.get(), FILL_JAR_OF_ACID);
    }
}
