package net.abraxator.moresnifferflowers.events.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

public class SlotTakeEvent extends Event {

    private final ItemStack itemStack;
    private final Player player;

    public SlotTakeEvent(ItemStack itemStack, Player player) {
        this.itemStack = itemStack;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ItemStack getStack() {
        return itemStack;
    }
}
