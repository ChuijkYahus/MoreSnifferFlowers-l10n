package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.GiantCropBlockEntity;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.abraxator.moresnifferflowers.blocks.multiblock.PreviewableMultiblock;
import net.abraxator.moresnifferflowers.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;


public class GiantCropBlock extends Block implements ModEntityBlock, Bonmeelable, PreviewableMultiblock {
    public static final VoxelShape SHAPE_POTATO = makeShapePotato();
    public static final VoxelShape SHAPE_CARROT = makeShapeCarrot();
    public static final VoxelShape SHAPE_BEET = makeShapeBeet();
    public static final VoxelShape SHAPE_NETHERWART = makeShapeWart();
    public static final VoxelShape SHAPE_WHEAT = makeShapeWheat();
    public static final VoxelShape SHAPE_ONION = makeShapeOnion();
    public static final VoxelShape SHAPE_TOMATO = makeShapeTomato();
    public static final VoxelShape SHAPE_CABBAGE = makeShapeCabbage();

    public GiantCropBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(ModStateProperties.CENTER, false));
    }

    @Override
    public Stream<BlockPos> fullBlockShape(@Nullable Direction direction, BlockPos center) {
        if (this.equals(ModBlocks.GIANT_CABBAGE.get())){
            return BlockPos.betweenClosedStream(
                    center.getX() - 1,
                    center.getY() - 1,
                    center.getZ() - 1,
                    center.getX() + 1,
                    center.getY(),
                    center.getZ() + 1
            );
        }
        return BlockPos.betweenClosedStream(
                center.getX() - 1,
                center.getY() - 1,
                center.getZ() - 1,
                center.getX() + 1,
                center.getY() + 1,
                center.getZ() + 1
        );
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (isCenter(state)) return RenderShape.ENTITYBLOCK_ANIMATED;
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if(pLevel.getBlockEntity(pPos) instanceof GiantCropBlockEntity entity) {
            if(entity.state == 1) {
                entity.canGrow = true;
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSurviveHelper(state, level, pos);
    }

    @Override
    public boolean canPlace(LevelReader level, BlockPos center, BlockState state) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return updateShapeHelper(state, level, pos);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(pState.getValue(ModStateProperties.CENTER)) {
            pLevel.getBlockTicks().schedule(new ScheduledTick<>(this, pPos, pLevel.getGameTime() + 7, pLevel.nextSubTickCount()));
            if(pLevel.getBlockEntity(pPos) instanceof GiantCropBlockEntity entity && entity.state == 0) {
                entity.state = 1;
            }

            if(pLevel instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ModParticles.GIANT_CROP.get(), pPos.getCenter().x, pPos.getCenter().y, pPos.getCenter().z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ModStateProperties.CENTER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GiantCropBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return tickerHelper(pLevel);
    }

    @Override
    public void performBonmeel(BlockPos blockPos, BlockState blockState, Level level, Player player) {
        this.fullBlockShape(null, blockPos.above()).forEach(pos -> {
            pos = pos.immutable();
            level.destroyBlock(pos, false);
            level.setBlockAndUpdate(pos, getCropMap().get(blockState.getBlock()).getA().defaultBlockState().setValue(ModStateProperties.CENTER, pos.equals(blockPos.above())));
            if(level.getBlockEntity(pos) instanceof MultiBlockEntity entity) {
                entity.setCenter(blockPos.above());
            }
        });

        if(player != null) {

            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancementCritters.USED_BONMEEL.get().trigger(serverPlayer);
            }
        }

        level.playLocalSound(blockPos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    @Override
    public boolean canBonmeel(BlockPos blockPos, BlockState blockState, Level level) {
        Block crop = blockState.getBlock();

        return fullBlockShape(null, blockPos.above()).allMatch(pos -> {
            BlockState state = level.getBlockState(pos);
            int cropY = blockPos.getY();
            var PROPERTY = getCropMap().get(crop).getB().getA();
            int MAX_AGE = getCropMap().get(crop).getB().getB();

            if(pos.getY() == cropY) {
                return state.is(blockState.getBlock()) && state.is(ModTags.ModBlockTags.BONMEELABLE) && state.getValue(PROPERTY) == MAX_AGE;
            } else {
                return state.canBeReplaced();
            }
        });
    }

    private static Map<Block, Pair<Block, Pair<IntegerProperty, Integer>>> cropMapCompat() {
        return Map.of(
                Blocks.CARROTS, new Pair<>(ModBlocks.GIANT_CARROT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.POTATOES, new Pair<>(ModBlocks.GIANT_POTATO.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.NETHER_WART, new Pair<>(ModBlocks.GIANT_NETHERWART.get(), new Pair<>(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE)),
                Blocks.BEETROOTS, new Pair<>(ModBlocks.GIANT_BEETROOT.get(), new Pair<>(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE)),
                Blocks.WHEAT, new Pair<>(ModBlocks.GIANT_WHEAT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),

                vectorwing.farmersdelight.common.registry.ModBlocks.ONION_CROP.get(), new Pair<>(ModBlocks.GIANT_ONION.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                vectorwing.farmersdelight.common.registry.ModBlocks.TOMATO_CROP.get(), new Pair<>(ModBlocks.GIANT_TOMATO.get(), new Pair<>(TomatoVineBlock.VINE_AGE, 3)),
                vectorwing.farmersdelight.common.registry.ModBlocks.CABBAGE_CROP.get(), new Pair<>(ModBlocks.GIANT_CABBAGE.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE))

        );
    }

    private static Map<Block, Pair<Block, Pair<IntegerProperty, Integer>>> cropMapVanilla() {
        return Map.of(
                Blocks.CARROTS, new Pair<>(ModBlocks.GIANT_CARROT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.POTATOES, new Pair<>(ModBlocks.GIANT_POTATO.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE)),
                Blocks.NETHER_WART, new Pair<>(ModBlocks.GIANT_NETHERWART.get(), new Pair<>(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE)),
                Blocks.BEETROOTS, new Pair<>(ModBlocks.GIANT_BEETROOT.get(), new Pair<>(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE)),
                Blocks.WHEAT, new Pair<>(ModBlocks.GIANT_WHEAT.get(), new Pair<>(CropBlock.AGE, CropBlock.MAX_AGE))
        );
    }

    public static Map<Block, Pair<Block, Pair<IntegerProperty, Integer>>> getCropMap() {
        return MoreSnifferFlowers.hasFarmersDelight() ? cropMapCompat() : cropMapVanilla();
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {}

    public static final BlockBehaviour.StatePredicate STATE_PREDICATE = (p_152641_, p_152642_, p_152643_) -> p_152641_.getValue(ModStateProperties.CENTER);

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.block();

        if (getXOffset(getter, pos) == 0 && getZOffset(getter, pos) == 0){
            return shape;
        }

        if (this.equals(ModBlocks.GIANT_POTATO.get())) shape = SHAPE_POTATO;
        if (this.equals(ModBlocks.GIANT_CARROT.get())) shape = SHAPE_CARROT;
        if (this.equals(ModBlocks.GIANT_BEETROOT.get())) shape = SHAPE_BEET;
        if (this.equals(ModBlocks.GIANT_NETHERWART.get())) shape = SHAPE_NETHERWART;
        if (this.equals(ModBlocks.GIANT_WHEAT.get())) shape = SHAPE_WHEAT;
        if (this.equals(ModBlocks.GIANT_ONION.get())) shape = SHAPE_ONION;
        if (this.equals(ModBlocks.GIANT_TOMATO.get())) shape = SHAPE_TOMATO;
        if (this.equals(ModBlocks.GIANT_CABBAGE.get())) shape = SHAPE_CABBAGE;


        return voxelShapeHelper(state, getter, pos, shape, 0, -1, 0);
    }

    public static VoxelShape makeShapePotato(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.4375, -0.0625, -0.4375, 1.4375, 2.125, 1.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.3125, 2.125, -0.3125, 1.3125, 2.375, 1.3125), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeWheat(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.5625, -0.0625, -0.5625, 1.5625, 2.9375, 1.5625), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeCarrot(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.6875, -0.0625, -0.6875, 1.6875, 0.4375, 1.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.4375, 0.375, -0.4375, 1.4375, 1.875, 1.4375), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeWart(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.5625, 1.25, -0.5625, 1.5625, 3, 1.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.125, 0, -0.125, 1.125, 1.25, 1.125), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeBeet(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.375, -0.0625, -0.375, 1.375, 1.75, 1.375), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeCabbage(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.5625, 0, -0.5625, 1.5625, 1.5, 1.5625), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeOnion(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.296875, 0, -0.3125, 1.328125, 2.0625, 1.3125), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeTomato(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.4375, -0.0625, -0.4, 1.4375, 1.8125, 1.465), BooleanOp.OR);

        return shape;
    }



}
