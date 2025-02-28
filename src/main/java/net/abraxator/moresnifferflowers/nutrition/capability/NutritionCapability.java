package net.abraxator.moresnifferflowers.nutrition.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

@AutoRegisterCapability
public interface NutritionCapability extends INBTSerializable<CompoundTag> {
    ResourceLocation ID = MoreSnifferFlowers.loc("unlocked_nutrition");
    
    List<Item> getItems();
    
    void setItems(List<Item> items);
}
