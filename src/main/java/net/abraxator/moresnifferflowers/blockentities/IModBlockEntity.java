package net.abraxator.moresnifferflowers.blockentities;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;

public interface IModBlockEntity {
    default void tick(Level level) {};

    default void clientTick(ClientLevel level) {};

}
