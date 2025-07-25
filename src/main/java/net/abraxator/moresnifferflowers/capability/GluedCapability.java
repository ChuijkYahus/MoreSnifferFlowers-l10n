package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.UpdateGluedPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GluedCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public boolean isGlued;
    private final LazyOptional<GluedCapability> optional = LazyOptional.of(() -> this);
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("is_glued");

    public static void setAndSync(LivingEntity entity, boolean isGlued){
        Level level = entity.level();
        if (level.isClientSide) return;

        playSound(level, entity);
        entity.getCapability(CapabilityList.GLUED).ifPresent(cap -> {
            cap.isGlued = isGlued;
            cap.sync(entity);
        });
    }

    public void sync(LivingEntity entity){
        sync(entity, isGlued);
    }

    public static void sync(LivingEntity entity, boolean isGlued) {
        ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(),new UpdateGluedPacket(isGlued, entity.getId()));
    }

    public static void playSound(Level level, Entity entity){
        level.playSound(null, entity.getOnPos(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.PLAYERS, 5.0F, 0.02F + level.random.nextFloat() * 0.01F);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.GLUED.orEmpty(cap, optional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isGlued", isGlued);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
      isGlued = tag.getBoolean("isGlued");
    }
}
