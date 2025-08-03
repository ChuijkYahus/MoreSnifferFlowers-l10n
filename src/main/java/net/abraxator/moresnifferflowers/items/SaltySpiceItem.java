package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.entities.JarOfAcidProjectile;
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

public class SaltySpiceItem extends ItemNameBlockItem implements ProjectileItem{
    public SaltySpiceItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return throwItem(level, player, hand, new SaltProjectile(level, player), this);
    }
}
