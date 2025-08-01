package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DebugFlowerItem extends Item {
    public DebugFlowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(ModBlocks.CORRUPTED_GRASS_BLOCK.get())){
            CorruptionCapability.printDebug(level.getChunkAt(pos));
        }
        return super.useOn(context);
    }
}
