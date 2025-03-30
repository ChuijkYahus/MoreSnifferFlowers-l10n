package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MoreSnifferFlowers.MOD_ID);

    public static final RegistryObject<SoundEvent> CROPRESSOR_BELT = variableRange("block.cropressor.belt");
    public static final RegistryObject<SoundEvent> DYESPRIA_PAINT = variableRange("item.dyespria.paint");
    public static final RegistryObject<SoundEvent> C_CHICKEN_JOCKEY = variableRange("c_chicken_jockey");
    public static final RegistryObject<SoundEvent> C_WATER_BUCKET = variableRange("c_water_bucket");
    public static final RegistryObject<SoundEvent> C_OVERWORLD = variableRange("c_overworld");
    public static final RegistryObject<SoundEvent> C_CRAFTING = variableRange("c_crafting");
    public static final RegistryObject<SoundEvent> C_FLINT = variableRange("c_flint");
    public static final RegistryObject<SoundEvent> C_NETHER = variableRange("c_nether");
    public static final RegistryObject<SoundEvent> C_WITHER = variableRange("c_wither");
    public static final RegistryObject<SoundEvent> C_CREATIVITY = variableRange("c_creativity");
    public static final RegistryObject<SoundEvent> C_ELYTRA = variableRange("c_elytra");

    private static RegistryObject<SoundEvent> variableRange(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(MoreSnifferFlowers.loc(name)));
    }
}
