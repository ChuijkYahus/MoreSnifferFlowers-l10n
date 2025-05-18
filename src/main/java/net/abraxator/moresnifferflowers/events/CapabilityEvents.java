package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.BlockPatternSavedData;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {

    // Handle capability persistence when a player dies and respawns
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps(); // This makes sure old capabilities are still accessible

        oldPlayer.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(oldCap -> {

            newPlayer.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(newCap -> {

                newCap.setItems(new HashSet<>(oldCap.getItems())); // Copy data
                newCap.sync(newPlayer);});

        });

        oldPlayer.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(oldCap -> {

            newPlayer.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(newCap -> {

                newCap.setAllItems(oldCap.getMouthSlotItems()); // Copy data
                newCap.sync(newPlayer);});

        });

        oldPlayer.invalidateCaps(); // Clean up old capabilities after cloning
    }

    // Handle capability persistence when a player logs in
    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        event.getEntity().getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
            CompoundTag tag = event.getEntity().getPersistentData().getCompound("NutritionData");
            cap.deserializeNBT(tag);
        });
        event.getEntity().getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
            CompoundTag tag = event.getEntity().getPersistentData().getCompound("MouthData");
            cap.deserializeNBT(tag);
        });
    }

    // Handle saving the capability when the player logs out
    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        event.getEntity().getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
            CompoundTag tag = cap.serializeNBT();
            event.getEntity().getPersistentData().put("NutritionData", tag);
        });
        event.getEntity().getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
            CompoundTag tag = cap.serializeNBT();
            event.getEntity().getPersistentData().put("MouthData", tag);
        });
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
            cap.sync(player);
        });
        player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
            cap.sync(player);
        });
        if (CapabilityList.getBlockPatterns().isEmpty() && player.level() instanceof ServerLevel serverLevel) CapabilityList.setFromDisk(serverLevel);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();
        BlockPatternCapability blockPatterns = CapabilityList.getBlockPatterns();

        if (levelAccessor instanceof ServerLevel serverLevel) {
            CapabilityList.setFromDisk(serverLevel);
            blockPatterns.sync();
        } else {
        }
    }

    @SubscribeEvent
    public static void onLevelSave(LevelEvent.Save event) {
        LevelAccessor levelAccessor = event.getLevel();
        BlockPatternCapability blockPatterns = CapabilityList.getBlockPatterns();

        if (levelAccessor instanceof ServerLevel serverLevel) {
            BlockPatternSavedData.get(serverLevel).save(blockPatterns.save(new CompoundTag()));
        } else {
         //   blockPatterns.clear();
        }


    }


}
