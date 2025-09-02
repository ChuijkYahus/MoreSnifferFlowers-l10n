package net.abraxator.moresnifferflowers.mixins;


import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.capability.FakeRenderingCapability;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "onRemove", at = @At("HEAD"))
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston, CallbackInfo ci) {
        boolean isSame = state.is(newState.getBlock());
        if (isSame) return;

        if (BlockPatternCapability.hasPattern(pos, level)) {
            BlockPatternCapability.removePattern(pos, level);
        }

        LevelChunk chunk = level.getChunkAt(pos);

        if (state.is(ModTags.ModBlockTags.CORRUPTION_SHIELDING) && !level.isClientSide){
            CorruptionCapability cap = CorruptionCapability.get(chunk);
            cap.flowers.remove(pos);
            if (cap.resistance > 0 && cap.flowers.size() < cap.resistance) cap.resistance--;
        }

        if (state.is(ModTags.ModBlockTags.FAKE_RENDER)){
            Set<BlockPos> posSet = FakeRenderingCapability.getCopy(chunk);
            posSet.remove(pos);
            FakeRenderingCapability.set(chunk, posSet);
        }
    }


}
