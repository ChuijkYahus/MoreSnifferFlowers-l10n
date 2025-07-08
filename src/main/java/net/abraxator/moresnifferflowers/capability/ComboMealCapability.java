package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ComboMealCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public float speed = 1;
    public int duration = 0;
    private final LazyOptional<ComboMealCapability> optional = LazyOptional.of(() -> this);
    ResourceLocation ID = MoreSnifferFlowers.loc("combo_meal");

    public void tick(Player player) {
        if (player.hasEffect(ModMobEffects.COMBO_MEAL.get()) && player instanceof attackStrengthTickerAccessor accessor){
            if (duration <= 0){
                speed = 1;
                player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(UUID.fromString("41DD0153-E92A-B00B-9800-EFFEC53C00B0"));
            }
            if (duration > 0) {
                duration--;
            }

        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return optional.cast(); // I hope this doesnt break anything
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("speed", speed);
        tag.putInt("duration", duration);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
       speed = tag.getFloat("speed");
       duration = tag.getInt("duration");
    }

    public interface attackStrengthTickerAccessor{
        int moreSnifferFlowers$getAttackStrengthTickerAccessor();
    }
}
