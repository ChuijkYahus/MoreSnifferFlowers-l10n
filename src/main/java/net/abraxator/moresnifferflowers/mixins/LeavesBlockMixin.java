package net.abraxator.moresnifferflowers.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
        // this.registerDefaultState(this.stateDefinition.any().setValue(ModStateProperties.CURED, false).setValue(ModStateProperties.CORRUPTED, false));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
       // builder.add(ModStateProperties.CURED).add(ModStateProperties.CORRUPTED);
    }


}
