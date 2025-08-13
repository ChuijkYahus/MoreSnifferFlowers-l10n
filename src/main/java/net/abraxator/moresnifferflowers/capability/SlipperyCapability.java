package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
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
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SlipperyCapability{
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("slippery");

    public float lastYaw = 0;
    public float lastSpeed = 0;
    public boolean isFallen = false;
    public int fallenTicks = 0;
    public int maxFallenTicks = 0;

    public static final Codec<SlipperyCapability> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("lastYaw").forGetter(data -> data.lastYaw),
                    Codec.FLOAT.fieldOf("lastSpeed").forGetter(data -> data.lastSpeed),
                    Codec.BOOL.fieldOf("isFallen").forGetter(data -> data.isFallen),
                    Codec.INT.fieldOf("fallenTicks").forGetter(data -> data.fallenTicks),
                    Codec.INT.fieldOf("maxFallenTicks").forGetter(data -> data.maxFallenTicks)
            ).apply(instance, (a,b,c,d,e) -> {
                SlipperyCapability cap =  new SlipperyCapability();
                cap.lastYaw = a;
                cap.lastSpeed = b;
                cap.isFallen = c;
                cap.fallenTicks = d;
                cap.maxFallenTicks = e;
                return cap;
            }));


    public void onEffectEnd(Player player) {
        lastSpeed = 0;
        lastYaw = 0;
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

        } else if (!player.level().isClientSide && !(lastSpeed == 0 && lastYaw == 0)){

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

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ID);
        AttributeModifier mod = new AttributeModifier(ID, -100, AttributeModifier.Operation.ADD_VALUE);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(mod);

        player.level().playSound(null, player.blockPosition(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 1f, 1f);

        sync(player);
    }

    public void getUp(Player player) {
        fallenTicks = 0;
        isFallen = false;

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ID);
        player.setForcedPose(null);
       if (!player.level().isClientSide) sync(player);
    }



    public void sync(Player player){
       // PacketDistributor.sendToAllPlayers(SyncSlipperyPacket(isFallen, player.getId(), fallenTicks, maxFallenTicks));
    }

    public static SlipperyCapability get(Player player) {
       return player.getData(ModDataAttachments.SLIPPERY);
    }
}
