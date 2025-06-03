package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

public class BondripiaBlockEntity extends ModBlockEntity implements MultiBlockEntity{
    public BlockPos center;
    
    public BondripiaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BONDRIPIA.get(), pPos, pBlockState);
        this.center = this.getBlockPos();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("center", NbtUtils.writeBlockPos(this.center));
    }
    
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.center = NbtUtils.readBlockPos(pTag.getCompound("center"));
    }

    @Override
    public BlockPos getCenter() {
        return center;
    }

    @Override
    public void setCenter(BlockPos pos) {
        center = pos;
    }
}
