package net.abraxator.moresnifferflowers.entities;

import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.SaltemoneParticlePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3f;

public class SaltBubbleProjectile extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(SaltBubbleProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(SaltBubbleProjectile.class, EntityDataSerializers.INT); // 0 = flying, 1 = expanding, 2 = popping
    private Vector3f pos;
    private int time;
    private float height;
    private float slowdown;
    private int maxTime;

    public SaltBubbleProjectile(EntityType<? extends SaltBubbleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SaltBubbleProjectile(double x, double y, double z, Level level) {
        super(ModEntityTypes.SALT_BUBBLE.get(), x, y, z , level);
        this.pos = new Vector3f((float) x, (float) y, (float) z);
        this.height = level.random.nextIntBetweenInclusive(1, 6) + level.random.nextFloat();
        this.slowdown = 1.0f + 0.14f / height;
        this.maxTime = level.random.nextIntBetweenInclusive(1000, 1500);

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        pop();
        return true;
    }

    @Override
    public void tick() {
        Vec3 speed = getDeltaMovement();
        int state = getState();

        if (pos != null && state == 0) {
            if (this.pos.distance(this.position().toVector3f()) > height || speed.distanceTo(new Vec3(0,0,0)) < 0.05) {
                setState(1);
                setDeltaMovement(0, 0, 0);
            } else {
                setDeltaMovement(speed.x / slowdown, speed.y / slowdown, speed.z / slowdown);

            }

        }

        if (pos != null) {
            time++;
            if (time >= maxTime) {
                pop();
                return;
            }
        }

        super.tick();
    }

    private void pop() {
        int projectiles = random.nextFloat() < 0.25f ? 2 : 1;
        setState(2);
        for (int i = 0; i < projectiles; i++) {
            SaltProjectile projectile = new SaltProjectile(level());
            projectile.moveTo(getX(), getY(), getZ());
            projectile.shoot(random.nextFloat() - 0.5, random.nextFloat() + 0.5, random.nextFloat() - 0.5, 0.5F, 1.0F);
            projectile.setCorrupted(isCorrupted());
            level().addFreshEntity(projectile);
            level().playSound(null, blockPosition(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 10.0F + random.nextFloat() * 3, 0.75f + random.nextFloat() / 2);
            ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SaltemoneParticlePacket(this.position().toVector3f()));
        }
        discard();
    }

    @Override
    protected void updateRotation() {
        Vec3 vec3 = new Vec3(0,0,0);
        double d0 = vec3.horizontalDistance();
        this.setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI))));
        this.setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI))));

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
