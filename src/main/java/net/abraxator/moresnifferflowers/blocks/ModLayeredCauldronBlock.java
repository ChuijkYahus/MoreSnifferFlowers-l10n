package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.ModCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.datafix.fixes.CauldronRenameFix;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class ModLayeredCauldronBlock extends LayeredCauldronBlock implements EntityBlock {
    public ModLayeredCauldronBlock(Properties properties, Predicate<Biome.Precipitation> fillPredicate, Map<Item, CauldronInteraction> interactions) {
        super(properties, fillPredicate, interactions);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 1));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModCauldronBlockEntity.getItemstack(level, pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModCauldronBlockEntity(pos, state);
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!newState.is(state.getBlock()) && !(newState.is(BlockTags.CAULDRONS)) && level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity) {
            ItemStack cauldronItem = entity.getItemstack();

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cauldronItem);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity && oldState.is(BlockTags.CAULDRONS) && !oldState.is(this)) {
            entity.originalCauldron = oldState;
        }

        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof ModCauldronBlockEntity entity) {
            super.spawnDestroyParticles(level, player, pos, entity.originalCauldron);
            return;
        }
        super.spawnDestroyParticles(level, player, pos, state);
    }
}
