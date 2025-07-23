package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.UpdateIsGluedPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
        if (entity.level().isClientSide) return;
        entity.getCapability(CapabilityList.GLUED).ifPresent(cap -> {
            cap.isGlued = isGlued;
            cap.sync(entity);
        });
    }

    public void sync(LivingEntity entity){
        sync(entity, isGlued);
    }

    public static void sync(LivingEntity entity, boolean isGlued) {
        if (!(entity instanceof ServerPlayer player)) {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new UpdateIsGluedPacket(isGlued, entity.getId())
            );
        } else {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) entity),
                    new UpdateIsGluedPacket(isGlued, entity.getId())
            );
        }
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
