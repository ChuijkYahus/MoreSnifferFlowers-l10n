package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.client.gui.slot.HardenedMouthSlot;
import net.abraxator.moresnifferflowers.init.config.ModClientConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingInput, CraftingRecipe> {


    public InventoryMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Inventory playerInventory, boolean active, Player owner, CallbackInfo ci) {
        int moreSnifferFlowers$mouthSlotX = 180;
        int moreSnifferFlowers$mouthSlotY = 80;

        if (owner.level().isClientSide) {
            moreSnifferFlowers$mouthSlotX = ModClientConfig.HARDENED_MOUTH_X.get();
            moreSnifferFlowers$mouthSlotY = ModClientConfig.HARDENED_MOUTH_Y.get();

        }

        this.addSlot(new HardenedMouthSlot(owner , 0, moreSnifferFlowers$mouthSlotX + 4, moreSnifferFlowers$mouthSlotY + 4,
                () -> owner.getCapability(CapabilityList.MOUTH_SLOTS)
                .map(HardenedMouthCapability::getMouthSlotItems)
                .orElse(NonNullList.withSize(2, ItemStack.EMPTY))));
        // x180, y84

        this.addSlot(new HardenedMouthSlot(owner , 1, moreSnifferFlowers$mouthSlotX + 4, moreSnifferFlowers$mouthSlotY + 40,
                () -> owner.getCapability(CapabilityList.MOUTH_SLOTS)
                        .map(HardenedMouthCapability::getMouthSlotItems)
                        .orElse(NonNullList.withSize(2, ItemStack.EMPTY))));
        // x180, y120


    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < this.slots.size()) {
            Slot slot = this.slots.get(slotId);

            if (slot instanceof HardenedMouthSlot hardenedMouthSlot) {
                hardenedMouthSlot.handleCapabilitySlotClick(hardenedMouthSlot, player, clickType, dragType);
                return;
            }
        }
        super.clicked(slotId, dragType, clickType, player);
    }

}
