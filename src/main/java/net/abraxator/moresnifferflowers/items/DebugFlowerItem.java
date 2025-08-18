package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

        if (level.isClientSide()) System.out.println("Below this is CLIENT:");
        if (!level.isClientSide()) System.out.println("Below this is SERVER:");

        if (blockState.is(ModBlocks.CORRUPTED_GRASS_BLOCK.get())){
            CorruptionCapability.printDebug(level.getChunkAt(pos));
        }

        if (blockState.is(Blocks.GRASS_BLOCK)){
            System.out.println("BlockPatterns = " + BlockPatternCapability.getBlockPatterns(pos, level).getPatterns());
            level.getChunkAt(pos).getData(ModDataAttachments.BLOCK_PATTERNS).sync(pos, level);
        }
        return super.useOn(context);
    }
}
