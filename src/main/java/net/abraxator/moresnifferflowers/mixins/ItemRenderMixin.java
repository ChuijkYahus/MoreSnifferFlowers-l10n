package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRenderMixin {
    @ModifyVariable(method = "renderQuadList", at = @At(value = "STORE"), ordinal = 2)
    public int injectItemColors(int color, @Local(argsOnly = true) ItemStack itemStack, @Local BakedQuad bakedquad) {
        if (itemStack.getOrCreateTag().contains(Colorable.TAG_HEX) && bakedquad.isTinted()) {
            return itemStack.getOrCreateTag().getInt(Colorable.TAG_HEX);
        }
        return color;
    }
}
