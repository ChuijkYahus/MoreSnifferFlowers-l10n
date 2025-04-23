package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModClientConfig;
import net.abraxator.moresnifferflowers.init.ModMobEffects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
    @Unique
    private static final ResourceLocation TEXTURE_LOCATION = MoreSnifferFlowers.loc("textures/gui/container/hardened_mouth.png");
    @Unique
    private int moreSnifferFlowers$mouthSlotX;
    @Unique
    private int moreSnifferFlowers$mouthSlotY;

    public InventoryScreenMixin(InventoryMenu menu, Inventory playerInventory, Component title, int moreSnifferFlowers$mouthSlotX, int moreSnifferFlowers$mouthSlotY) {
        super(menu, playerInventory, title);
        this.moreSnifferFlowers$mouthSlotX = moreSnifferFlowers$mouthSlotX;
        this.moreSnifferFlowers$mouthSlotY = moreSnifferFlowers$mouthSlotY;
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci){
        this.moreSnifferFlowers$mouthSlotX = ModClientConfig.CLIENT.HARDENED_MOUTH_X.get();
        this.moreSnifferFlowers$mouthSlotY = ModClientConfig.CLIENT.HARDENED_MOUTH_Y.get();

    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.minecraft.player.hasEffect(ModMobEffects.HARDENED_MOUTH.get())){
            guiGraphics.blit(TEXTURE_LOCATION, this.leftPos + this.moreSnifferFlowers$mouthSlotX, this.topPos + this.moreSnifferFlowers$mouthSlotY, 0, 0, 24, 60);

        }
    }

/*    @Inject(method = "containerTick", at = @At("TAIL"))
    public void containerTick(CallbackInfo ci) {

    }*/



}
