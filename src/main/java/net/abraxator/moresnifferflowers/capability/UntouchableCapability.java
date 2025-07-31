package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UntouchableCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<UntouchableCapability> optional = LazyOptional.of(() -> this);
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("untouchable");

    public static final UUID UUID_SPEED = java.util.UUID.fromString("41DD0153-E92A-B00B-A550-EFFEC535FEED");
    public static final UUID UUID_RESISTANCE = java.util.UUID.fromString("41DD0153-BE51-B00B-A550-EFFEC535FEED");

    public double lastX = 0;
    public double lastZ = 0;
    public int lastSpeed = 0;
    public float speedModifier = 0;

    public void onAttacked(){
        speedModifier = speedModifier * 0.7f;
    }

    public void onEffectEnd(Player player){
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID_SPEED);
        player.getAttribute(Attributes.ARMOR).removeModifier(UUID_RESISTANCE);
        speedModifier = 0;
    }

    public void tick(Player player, int amplifier){
        Vec3 position = player.position();
        double posX = position.x;
        double posZ = position.z;

        if (lastX == 0 && lastZ == 0 && lastSpeed == 0){
            lastX = posX;
            lastZ = posZ;

            return;
        }

        float speedAccumulation = 0.00005f;
        float maxSpeed = 150 + amplifier*15;
        speedAccumulation += (speedAccumulation / 3) * amplifier;

        double speedX = Math.abs(posX - lastX);
        double speedZ = Math.abs(posZ - lastZ);
        int speed = Math.round((float) (speedX * speedX + speedZ * speedZ) * 1000);

        float armorModifier = Mth.floor(speedModifier * 400);
        if (armorModifier < 0) armorModifier = 0;

        if (player.isSprinting() ){

           if (speed <= maxSpeed) speedModifier += speedAccumulation;

        } else {
            if (speedModifier > 0) speedModifier -= speedAccumulation * 15;
        }

        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(UUID_SPEED);
        player.getAttribute(Attributes.ARMOR).removeModifier(UUID_RESISTANCE);

        AttributeModifier mod = new AttributeModifier(UUID_SPEED, "untouchable_speed", speedModifier, AttributeModifier.Operation.ADDITION);
        AttributeModifier mod1 = new AttributeModifier(UUID_RESISTANCE, "untouchable_resistance", armorModifier, AttributeModifier.Operation.ADDITION);

        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(mod);
        player.getAttribute(Attributes.ARMOR).addTransientModifier(mod1);

        lastSpeed = speed;
        lastX = posX;
        lastZ = posZ;
    }

    public void debugPrint(){
        System.out.println("Speed Modifier = " + speedModifier +" lastSpeed =" + lastSpeed);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.UNTOUCHABLE.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("lastX", lastX);
        tag.putDouble("lastZ", lastZ);
        tag.putInt("speed", lastSpeed);
        tag.putFloat("speedMod", speedModifier);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        lastX = nbt.getDouble("lastX");
        lastZ = nbt.getDouble("lastZ");
        lastSpeed = nbt.getInt("speed");
        speedModifier = nbt.getFloat("speedMod");
    }
}
