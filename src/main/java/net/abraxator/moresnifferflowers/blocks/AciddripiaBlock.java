package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BondripiaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class AciddripiaBlock extends BondripiaBlock {
    public AciddripiaBlock(Properties p_49795_) {
        super(p_49795_);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof BondripiaBlockEntity entity) {
            if (!isMaxAge(state)) {
                grow(level, pos, state);
            } else if (random.nextDouble() <= 0.33D) {
                var aabb = new AABB(entity.center.below()).inflate(1.5D, 0, 1.5D).setMaxY(10.0D);
                level.getEntities((Entity) null, aabb, entity1 -> entity1.getType() == EntityType.PLAYER)
                        .stream().map(entity1 -> ((Player) entity1))
                        .forEach(entity1 -> {
                            entity1.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2));
                        });
                for (BlockPos blockPos : BlockPos.betweenClosed(entity.center.below().north().east(), entity.center.below().south().west())) {
                    BlockPos currentPos = blockPos;

                    int y = level.getRandom().nextIntBetweenInclusive(1, 11);
                    currentPos = currentPos.below(y);
                    if (getProperty(currentPos, level).isPresent()) {
                            BlockState stateNew = level.getBlockState(currentPos);
                            stateNew = stateNew.setValue((IntegerProperty) getProperty(currentPos, level).get(), 0);
                            level.setBlock(currentPos, stateNew, 2);
                    }

                        BlockState stateNew = level.getBlockState(currentPos);
                        if (stateNew.is(BlockTags.LEAVES)) {
                            level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 2);
                        } else if (level.getBlockState(currentPos).getBlock() instanceof AbstractCauldronBlock) {
                            fillCauldron(level, currentPos, this.defaultBlockState());
                        } else if (stateNew.is(BlockTags.DIRT) && !stateNew.is(Blocks.DIRT)) {
                            level.setBlock(currentPos, Blocks.DIRT.defaultBlockState(), 2);
                        }

                    }
            }
        }
    }

    private Optional<Property<?>> getProperty(BlockPos pos, Level level) {
        return level.getBlockState(pos).getProperties().stream()
                .filter(property -> property.getName().contains("age") && property instanceof IntegerProperty)
                .findAny();
    }
}
