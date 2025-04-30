package net.abraxator.moresnifferflowers.data.tag;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModBannerPatterns;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

public class ModBannerPatternTagsProvider extends TagsProvider<BannerPattern> {
    public ModBannerPatternTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.BANNER_PATTERN, provider, MoreSnifferFlowers.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.ModBannerPatternTags.AMBUSH_BANNER_PATTERN).addOptional(ModBannerPatterns.AMBUSH.location());
        tag(ModTags.ModBannerPatternTags.EVIL_BANNER_PATTERN).addOptional(ModBannerPatterns.EVIL.location());
    }
}
