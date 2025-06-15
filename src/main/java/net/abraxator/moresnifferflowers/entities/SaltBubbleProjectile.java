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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SaltBubbleProjectile extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(SaltBubbleProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(SaltBubbleProjectile.class, EntityDataSerializers.INT); // 0 = flying, 1 = expanding, 2 = popping
    private Vector3f pos;
    private int time;
    private float height;
    private float slowdown;

    public SaltBubbleProjectile(EntityType<? extends SaltBubbleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SaltBubbleProjectile(double x, double y, double z, Level level) {
        super(ModEntityTypes.SALT_BUBBLE.get(), x, y, z , level);
        this.pos = new Vector3f((float) x, (float) y, (float) z);
        this.height = level.random.nextIntBetweenInclusive(1, 4) + level.random.nextFloat();
        this.slowdown = 1.0f + 0.18f / height;

    }

    @Override
    public void tick() {
        Vec3 speed = getDeltaMovement();

        if (pos != null && getState() == 0) {
            if (this.pos.distance(this.position().toVector3f()) > height || speed.distanceTo(new Vec3(0,0,0)) < 0.05) {
                setState(1);
                setDeltaMovement(0, 0, 0);
            } else {
                setDeltaMovement(speed.x / slowdown, speed.y / slowdown, speed.z / slowdown);

            }

        }

        time++;
        if (time >= 200) { setState(1); }
        if (time >= 350) { discard(); }

        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CORRUPTED, false);
        this.entityData.define(STATE, 0);

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

    public void setState(int state) {
        this.entityData.set(STATE, state);
    }

    public int getState() {
        return this.entityData.get(STATE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("corrupted", this.isCorrupted());
        tag.putInt("state", this.getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setCorrupted(tag.getBoolean("corrupted"));
        this.setState(tag.getInt("state"));

    }
}
