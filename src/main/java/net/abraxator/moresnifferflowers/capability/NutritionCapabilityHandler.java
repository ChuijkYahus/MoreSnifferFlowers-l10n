package net.abraxator.moresnifferflowers.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class NutritionCapabilityHandler implements NutritionCapability {
    private Set<Item> items = new HashSet<>();
    
    @Override
    public Set<Item> getItems() {
        return this.items;
    }

    @Override
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public void addItem(Item item) {
        var set = getItems();
        set.add(item);
        setItems(set);
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        
        tag.putInt("size", items.size());
        
        int i = 0;
        for (Item item : items) {
            tag.putString("unlocked" + i, ForgeRegistries.ITEMS.getKey(item).toString());
            i++;
        }
        
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        items = new HashSet<>();
        
        for (int i = 0; i < nbt.getInt("size"); i++) {
            ResourceLocation location = ResourceLocation.of(nbt.getString("unlocked" + i), ':');
            items.add(ForgeRegistries.ITEMS.getValue(location));
        }
    }
}
