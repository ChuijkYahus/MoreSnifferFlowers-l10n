package net.abraxator.moresnifferflowers.mixins;

import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(ModStateProperties.NOT_CORRUPTED).add(ModStateProperties.NOT_CURED);
    }

    @Inject(method = "updateShape", at = @At("TAIL"), cancellable = true)
    public void updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos, CallbackInfoReturnable<BlockState> cir) {
        if (ModStateProperties.hasCustomLeavesProperties(state) && ModStateProperties.hasCustomLeavesProperties(facingState)) {

            boolean isCorrupted = !facingState.getValue(ModStateProperties.NOT_CORRUPTED);
            boolean isCured = !facingState.getValue(ModStateProperties.NOT_CURED);

            if (isCorrupted) {
                cir.setReturnValue(state.setValue(ModStateProperties.NOT_CORRUPTED, false));
            }

            if (isCured) {
                cir.setReturnValue(state.setValue(ModStateProperties.NOT_CURED, false).setValue(ModStateProperties.NOT_CORRUPTED, true));
            }

            if (!isCured && !isCorrupted) {
                cir.setReturnValue(state.setValue(ModStateProperties.NOT_CURED, true));
            }
        }
    }

}
