package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GluingGumEntity extends Entity {

    public GluingGumEntity(Level level) {
        super(ModEntityTypes.GLUING_GUM_ENTITY.get(), level);
    }

    public GluingGumEntity(EntityType<? extends GluingGumEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
