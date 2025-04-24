package net.abraxator.moresnifferflowers.client.gui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DummyContainer implements Container {
    public static final DummyContainer INSTANCE = new DummyContainer();
    private DummyContainer() {}
    public int getContainerSize() { return 0; }
    public boolean isEmpty() { return true; }
    public ItemStack getItem(int i) { return ItemStack.EMPTY; }
    public ItemStack removeItem(int i, int j) { return ItemStack.EMPTY; }
    public ItemStack removeItemNoUpdate(int i) { return ItemStack.EMPTY; }
    public void setItem(int i, ItemStack stack) {}
    public void setChanged() {}
    public boolean stillValid(Player p) { return true; }
    public void clearContent() {}
}