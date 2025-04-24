package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface HardenedMouthCapability extends INBTSerializable<CompoundTag> {
     ResourceLocation ID = MoreSnifferFlowers.loc("mouth_inventory");

     NonNullList<ItemStack> getMouthSlotItems();

     void setAllItems(NonNullList<ItemStack> itemStacks);

     void setItem(int index, ItemStack stack);

     ItemStack getItem(int index);

     void sync(Player player);

     void clear();
}
