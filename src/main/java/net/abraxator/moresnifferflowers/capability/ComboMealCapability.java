package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class ComboMealCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public float speed = 1;
    public int duration = 0;
    private final LazyOptional<ComboMealCapability> optional = LazyOptional.of(() -> this);
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("combo_meal");

    public void onEffectEnd(Player player) {
        speed = 1;
        duration = 0;
    }

    public void onAttack(Player player, boolean isCharged) {
        int amplifier = Objects.requireNonNull(player.getEffect(ModEffects.COMBO_MEAL.get())).getAmplifier();

        if (isCharged){
            speed *= 1 + (amplifier / 4f + 1) / 10f;
            duration = (int) (150 / (speed * 2));

            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(UUID.fromString("41DD0153-E92A-B00B-9800-EFFEC53C00B0"));
            AttributeModifier mod = new AttributeModifier(UUID.fromString("41DD0153-E92A-B00B-9800-EFFEC53C00B0"), "combo_meal", speed - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
            player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(mod);

        } else {

            duration /= 2;

        }
    }

    public void tick(Player player) {
        if (duration <= 0){
            speed = 1;
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(UUID.fromString("41DD0153-E92A-B00B-9800-EFFEC53C00B0"));
        }
        if (duration > 0) {
            duration--;
        }

    }

    public void debugPrint(){
        System.out.println("speed: " + speed + " duration: " + duration);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.COMBO_MEAL.orEmpty(cap, optional.cast());
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
}
