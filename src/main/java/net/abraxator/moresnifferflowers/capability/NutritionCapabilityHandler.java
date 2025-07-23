package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.UpdateNutritionPacket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class NutritionCapabilityHandler implements NutritionCapability {
    private Set<Item> items = new HashSet<>();
    private final LazyOptional<NutritionCapability> optional = LazyOptional.of(() -> this);

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
    public void sync(Player player) {
        if (!player.level().isClientSide) {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new UpdateNutritionPacket(this.items)
            );
        }
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.UNLOCKED_NUTRITIONS.orEmpty(cap, optional.cast());
    }
}
