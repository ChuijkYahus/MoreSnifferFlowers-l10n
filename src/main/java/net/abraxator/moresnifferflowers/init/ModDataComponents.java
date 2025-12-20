package net.abraxator.moresnifferflowers.init;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.RootedSoup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class ModDataComponents {

    public static final DataComponent<RootedSoup> ROOTED_SOUP = new DataComponent<>("rooted_soup", RootedSoup.CODEC);
    public static final DataComponent<List<RootedSoup.RootedEffect>> ROOTED_EFFECTS = new DataComponent<>("rooted_effects", RootedSoup.RootedEffect.LIST_CODEC);
    public static final DataComponent<Integer> USES = new DataComponent<>("uses", ExtraCodecs.NON_NEGATIVE_INT);
    public static final DataComponent<List<ItemStack>> ROOTED_INGREDIENTS = new DataComponent<>("rooted_ingredients", RootedSoup.ITEM_LIST_CODEC);


    public static  <T> void set(ItemStack stack, DataComponent<T> component, T data){
        component.setOn(stack, data);
    }

    @Nullable
    public static <T> T get(ItemStack stack, DataComponent<T> component){
       return component.getOn(stack);
    }

    public static <T> boolean has(ItemStack stack, DataComponent<T> component){
       return component.isOn(stack);
    }

    public static  <T> void remove(ItemStack stack, DataComponent<T> component){
        component.removeFrom(stack);
    }

    @Nonnull
    public static <T> T getOrDefault(ItemStack stack, DataComponent<T> component, T defaultValue) {
        T value = component.getOn(stack);
        return value == null ? defaultValue : value;
    }

    public record DataComponent<T>(String name, Codec<T> codec){

        private void setOn(ItemStack stack, T data){
            CompoundTag compoundTag = stack.getOrCreateTag();
            codec.encodeStart(NbtOps.INSTANCE, data).result().ifPresent(tag -> compoundTag.put(name, tag));
        }

        private void removeFrom(ItemStack stack){
            CompoundTag compoundTag = stack.getOrCreateTag();

            if (compoundTag.contains(name)){
                compoundTag.remove(name);
            }
        }

        private boolean isOn(ItemStack stack){
            return stack.getOrCreateTag().contains(name);
        }

        @Nullable
        private T getOn(ItemStack stack){
            Tag tag = stack.getOrCreateTag().get(name);
            DataResult<Pair<T, Tag>> decode = codec.decode(NbtOps.INSTANCE, tag);

            return decode.result()
                    .map(Pair::getFirst)
                    .orElse(null);
        }

    }
}
