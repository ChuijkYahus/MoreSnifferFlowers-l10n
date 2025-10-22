package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.NotNull;

public class SaltemoneBlockEntity extends AbstractMultiBlockEntity {
    public SaltemoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SALTEMONE.get(), pos, state);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox() {
        return new AABB(getCenter()).inflate(1);
    }
}
