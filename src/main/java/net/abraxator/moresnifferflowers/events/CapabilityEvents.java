package net.abraxator.moresnifferflowers.events;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.UpdateMouthSlotsPacket;
import net.abraxator.moresnifferflowers.networking.UpdateNutritionPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

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
        if (player instanceof ServerPlayer serverPlayer) {
            player.getCapability(CapabilityList.UNLOCKED_NUTRITIONS).ifPresent(cap -> {
                ModPacketHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new UpdateNutritionPacket(cap.getItems())
                );
            });
            player.getCapability(CapabilityList.MOUTH_SLOTS).ifPresent(cap -> {
                ModPacketHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new UpdateMouthSlotsPacket(cap.getMouthSlotItems())
                );
            });
        }
    }

}
