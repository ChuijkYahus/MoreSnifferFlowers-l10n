package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.core.BlockPos;

public interface MultiBlockEntity {
    BlockPos getCenter();
    void setCenter(BlockPos pos);
}
