package net.abraxator.moresnifferflowers.data;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    protected ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MoreSnifferFlowers.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.CROPRESSOR_BELT, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.CROPRESSOR_BELT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_1")))
                .with(sound(MoreSnifferFlowers.loc("cropressor_sound_2")))
        );
        add(ModSoundEvents.DYESPRIA_PAINT, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.DYESPRIA_PAINT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("dyespria_paint")))
        );
        add(ModSoundEvents.C_CHICKEN_JOCKEY, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_CHICKEN_JOCKEY.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_chicken_jockey")))
        );
        add(ModSoundEvents.C_WATER_BUCKET, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_WATER_BUCKET.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_water_bucket")))
        );
        add(ModSoundEvents.C_OVERWORLD, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_OVERWORLD.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_overworld")))
        );
        add(ModSoundEvents.C_CRAFTING, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_CRAFTING.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_crafting")))
        );
        add(ModSoundEvents.C_FLINT, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_FLINT.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_flint")))
        );
        add(ModSoundEvents.C_NETHER, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_NETHER.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_nether")))
        );
        add(ModSoundEvents.C_WITHER, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_WITHER.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_wither")))
        );
        add(ModSoundEvents.C_CREATIVITY, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_CREATIVITY.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_creativity")))
        );
        add(ModSoundEvents.C_ELYTRA, definition()
                .subtitle("sound.moresnifferflowers." + ModSoundEvents.C_ELYTRA.get().getLocation().getPath())
                .with(sound(MoreSnifferFlowers.loc("c_elytra")))
        );


    }

}
