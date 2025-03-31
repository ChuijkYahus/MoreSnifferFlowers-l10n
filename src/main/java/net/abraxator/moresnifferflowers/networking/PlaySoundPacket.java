package net.abraxator.moresnifferflowers.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlaySoundPacket(SoundEvent sound, double x, double y, double z) {
    public static void handle(PlaySoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().level.playLocalSound(
                        msg.x(), msg.y(), msg.z(), msg.sound(), SoundSource.PLAYERS, 1.0F, 1.0F, false
                );
            });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}