package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;

@Mixin(TomatoVineBlock.class)
public class FarmersDelightTomatoVineMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void bonmeelAllower(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir){
        if (player.getItemInHand(hand).is(ModItems.JAR_OF_BONMEEL.get())){
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
