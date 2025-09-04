package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class BondripiaBlockEntity extends MultiBlockEntity{
    public BondripiaBlockEntity(BlockPos pos, BlockState pBlockState) {
        super(ModBlockEntities.BONDRIPIA.get(), pos, pBlockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(center).inflate(1);
    }
}
