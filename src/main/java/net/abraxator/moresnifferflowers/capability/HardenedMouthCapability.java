package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.client.gui.slot.HardenedMouthSlot;
import net.abraxator.moresnifferflowers.init.ModEffects;
import net.abraxator.moresnifferflowers.networking.toClient.SyncMouthSlotsPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class HardenedMouthCapability {
    public static int SLOT_COUNT = 2;
    NonNullList<ItemStack> mouthSlots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    public int cooldown = 0;

    public static final Codec<List<ItemStack>> ITEMSTACK_LIST_CODEC = ItemStack.CODEC.listOf()
            .validate(list -> list.size() != 2
                    ? DataResult.error(() -> "Expected " + 2 + " items, got " + list.size())
                    : DataResult.success(list.stream()
                    .map(stack -> stack == null ? ItemStack.EMPTY : stack)
                    .toList()));

    public static final Codec<HardenedMouthCapability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ITEMSTACK_LIST_CODEC.fieldOf("slots").forGetter(cap -> cap.mouthSlots))
            .apply(instance, (itemStacks) -> {
                HardenedMouthCapability capability  = new HardenedMouthCapability();
                capability.mouthSlots = (NonNullList<ItemStack>) itemStacks;
                return capability;
    }));


    
    public NonNullList<ItemStack> getMouthSlotItems() {
        return mouthSlots;
    }

    
    public void setAllItems(NonNullList<ItemStack> itemStacks) {
        for (int i = 0; i < itemStacks.size(); i++) {
            mouthSlots.set(i, itemStacks.get(i));
        }
    }

    
    public void setItem(int index, ItemStack stack) {
        mouthSlots.set(index, stack);
    }

    
    public ItemStack getItem(int index) {
        return mouthSlots.get(index);
    }

    
    public void clear() {
        mouthSlots.clear();
    }

    
    public void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToAllPlayers(new SyncMouthSlotsPacket(mouthSlots, cooldown));
        }
    }

    public void onEffectEnd(Player player) {
        getMouthSlotItems().forEach(itemStack -> {
            if (HardenedMouthSlot.moveToPlayerInventory(player.inventoryMenu, itemStack)) return;
            if (itemStack.isEmpty()) return;
            player.drop(itemStack, true);
        });
        clear();
        sync(player);
    }

    public void tick(Player player) {

        if (player.level().isClientSide || !player.hasEffect(ModEffects.HARDENED_MOUTH)) return;

        ItemStack input = this.getItem(0);
        ItemStack output = this.getItem(1);


        if (getSmeltingResult(player.level(), input).isEmpty() || (!getSmeltingResult(player.level(), input).get().is(output.getItem()) && !output.isEmpty())) cooldown = getMaxCooldown(player);

        if (cooldown > 0) {
            cooldown--;
            sync(player);
            return;
        }

        getSmeltingResult(player.level(), input).ifPresentOrElse(result -> {

            if (output.isEmpty() || (ItemStack.isSameItemSameComponents(output, result) && output.getCount() < output.getMaxStackSize())) {
                input.shrink(1);

                if (output.isEmpty()) {
                    this.setItem(1, result.copy());
                } else {
                    output.grow(1);
                    this.setItem(1, output);
                }

                this.setItem(0, input);

                cooldown = getMaxCooldown(player);
            }
        }, () -> cooldown = getMaxCooldown(player));

        sync(player);
    }

    
    public int getCooldown() {
        return this.cooldown;
    }

    
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    
    public int getMaxCooldown(Player player) {
        int amplifier = 0;
        if (player.hasEffect(ModEffects.HARDENED_MOUTH)) amplifier = player.getEffect(ModEffects.HARDENED_MOUTH).getAmplifier();

        return Math.max(1, 80 - amplifier * 10);
    }

    public Optional<ItemStack> getSmeltingResult(Level level, ItemStack input) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().getResultItem(level.registryAccess()));
    }

    public boolean isEmpty(){
        return getItem(0).isEmpty() && getItem(1).isEmpty();
    }

}
