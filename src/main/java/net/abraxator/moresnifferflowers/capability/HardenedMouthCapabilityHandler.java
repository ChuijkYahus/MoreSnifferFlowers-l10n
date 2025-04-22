package net.abraxator.moresnifferflowers.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class HardenedMouthCapabilityHandler implements HardenedMouthCapability{
    public static int SLOT_COUNT = 1;
    NonNullList<ItemStack> mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getMouthSlots() {
        return null;
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
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();

        ContainerHelper.saveAllItems(tag, mouthSlots);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(nbt, mouthSlots);

    }
}
