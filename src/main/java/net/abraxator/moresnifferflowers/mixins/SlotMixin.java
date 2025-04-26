package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.events.custom.SlotTakeEvent;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getItem();

    @Shadow @Final public Container container;

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    public void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().is(ModItems.BURNED_SLOT.get())){
            cir.setReturnValue(false);
        }
     //   moreSnifferFlowers$cancelWithoutEffect(cir);
    }

/*    @Inject(method = "mayPlace",at = @At("HEAD"), cancellable = true)
    public void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
       //  moreSnifferFlowers$cancelWithoutEffect(cir);
    }*/

    @Inject(method = "isHighlightable",at = @At("HEAD"), cancellable = true)
    public void isHighlightable(CallbackInfoReturnable<Boolean> cir) {
      if (this.getItem().is(ModItems.BURNED_SLOT.get())) cir.setReturnValue(false);
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(Player player, ItemStack stack, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.post(new SlotTakeEvent(stack, player));

    }

/*    @Unique
    private void moreSnifferFlowers$cancelWithoutEffect(CallbackInfoReturnable<Boolean> cir) {
        if (this.container instanceof Inventory inventory) {
            Player player = inventory.player;
            if (player.inventoryMenu instanceof InventoryMenuExtension menuExtension && !player.hasEffect(ModMobEffects.HARDENED_MOUTH.get())) {
                menuExtension.moreSnifferFlowers$getExtraSlotIds().forEach(integer -> {
                    if (this.index == integer) cir.setReturnValue(false);
                });
            }
        }
    }*/


}
