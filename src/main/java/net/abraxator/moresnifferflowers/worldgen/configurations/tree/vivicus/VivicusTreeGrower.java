package net.abraxator.moresnifferflowers.worldgen.configurations.tree.vivicus;

import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.worldgen.configurations.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class VivicusTreeGrower {
    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, BlockState saplingState) {
        return saplingState.getValue(ModStateProperties.VIVICUS_CURED) == false ? ModConfiguredFeatures.CORRUPTED_VIVICUS_TREE : ModConfiguredFeatures.CURED_VIVICUS_TREE;
    }

    public boolean growTree(ServerLevel level, ChunkGenerator pChunkGenerator, BlockPos pos, BlockState saplingState, RandomSource random) {
        ResourceKey<ConfiguredFeature<?, ?>> resourcekey1 = this.getConfiguredFeature(random, saplingState);
        if (resourcekey1 == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> holder1 = level.registryAccess()
                    .registryOrThrow(Registries.CONFIGURED_FEATURE)
                    .getHolder(resourcekey1)
                    .orElse(null);
            var event = ForgeEventFactory.blockGrowFeature(level, random, pos, holder1);
            holder1 = event.getFeature();
            if (event.isCanceled()) return false;
            if (holder1 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> configuredfeature1 = holder1.value();
                BlockState blockstate1 = level.getFluidState(pos).createLegacyBlock();
                //level.setBlock(pos, blockstate1, 4);
                if (configuredfeature1.place(level, pChunkGenerator, random, pos)) {
                    if (level.getBlockState(pos) == blockstate1) {
                        level.sendBlockUpdated(pos, saplingState, blockstate1, 2);
                    }

                    return true;
                } else {
                    level.setBlock(pos, saplingState, 4);
                    return false;
                }
            }
        }
    }
}
