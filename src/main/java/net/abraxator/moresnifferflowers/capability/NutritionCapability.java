package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.SyncNutritionPacket;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NutritionCapability implements INBTSerializable<CompoundTag>, ICapabilityProvider {
    static ResourceLocation ID = MoreSnifferFlowers.loc("unlocked_nutrition");
    private Set<Item> items = new HashSet<>();
    public Set<Integer> unlockedEffects = new HashSet<>();
    private final LazyOptional<NutritionCapability> optional = LazyOptional.of(() -> this);

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
        if (!player.level().isClientSide) {
            ModPacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new SyncNutritionPacket(this.items, this.unlockedEffects)
            );
        }
    }

    public static MobEffect effectFromId(int id){
        if (id % 2 == 0){
           return NutritionType.getEffect(NutritionType.byId(id / 2), false);
        }
        return NutritionType.getEffect(NutritionType.byId(Mth.floor(id / 2f)), true);
    }

    public static int idFromNutrition(NutritionType type, boolean isPositive){
        int id = type.ordinal() * 2;
        if (isPositive) id++;
        return id;
    }

    public static final Map<Integer, ResourceLocation> ICON_FROM_ID = Map.of(
            0, MoreSnifferFlowers.loc("textures/mob_effect/slippery.png"),
            1, MoreSnifferFlowers.loc("textures/mob_effect/untouchable.png"),
            2, MoreSnifferFlowers.loc("textures/mob_effect/salty.png"),
            3, MoreSnifferFlowers.loc("textures/mob_effect/combo_meal.png"),
            4, MoreSnifferFlowers.loc("textures/mob_effect/pants_on_fire.png"),
            5, MoreSnifferFlowers.loc("textures/mob_effect/hardened_mouth.png"),
            6, MoreSnifferFlowers.loc("textures/mob_effect/sticky.png"),
            7, MoreSnifferFlowers.loc("textures/mob_effect/gluing_touch.png"),
            8, MoreSnifferFlowers.loc("textures/mob_effect/bland.png"),
            9, MoreSnifferFlowers.loc("textures/mob_effect/well_balanced.png")
    );

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        items = new HashSet<>();
        
        for (int i = 0; i < nbt.getInt("size"); i++) {
            ResourceLocation location = ResourceLocation.of(nbt.getString("unlocked" + i), ':');
            items.add(ForgeRegistries.ITEMS.getValue(location));
        }

        unlockedEffects = new HashSet<>();

        for (int i = 0; i < nbt.getInt("effect_size"); i++) {
            unlockedEffects.add(nbt.getInt("effect" + i));
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

        tag.putInt("effect_size", unlockedEffects.size());
        int i1 = 0;
        for (int id :  unlockedEffects) {
            tag.putInt("effect" + i1, id);
            i1++;
        }

        return tag;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.UNLOCKED_NUTRITIONS.orEmpty(cap, optional.cast());
    }
}
