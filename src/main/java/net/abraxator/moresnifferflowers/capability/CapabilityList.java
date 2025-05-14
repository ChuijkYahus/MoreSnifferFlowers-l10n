package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityList {
    public static final Capability<NutritionCapability> UNLOCKED_NUTRITIONS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<HardenedMouthCapability> MOUTH_SLOTS = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NutritionCapability.class);
        event.register(HardenedMouthCapability.class);
    }

    @SubscribeEvent
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
                event.addCapability(HardenedMouthCapability.ID, new ICapabilitySerializable<CompoundTag>() {
                    final LazyOptional<HardenedMouthCapability> inst = LazyOptional.of(HardenedMouthCapabilityHandler::new);

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
                        return MOUTH_SLOTS.orEmpty(cap, inst);
                    }
                });
            }
        }
    }
    private static final BlockPatternCapability BLOCK_PATTERN_CAPABILITY = new BlockPatternCapability();

    public static BlockPatternCapability getBlockPatterns() {
        return BLOCK_PATTERN_CAPABILITY;
    }

}
