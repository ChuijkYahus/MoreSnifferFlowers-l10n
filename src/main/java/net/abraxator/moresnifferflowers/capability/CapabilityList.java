package net.abraxator.moresnifferflowers.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityList {
    public static final Capability<NutritionCapability> UNLOCKED_NUTRITIONS = CapabilityManager.get(new CapabilityToken<>() {});
    
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NutritionCapability.class);
    }

    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof LivingEntity) {
            if (event.getObject() instanceof Player player) {
                event.addCapability(NutritionCapability.ID, new ICapabilitySerializable<CompoundTag>() {
                    final LazyOptional<NutritionCapability> inst = LazyOptional.of(NutritionCapabilityHandler::new);

                    @Override
                    public CompoundTag serializeNBT() {
                        return inst.orElseThrow(NullPointerException::new).serializeNBT();
                    }

                    @Override
                    public void deserializeNBT(CompoundTag nbt) {
                        inst.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
                    }

                    @Override
                    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                        return UNLOCKED_NUTRITIONS.orEmpty(cap, inst);
                    }
                });
            }
        }
    } 
}
