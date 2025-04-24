package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.UpdateMouthSlotsPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class HardenedMouthCapabilityHandler implements HardenedMouthCapability{
    public static int SLOT_COUNT = 2;
    NonNullList<ItemStack> mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    @Override
    public NonNullList<ItemStack> getMouthSlotItems() {
        return mouthSlots;
    }

    @Override
    public void setAllItems(NonNullList<ItemStack> itemStacks) {
        mouthSlots = itemStacks;
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
        if (!player.level().isClientSide) {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new UpdateMouthSlotsPacket(mouthSlots)
            );
        }
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
