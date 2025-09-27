package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

public class SaltemoneBlockEntity extends AbstractMultiBlockEntity {
    public SaltemoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SALTEMONE.get(), pos, state);
    }
}
