package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.SaltProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SourSpiceItem extends ItemNameBlockItem implements ProjectileItem {
    public SourSpiceItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        SaltProjectile projectile = new SaltProjectile(level, player);
        projectile.setCorrupted(true);

        return throwItem(level, player, hand, projectile, this);
    }
}
