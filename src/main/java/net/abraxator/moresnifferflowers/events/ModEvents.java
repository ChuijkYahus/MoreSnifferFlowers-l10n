package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.data.datamaps.ModDataMaps;
import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItemProperties;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BOBLING.get(), BoblingEntity.createAttributes().build());
    }


    @SubscribeEvent
    public static void onRegisterDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ModDataMaps.CORRUPTABLE);
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntityTypes.BOBLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerConditionalProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(MoreSnifferFlowers.loc("dyespria"), ModItemProperties.DyespriaConditionalProperty.MAP_CODEC);
        event.register(MoreSnifferFlowers.loc("og"), ModItemProperties.OGConditionalProperty.MAP_CODEC);

    }
}
