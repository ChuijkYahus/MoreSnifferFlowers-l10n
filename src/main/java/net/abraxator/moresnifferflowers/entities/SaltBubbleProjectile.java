package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SaltBubbleProjectile extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(SaltBubbleProjectile.class, EntityDataSerializers.BOOLEAN);
    public SaltBubbleProjectile(EntityType<? extends SaltBubbleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SaltBubbleProjectile(double x, double y, double z, Level level) {
        super(ModEntityTypes.SALT_BUBBLE.get(), x, y, z , level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CORRUPTED, false);
    }


    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        discard();
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
        tag.putBoolean("Corrupted", this.isCorrupted());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCorrupted(tag.getBoolean("Corrupted"));
    }
}
