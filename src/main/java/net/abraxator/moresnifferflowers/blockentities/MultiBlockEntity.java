package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.components.PreviewMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class MultiBlockEntity extends ModBlockEntity {
    public BlockPos center;
    public boolean isPlaced; //True once the whole placing logic runs (to prevent updateShape from breaking it early)
    public PreviewMode previewMode = PreviewMode.PLACED;

    public MultiBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState pBlockState) {
        super(type, pos, pBlockState);
        this.center = this.getBlockPos();
        this.isPlaced = false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("center", NbtUtils.writeBlockPos(this.center));
        tag.putBoolean("placed", this.isPlaced);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.center = NbtUtils.readBlockPos(tag.getCompound("center"));
        this.isPlaced = tag.getBoolean("placed");
    }

    @Override
    public AABB getRenderBoundingBox() {
        return AABB.ofSize(this.center.getCenter(), 3, 3, 3);
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public void setCenter(BlockPos pos) {
        this.center = pos;
    }

    public void setPlaced() {
        this.isPlaced = true;
    }

    public boolean canRender(){
        return center.equals(this.getBlockPos()) || !previewMode.equals(PreviewMode.PLACED);
    }

    public static void setPlaced(LevelReader level, BlockPos blockPos) {
        if(level.getBlockEntity(blockPos) instanceof MultiBlockEntity entity) entity.setPlaced();
    }

    public boolean isCenter(){
        return this.center.equals(getBlockPos());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


}
