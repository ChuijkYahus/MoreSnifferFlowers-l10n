package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.SyncSlipperyPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SlipperyCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final UUID ATTRIBUTE_ID = UUID.fromString("41DD0153-E92A-B00B-9800-EFFEC5511BB1");
    private final LazyOptional<SlipperyCapability> optional = LazyOptional.of(() -> this);
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("slippery");
    public float lastYaw;
    public float lastSpeed;
    public boolean isFallen;
    public int fallenTicks;
    public int maxFallenTicks;


    public void onEffectEnd(Player player) {
        if (isFallen) getUp(player);
    }

    public void tick(Player player, int amplifier) {
        float yaw = player.getYRot();
        Vec3 motion = player.getDeltaMovement();
        float speed = (float) (motion.x + motion.y + motion.z);

        if (isFallen){
            fallenTicks--;
            player.setJumping(false);
            player.setDeltaMovement(motion.x, Math.min(motion.y, 0), motion.z);

            if (!player.getPose().equals(Pose.SWIMMING)) player.setForcedPose(Pose.SWIMMING);

            if (fallenTicks <= 0){
                getUp(player);
            }

        } else if (!player.level().isClientSide){

            boolean speedChange = Math.abs(speed - lastSpeed) > 0.60f; // this only works for falling down for some reason
            float rotationLimit = Math.max(90f - amplifier*10, 15f);
            boolean rotationChange = Math.abs(Mth.wrapDegrees(yaw - lastYaw)) > rotationLimit && player.isSprinting();

            if (player.onGround() && (speedChange || rotationChange)) {
                fallDown(player, amplifier);
            }
        }

        lastSpeed = speed;
        lastYaw = yaw;
    }



    public void fallDown(Player player, int amplifier) {
        isFallen = true;
        maxFallenTicks = 30 + amplifier * 10;
        fallenTicks = maxFallenTicks;

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ATTRIBUTE_ID);
        AttributeModifier mod = new AttributeModifier(ATTRIBUTE_ID, "slippery", -100, AttributeModifier.Operation.ADDITION);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(mod);

        player.level().playSound(null, player.blockPosition(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1f, 1f);

        sync(player);
    }

    public void getUp(Player player) {
        fallenTicks = 0;
        isFallen = false;

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ATTRIBUTE_ID);
        player.setForcedPose(null);
       if (!player.level().isClientSide) sync(player);
    }



    public void sync(Player player){
        ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(),new SyncSlipperyPacket(isFallen, player.getId(), fallenTicks, maxFallenTicks));
    }

    public static SlipperyCapability get(Player player) {
       return player.getCapability(CapabilityList.SLIPPERY).orElseThrow(IllegalStateException::new);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.SLIPPERY.orEmpty(cap, optional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("lastYaw", lastYaw);
        tag.putFloat("lastSpeed", lastSpeed);
        tag.putBoolean("isFallen", isFallen);
        tag.putInt("fallenTicks", fallenTicks);
        tag.putInt("maxFallenTicks", maxFallenTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        lastYaw = nbt.getFloat("lastYaw");
        lastSpeed = nbt.getFloat("lastSpeed");
        isFallen = nbt.getBoolean("isFallen");
        fallenTicks = nbt.getInt("fallenTicks");
        maxFallenTicks = nbt.getInt("maxFallenTicks");
    }
}
