package net.abraxator.moresnifferflowers.effects;

import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PantsOnFireEffect extends MobEffect {
    public PantsOnFireEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }


    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.level();

        if (livingEntity instanceof Player player && player.hasEffect(this) && !level.isClientSide){
            int burnedSlots = 0;
            final int maxBurnedSlots = (1 + 2*amplifier);

            for (Slot slot : player.inventoryMenu.slots){
                if (slot.getItem().is(ModItems.BURNED_SLOT.get())) burnedSlots++;
                if (burnedSlots >= maxBurnedSlots) return;
            }

            for (int i = 0; i < maxBurnedSlots - burnedSlots ; i++) {
                int slotId = level.random.nextIntBetweenInclusive(9, 35);
                InventoryMenu menu = player.inventoryMenu;
                Slot slot = menu.getSlot(slotId);

                if (slot.getItem().is(ModItems.BURNED_SLOT.get())){
                    return;
                }

                ItemStack stack = slot.getItem();
                player.drop(stack, true);

                slot.set(ModItems.BURNED_SLOT.get().getDefaultInstance());
            }

            level.playSound(null, livingEntity, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS ,1, 1);
        }
    }

}
