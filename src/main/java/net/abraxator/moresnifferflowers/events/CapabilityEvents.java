package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;

import java.util.HashSet;

// @EventBusSubscriber(modid = MoreSnifferFlowers.MOD_ID)
public class CapabilityEvents {

/*    // Pure Chatgpt code
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
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event) {
        Player player = event.getPlayer();
        if (!event.getLevel().isClientSide){
            LevelChunk chunk = event.getChunk();
            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.sync(chunk.getPos()));
        }

    }*/


}
