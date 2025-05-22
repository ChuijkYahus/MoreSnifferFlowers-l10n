package net.abraxator.moresnifferflowers.mixins;

import com.simibubi.create.foundation.utility.BlockHelper;
import net.abraxator.moresnifferflowers.events.ForgeEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BlockHelper.class)
public abstract class CreateModBlockHelperMixin {

    @Inject(method = "destroyBlockAs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"), remap = false, cancellable = true)
    private static void onDestroy(Level world, BlockPos pos, Player player, ItemStack usedTool, float effectChance, Consumer<ItemStack> droppedItemCallback, CallbackInfo ci){
       if (ForgeEvents.blockBreakEventWithoutPlayer(pos, world))
            ci.cancel();
    }
}
