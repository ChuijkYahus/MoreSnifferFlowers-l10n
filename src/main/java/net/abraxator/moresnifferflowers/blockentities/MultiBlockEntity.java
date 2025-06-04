package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MultiBlockEntity extends ModBlockEntity {
    public BlockPos center;
    public boolean isPlaced; //True once the whole placing logic runs (to prevent updateShape from breaking it early)

    public MultiBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.center = this.getBlockPos();
        this.isPlaced = false;
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
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.center.getCenter(), 3, 3, 3);
    }

    public BlockPos getCenter() {
        return center;
    }

    public void setCenter(BlockPos pos) {
        center = pos;
    }

    public void setPlaced() {
        isPlaced = true;
    }

    public static void setPlaced(LevelReader level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof MultiBlockEntity entity) entity.setPlaced();
    }
}
