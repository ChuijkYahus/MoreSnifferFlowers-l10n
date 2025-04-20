package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.events.custom.SlotTakeEvent;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    public void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().is(ModItems.BURNED_SLOT.get()) && !player.isCreative()){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(Player player, ItemStack stack, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.post(new SlotTakeEvent(stack, player));

    }

}
