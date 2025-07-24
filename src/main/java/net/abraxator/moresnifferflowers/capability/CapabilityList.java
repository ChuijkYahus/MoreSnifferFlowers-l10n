package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityList {
    public static final Capability<NutritionCapability> UNLOCKED_NUTRITIONS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<HardenedMouthCapability> MOUTH_SLOTS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<BlockPatternCapability> BLOCK_PATTERNS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ComboMealCapability> COMBO_MEAL = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<GluedCapability> GLUED = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<CorruptionCapability> CORRUPTION = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(NutritionCapability.class);
        event.register(HardenedMouthCapability.class);
        event.register(BlockPatternCapability.class);
        event.register(ComboMealCapability.class);
        event.register(GluedCapability.class);
        event.register(CorruptionCapability.class);

    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof LivingEntity) {
            if (event.getObject() instanceof Player player) {
                event.addCapability(NutritionCapability.ID, new NutritionCapabilityHandler());
                event.addCapability(HardenedMouthCapability.ID, new HardenedMouthCapabilityHandler());
                event.addCapability(ComboMealCapability.ID,  new ComboMealCapability());

            }

            // For all living entities
            event.addCapability(GluedCapability.ID, new GluedCapability());

        }
    }

    @SubscribeEvent
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        BlockPatternCapability capability = new BlockPatternCapability();
        event.addCapability(capability.ID, capability);
        event.addListener(capability::invalidate);

        event.addCapability(CorruptionCapability.ID, new CorruptionCapability());

    }

}
