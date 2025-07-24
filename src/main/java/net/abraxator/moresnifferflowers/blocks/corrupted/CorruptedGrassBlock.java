package net.abraxator.moresnifferflowers.blocks.corrupted;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.capability.CorruptionCapability;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModStatePropertiesUnsafe;
import net.abraxator.moresnifferflowers.init.config.ModServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LightEngine;

public class CorruptedGrassBlock extends SpreadingSnowyDirtBlock {
    public CorruptedGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(SNOWY, false).setValue(ModStateProperties.CROWDED, false));
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        double d0 = Math.abs(pEntity.getDeltaMovement().y);
        if (d0 < 0.1 && !pEntity.isSteppingCarefully()) {
            double d1 = 0.8;
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(d1, 1.0, d1));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ModStateProperties.CROWDED);
    }


    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    levelReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(levelReader, blockpos)
            );
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) return;
            level.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
        } else {
            if (!level.isAreaLoaded(pos, 3)) return;
            if (state.getValue(ModStateProperties.CROWDED)) return;
            if (level.getMaxLocalRawBrightness(pos.above()) <=6 && random.nextDouble() < 0.2D *  ModServerConfig.CORRUPTION_SPREAD_SPEED.get()) {
                BlockState blockstate = this.defaultBlockState();

                for (int i = 0; i < 4; i++) {
                   if (spread(level, pos, random, blockstate))
                        placeCorruptedLeaves(level, pos, random);

                }

                placeTallGrass(level, pos);
            }
        }
    }

    private static void placeCorruptedLeaves(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos blockPos1 = pos.above(random.nextInt(6));
        BlockState state2 = level.getBlockState(blockPos1);

        if (state2.getOptionalValue(ModStatePropertiesUnsafe.NOT_CORRUPTED).isPresent() && state2.getValue(ModStatePropertiesUnsafe.NOT_CORRUPTED)){
            level.setBlock(blockPos1, state2.setValue(ModStatePropertiesUnsafe.NOT_CORRUPTED, false), 3);
        }
    }

    private static boolean spread(ServerLevel level, BlockPos pos, RandomSource random, BlockState blockstate) {
        BlockPos pos1 = pos.offset(random.nextIntBetweenInclusive(-2,2), random.nextIntBetweenInclusive(-2,2), random.nextIntBetweenInclusive(-2,2));
        BlockState state1 = level.getBlockState(pos1);

        if (state1.is(BlockTags.DIRT) && canPropagate(blockstate, level, pos1) && !state1.is(ModBlocks.CURED_GRASS_BLOCK.get()) && !state1.is(ModBlocks.CORRUPTED_GRASS_BLOCK.get()) ) {

            if (level.isClientSide) return false;
            LevelChunk chunkNew = level.getChunkAt(pos1);
            LevelChunk chunkOriginal = level.getChunkAt(pos);

            boolean differentChunk = CorruptionCapability.areDifferentChunks(level, pos1, pos);
            
            boolean isSourceOriginal = CorruptionCapability.isSource(chunkOriginal);
            boolean isSourceNew = CorruptionCapability.isSource(chunkNew);
            boolean isNeighborNew = CorruptionCapability.isNeighbor(chunkNew);
            
            int count = CorruptionCapability.getCount(chunkNew);
            int resistance = CorruptionCapability.getResistance(chunkNew);

            if (differentChunk) {
               if (isSourceOriginal) {
                   chunkNew.getCapability(CapabilityList.CORRUPTION).ifPresent(capability -> capability.isNeighbor = true);

               } else if (isNeighborNew) {
                   return false;
               }

            }

            if (!isNeighborNew && !isSourceNew) return false;

            if (isNeighborNew && !isSourceNew) {
                int maxResistance = 5;
                int maxCorruption = 150;
                double chance = 1 - (double) count / Math.max(maxCorruption - resistance*(maxCorruption / maxResistance), 1);
                if (chance <= 0) level.setBlock(pos, blockstate.setValue(ModStateProperties.CROWDED, true), 3);
                if (random.nextDouble() > chance) {
                    return false;
                }
            }

            CorruptionCapability.addCount(chunkNew);

            level.setBlockAndUpdate(pos1, blockstate.setValue(SNOWY, level.getBlockState(pos1.above()).is(Blocks.SNOW)));

            BlockPos posAbove = pos1.above();
            if (random.nextFloat() < 0.10F && level.getBlockState(posAbove).isAir()){
                level.setBlock(posAbove, ModBlocks.CORRUPTED_WART.get().defaultBlockState(), 3);
            }
        }

        return true;
    }

    private static void placeTallGrass(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos.above()).is(Blocks.GRASS))
            level.setBlock(pos.above(), ModBlocks.CORRUPTED_GRASS.get().defaultBlockState(), 18);

        if (level.getBlockState(pos.above()).is(Blocks.TALL_GRASS)) {
            level.setBlock(pos.above(), ModBlocks.CORRUPTED_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 18);
            level.setBlock(pos.above(2), ModBlocks.CORRUPTED_TALL_GRASS.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 18);
        }
    }
}
