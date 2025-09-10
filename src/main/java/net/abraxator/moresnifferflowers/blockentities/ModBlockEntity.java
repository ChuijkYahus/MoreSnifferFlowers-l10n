package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ModBlockEntity extends BlockEntity {
    public ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState pBlockState) {
        super(type, pos, pBlockState);
    }

    public void tick(Level level) {}
    
    public void clientTick(ClientLevel level) {}
}
