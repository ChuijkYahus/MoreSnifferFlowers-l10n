package net.abraxator.moresnifferflowers.data.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends TagsProvider<Biome> {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.BIOME, provider, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.ModBiomeTags.HAS_SWAMP_SNIFFER_TEMPLE).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
    }
}
