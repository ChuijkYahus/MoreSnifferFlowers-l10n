package net.abraxator.moresnifferflowers.mixins;


import com.google.common.collect.ImmutableList;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.mixins.accessor.PalettedPermutationsAccessor;
import net.abraxator.moresnifferflowers.mixins.accessor.SpriteResourceLoaderAccessor;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SpriteResourceLoader.class)
public abstract class SpriteResourceLoaderMixin {

    @Inject(method = "load",
            at = @At("RETURN"))
    private static void moresnifferflowers$load(ResourceManager resourceManager, ResourceLocation location, CallbackInfoReturnable<SpriteResourceLoader> cir) {
        final List<String> trims = List.of("aroma", "carnage", "tater", "nether_wart", "carotene", "grain", "beat");

        if (location.equals(new ResourceLocation("minecraft", "armor_trims"))) {
            SpriteResourceLoader ret = cir.getReturnValue();
            for (SpriteSource source : ((SpriteResourceLoaderAccessor) ret).getSources()) {
                if(source instanceof PalettedPermutationsAccessor permutations) {
                    for (String trim : trims) { 
                        ResourceLocation trimLocation = MoreSnifferFlowers.loc("trims/models/armor/" + trim);
                        ResourceLocation leggingsTrimLocation = MoreSnifferFlowers.loc("trims/models/armor/" + trim + "_leggings");
                        permutations.setTextures(ImmutableList.<ResourceLocation>builder().addAll(permutations.getTextures()).add(trimLocation, leggingsTrimLocation).build());
                    }
                }
            }
        }
    }
}