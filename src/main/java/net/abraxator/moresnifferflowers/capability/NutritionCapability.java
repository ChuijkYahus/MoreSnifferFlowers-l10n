package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.toClient.SyncGluedPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NutritionCapability {
    public Set<Item> items = new HashSet<>();

    public static final Codec<Set<Item>> ITEM_SET_CODEC =
            BuiltInRegistries.ITEM.byNameCodec()
                    .listOf()
                    .xmap(HashSet::new, ArrayList::new);

    public static final Codec<NutritionCapability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ITEM_SET_CODEC.fieldOf("items").forGetter(cap -> cap.items))
            .apply(instance, (set) -> {
                NutritionCapability capability  = new NutritionCapability();
                capability.items = set;
                return capability;
            }));



    public Set<Item> getItems() {
        return this.items;
    }

    
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    
    public void addItem(Item item) {
        var set = getItems();
        set.add(item);
        setItems(set);
    }

    
    public void sync(Player player) {
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();

        tag.putInt("size", items.size());

        int i = 0;
        for (Item item : items) {
            tag.putString("unlocked" + i, BuiltInRegistries.ITEM.getKey(item).toString());
            i++;
        }

        return tag;
    }

    
    public void deserializeNBT(CompoundTag nbt) {
        items = new HashSet<>();
        
        for (int i = 0; i < nbt.getInt("size"); i++) {
            ResourceLocation location = MoreSnifferFlowers.ofLoc(nbt.getString("unlocked" + i));
            items.add(BuiltInRegistries.ITEM.get(location));
        }
    }

}
