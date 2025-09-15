package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BondripiaBlockEntity extends AbstractMultiBlockEntity {
    public BondripiaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BONDRIPIA.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(center).inflate(1);
    }
}
