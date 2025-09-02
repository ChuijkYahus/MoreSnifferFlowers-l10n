package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CropressedCropBlockEntity extends ModBlockEntity implements FakeRenderBlockEntity {
    boolean isFake = false;
    public CropressedCropBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CROPRESSED_CROP.get(), pos, state);
    }

    @Override
    public void setFake() {
        isFake = true;
    }

    @Override
    public boolean isFake() {
        return isFake;
    }
}
