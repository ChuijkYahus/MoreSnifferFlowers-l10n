package net.abraxator.moresnifferflowers.nutrition.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NutritionCapabilityHandler implements NutritionCapability {
    public static final Capability<NutritionCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken() {});
    
    private Set<Item> items;;
    
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
    
    public static void attachCapibility(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player) {
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
                    return NutritionCapabilityHandler.CAPABILITY.orEmpty(cap, inst);
                }
            });
        }
    } 
}
