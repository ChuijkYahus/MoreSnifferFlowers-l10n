package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.init.ModEffects;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.UpdateMouthSlotsPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HardenedMouthCapabilityHandler implements HardenedMouthCapability, ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static int SLOT_COUNT = 2;
    NonNullList<ItemStack> mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    public int cooldown = 0;
    private final LazyOptional<HardenedMouthCapabilityHandler> optional = LazyOptional.of(() -> this);

    @Override
    public NonNullList<ItemStack> getMouthSlotItems() {
        return mouthSlots;
    }

    @Override
    public void setAllItems(NonNullList<ItemStack> itemStacks) {
        for (int i = 0; i < itemStacks.size(); i++) {
            mouthSlots.set(i, itemStacks.get(i));
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        mouthSlots.set(index, stack);
    }

    @Override
    public ItemStack getItem(int index) {
        return mouthSlots.get(index);
    }

    @Override
    public void clear() {
        mouthSlots.clear();
    }

    @Override
    public void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new UpdateMouthSlotsPacket(mouthSlots, cooldown)
            );
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();

        tag.putInt("cooldown", cooldown);
        ContainerHelper.saveAllItems(tag, mouthSlots);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        cooldown = nbt.getInt("cooldown");

        ContainerHelper.loadAllItems(nbt, mouthSlots);

    }


    public void tick(Player player) {
        if (!player.hasEffect(ModEffects.HARDENED_MOUTH.get()) && !isEmpty()){
            mouthSlots.forEach(itemStack -> {
                player.drop(itemStack, true);
            });
            clear();
            sync(player);
        }
        if (player.level().isClientSide || !player.hasEffect(ModEffects.HARDENED_MOUTH.get())) return;

        ItemStack input = this.getItem(0);
        ItemStack output = this.getItem(1);


        if (getSmeltingResult(player.level(), input).isEmpty() || (!getSmeltingResult(player.level(), input).get().is(output.getItem()) && !output.isEmpty())) cooldown = getMaxCooldown(player);

        if (cooldown > 0) {
            cooldown--;
            sync(player);
            return;
        }

        getSmeltingResult(player.level(), input).ifPresentOrElse(result -> {

            if (output.isEmpty() || (ItemStack.isSameItemSameTags(output, result) && output.getCount() < output.getMaxStackSize())) {
                input.shrink(1);

                if (output.isEmpty()) {
                    this.setItem(1, result.copy());
                } else {
                    output.grow(1);
                    this.setItem(1, output);
                }

                this.setItem(0, input);

                cooldown = getMaxCooldown(player);
            }
        }, () -> cooldown = getMaxCooldown(player));

        sync(player);
    }

    @Override
    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public int getMaxCooldown(Player player) {
        int amplifier = 0;
        if (player.hasEffect(ModEffects.HARDENED_MOUTH.get())) amplifier = player.getEffect(ModEffects.HARDENED_MOUTH.get()).getAmplifier();

        return Math.max(1, 80 - amplifier * 10);
    }

    public Optional<ItemStack> getSmeltingResult(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(input), level)
                .map(recipe -> recipe.getResultItem(level.registryAccess()));
    }

    public boolean isEmpty(){
        return getItem(0).isEmpty() && getItem(1).isEmpty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.MOUTH_SLOTS.orEmpty(cap, optional.cast());
    }
}
