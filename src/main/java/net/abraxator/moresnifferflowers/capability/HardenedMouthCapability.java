package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public interface HardenedMouthCapability extends INBTSerializable<CompoundTag>, ICapabilityProvider {
     ResourceLocation ID = MoreSnifferFlowers.loc("mouth_inventory");

     int cooldown = 0;

     default NonNullList<ItemStack> getMouthSlotItems() {
       return NonNullList.withSize(2, ItemStack.EMPTY);
    }

     void setAllItems(NonNullList<ItemStack> itemStacks);

     void setItem(int index, ItemStack stack);

     ItemStack getItem(int index);

     void sync(Player player);

     void clear();

     void tick(Player player);

     int getCooldown();

     void onEffectEnd(Player player);

     void setCooldown(int cooldown);

     int getMaxCooldown(Player player);
}
