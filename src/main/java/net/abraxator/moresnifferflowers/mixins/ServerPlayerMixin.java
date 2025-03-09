package net.abraxator.moresnifferflowers.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "getRespawnPosition", at = @At("RETURN"), cancellable = true)
    public void moresnifferflowers$getRespawnPosition(CallbackInfoReturnable<BlockPos> cir) {
        cir.setReturnValue(new BlockPos(0, 64, 0));
    }
}
