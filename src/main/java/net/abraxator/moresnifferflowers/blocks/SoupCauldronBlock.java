package net.abraxator.moresnifferflowers.blocks;

import com.sun.source.tree.LambdaExpressionTree;
import net.abraxator.moresnifferflowers.blockentities.SoupCauldronBlockEntity;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SoupCauldronBlock extends HorizontalDirectionalBlock implements ModEntityBlock {
    public SoupCauldronBlock(Properties properties) {
        super(properties);
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
        BlockPos.betweenClosedStream(new AABB(pos, relative)).forEach(blockPos -> 
                level.setBlock(blockPos, state.setValue(ModStateProperties.ENTITY, pos.equals(blockPos)), 3));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(level.getBlockState(pos.below()).is(this)) {
            return Block.box(0, 0, 0, 16, 8, 16);
        } else {
            return Shapes.block();
        }
    }

    private boolean isEntityBlock(Level level, BlockPos pos) {
        return level.getBlockState(pos).hasProperty(ModStateProperties.ENTITY) && level.getBlockState(pos).getValue(ModStateProperties.ENTITY);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoupCauldronBlockEntity(blockPos, blockState);
    }
}
