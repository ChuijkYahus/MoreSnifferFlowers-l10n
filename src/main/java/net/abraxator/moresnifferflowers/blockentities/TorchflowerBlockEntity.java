package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TorchflowerBlockEntity extends ModBlockEntity{
    public TorchflowerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TORCHFLOWER.get(), pPos, pBlockState);
    }

    @Override
    public void tick(Level level) {
        AABB area = new AABB(getBlockPos().below().east(), getBlockPos().above(4).south());
        for (Entity entity : level.getEntities(null, area)) {
            int distance = getBlockPos().getY() + 4 - entity.getBlockY();
            entity.setSecondsOnFire(distance*2);
        }
    }
}
