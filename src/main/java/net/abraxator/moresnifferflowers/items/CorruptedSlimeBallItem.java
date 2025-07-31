package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CorruptedSlimeBallItem extends Item implements ProjectileItem {
    public CorruptedSlimeBallItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return throwItem(level, player, hand, new CorruptedProjectile(level, player), this);
    }
}
