package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Bonmeelable {
    Map<Block, Block> MAP = Map.of(
            Blocks.CARROTS, ModBlocks.GIANT_CARROT.get(),
            Blocks.POTATOES, ModBlocks.GIANT_POTATO.get(),
            Blocks.NETHER_WART, ModBlocks.GIANT_NETHERWART.get(),
            Blocks.BEETROOTS, ModBlocks.GIANT_BEETROOT.get(),
            Blocks.WHEAT, ModBlocks.GIANT_WHEAT.get(),
            vectorwing.farmersdelight.common.registry.ModBlocks.ONION_CROP.get(), ModBlocks.GIANT_ONION.get(),
            vectorwing.farmersdelight.common.registry.ModBlocks.TOMATO_CROP.get(), ModBlocks.GIANT_TOMATO.get(),
            vectorwing.farmersdelight.common.registry.ModBlocks.CABBAGE_CROP.get(), ModBlocks.GIANT_CABBAGE.get()
    );
    
    void performBonmeel(BlockPos blockPos, BlockState blockState, Level level, @Nullable Player player);

    boolean canBonmeel(BlockPos blockPos, BlockState blockState, Level level);
}

