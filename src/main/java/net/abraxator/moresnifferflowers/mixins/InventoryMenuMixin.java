package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.client.gui.menu.InventoryMenuExtension;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingContainer> implements InventoryMenuExtension {

    @Unique
    public final List<Integer> moreSnifferFlowers$extraSlotIds = new ArrayList<>();

    public InventoryMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Inventory playerInventory, boolean active, Player owner, CallbackInfo ci) {
        // Add two extra slots at fixed positions (example X/Y)
        int startIndex = playerInventory.items.size(); // Usually 36
        this.addSlot(new Slot(playerInventory, startIndex, 180, 84)); // Extra Slot 1
        moreSnifferFlowers$extraSlotIds.add(this.slots.size() - 1);
        this.addSlot(new Slot(playerInventory, startIndex + 1, 180, 120)); // Extra Slot 2}
        moreSnifferFlowers$extraSlotIds.add(this.slots.size() - 1);

    }

    @Override
    public List<Integer> moreSnifferFlowers$getExtraSlotIds() {
        return moreSnifferFlowers$extraSlotIds;
    }
}
