package net.abraxator.moresnifferflowers.mixins;

import com.mojang.authlib.GameProfile;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.NutritionCapability;
import net.abraxator.moresnifferflowers.client.gui.screen.cookbook.CookbookScreen;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow public abstract void tick();

    @Shadow @Final protected Minecraft minecraft;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "openItemGui", at = @At("TAIL"))
    public void openItemGui(ItemStack stack, InteractionHand hand, CallbackInfo ci){
        if (stack.is(ModItems.BEROOT_COOK_BOOK.get())){
            this.minecraft.setScreen((new CookbookScreen(this.getCapability(CapabilityList.UNLOCKED_NUTRITIONS)
                    .map(NutritionCapability::getItems)
                    .orElse(new HashSet<>()))));
        }
    }
}
