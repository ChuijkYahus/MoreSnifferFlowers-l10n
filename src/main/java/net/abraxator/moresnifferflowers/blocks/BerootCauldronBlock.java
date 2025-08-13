package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class BerootCauldronBlock extends AbstractMultiBlock implements ModEntityBlock, MultiBlock {
    public static final VoxelShape SHAPE_UPPER = makeShapeUpper();
    public static final VoxelShape SHAPE_LOWER = makeShapeLower();
    public static final VoxelShape SHAPE_LOWER_ROTATED = makeShapeLowerRotated();
    public static final VoxelShape SHAPE_INSIDE = Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1, 0.625);
    public static final VoxelShape SHAPE_FULL = Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75);

    public BerootCauldronBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(ModStateProperties.CENTER, false).setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    public @Nullable Block corruptedBlock() {
        return null;
    }

    @Override
    public Block curedBlock() {
        return this;
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING, ModStateProperties.CENTER);

    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(getCenter(level, pos)) instanceof BerootCauldronBlockEntity entity) {
            entity.isCenter = true;
        }
    }

    @Override
    public boolean directional() {
        return true;
    }

    @Override
    public Stream<BlockPos> fullBlockShape(Direction direction, BlockPos center) {
        BlockPos relative = center.relative(direction).relative(direction.getClockWise()).above();
        return BlockPos.betweenClosedStream(new AABB(center.getCenter(), relative.getCenter()));
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        var item = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(level.getBlockEntity(getCenter(level, pos)) instanceof BerootCauldronBlockEntity blockEntity) {
            return blockEntity.addItem(item, player);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private boolean isEntityBlock(Level level, BlockPos pos) {
        return level.getBlockState(pos).hasProperty(ModStateProperties.CENTER) && level.getBlockState(pos).getValue(ModStateProperties.CENTER);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BerootCauldronBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return tickerHelper(level);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context){
        if (getter.getBlockEntity(pos) instanceof BerootCauldronBlockEntity entity) {
            var x = entity.center.getX() - pos.getX();
            var y = entity.center.getY() - pos.getY();
            var z = entity.center.getZ() - pos.getZ() + 1.125;

            switch (state.getValue(HorizontalDirectionalBlock.FACING)){
                case EAST -> x +=1;
                case NORTH -> {
                    x += 1;
                    z -= 1;
                }
                case WEST -> z -= 1;
            }

            if (y != 0) return SHAPE_UPPER.move(x,y,z);
            if (state.getValue(HorizontalDirectionalBlock.FACING).equals(Direction.WEST) || state.getValue(HorizontalDirectionalBlock.FACING).equals(Direction.EAST))
                return SHAPE_LOWER_ROTATED.move(x,y,z);
            return SHAPE_LOWER.move(x,y,z);
        }

        return Shapes.block();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (getter.getBlockEntity(pos) instanceof BerootCauldronBlockEntity entity) {
            var x = entity.center.getX() - pos.getX();
            var y = entity.center.getY() - pos.getY();
            var z = entity.center.getZ() - pos.getZ() + 1.125;

            switch (state.getValue(HorizontalDirectionalBlock.FACING)){
                case EAST -> x +=1;
                case NORTH -> {
                    x += 1;
                    z -= 1;
                }
                case WEST -> z -= 1;
            }

            return SHAPE_FULL.move(x,y,z);
        }

        return Shapes.block();
    }

    public static VoxelShape makeShapeUpper(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }

    public static VoxelShape makeShapeLower(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-1, 0, -0.5, 1, 0.6875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);


        return shape.optimize();
    }

    public static VoxelShape makeShapeLowerRotated(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.375, 0, -1.125, 0.375, 0.6875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -1, 0.875, 1.6875, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.75, 0.5625, -0.875, 0.75, 1.6875, 0.625), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }
}
