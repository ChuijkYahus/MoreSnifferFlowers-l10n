package net.abraxator.moresnifferflowers.blocks;

import net.abraxator.moresnifferflowers.blockentities.SoupCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SoupCauldronBlock extends HorizontalDirectionalBlock implements ModEntityBlock {
    public SoupCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING, ModStateProperties.ENTITY);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos entityPos = BlockPos.withinManhattanStream(level.getBlockState(pos.below()).is(this) ? pos.below() : pos, 2, 1, 2)
                .filter(blockPos -> isEntityBlock(level, blockPos))
                .findFirst().orElse(null);

        if(entityPos != null && level.getBlockEntity(entityPos) instanceof SoupCauldronBlockEntity blockEntity) {
            blockEntity.addItem(player.getItemInHand(hand));
            
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        
        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        BlockPos relative = pos.relative(direction).relative(direction.getClockWise()).above();
        BlockPos.betweenClosedStream(new AABB(pos, relative)).forEach(blockPos -> {
            blockPos = blockPos.immutable();
            level.setBlock(blockPos, state.setValue(ModStateProperties.ENTITY, pos.equals(blockPos)), 3);
            if(level.getBlockEntity(blockPos) instanceof SoupCauldronBlockEntity entity) {
                entity.center = pos;
            }
        });
    }

/*    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(level.getBlockState(pos.below()).is(this)) {
            return Block.box(0, 0, 0, 16, 8, 16);
        } else {
            return Shapes.block();
        }
    }*/

    private boolean isEntityBlock(Level level, BlockPos pos) {
        return level.getBlockState(pos).hasProperty(ModStateProperties.ENTITY) && level.getBlockState(pos).getValue(ModStateProperties.ENTITY);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoupCauldronBlockEntity(blockPos, blockState);
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (getter.getBlockEntity(pos) instanceof SoupCauldronBlockEntity entity) {
            var x = entity.center.getX() - pos.getX();
            var y = entity.center.getY() - pos.getY();
            var z = entity.center.getZ() - pos.getZ() + 1.125;

            if (y != 0) return makeShapeUpper().move(x,y,z);
            return makeShapeLower().move(x,y,z);
        }

        return Shapes.block();
    }
    public static VoxelShape makeShapeUpper(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -0.9375, 0.875, 1.6875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.6875, 0.5625, -0.6875, 0.6875, 1.6875, 0.5625), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }

    public static VoxelShape makeShapeLower(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -0.9375, 0.875, 1.6875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 0, -0.5, 1, 0.6875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.6875, 0.5625, -0.6875, 0.6875, 1.6875, 0.5625), BooleanOp.ONLY_FIRST);


        return shape.optimize();
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.6875, 0.5625, -0.6875, 0.6875, 1.6875, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.875, 0.5, -0.9375, 0.875, 1.6875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 0, -0.25, 1, 0.6875, 0.5), BooleanOp.ONLY_FIRST);

        return shape.optimize();
    }

}
