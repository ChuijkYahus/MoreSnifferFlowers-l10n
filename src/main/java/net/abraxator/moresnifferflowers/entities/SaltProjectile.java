package net.abraxator.moresnifferflowers.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SaltProjectile extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(SaltProjectile.class, EntityDataSerializers.BOOLEAN);

    public SaltProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }


    public boolean isCorrupted() {
        return this.entityData.get(CORRUPTED);
    }

    public void setCorrupted(boolean corrupted) {
        this.entityData.set(CORRUPTED, corrupted);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("corrupted", this.isCorrupted());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCorrupted(tag.getBoolean("corrupted"));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CORRUPTED, false);

    }

}
