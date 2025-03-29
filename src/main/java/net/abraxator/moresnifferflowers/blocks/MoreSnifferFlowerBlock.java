package net.abraxator.moresnifferflowers.blocks;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.components.Colorable;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;

import java.util.Map;
import java.util.Optional;

public class MoreSnifferFlowerBlock extends ModEntityDoubleTallBlock implements ModCropBlock, Colorable {
    public static final IntegerProperty STALK_TYPE = IntegerProperty.create("stalk_type", 1, 3);
    public static final IntegerProperty LEAVES_TYPE = IntegerProperty.create("leaves_type", 1, 3);
    public static int AGE_TO_GROW_UP = 0;

    public MoreSnifferFlowerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(STALK_TYPE, 1).setValue(LEAVES_TYPE, 1).setValue(ModStateProperties.AGE_3, 0).setValue(ModStateProperties.COLOR, DyeColor.WHITE));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(STALK_TYPE, LEAVES_TYPE, getAgeProperty(), ModStateProperties.COLOR);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return ModStateProperties.AGE_3;
    }


    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        DyeColor dyeColor = Util.getRandom(DyeColor.values(), pLevel.random);
        int stalkType = pLevel.random.nextIntBetweenInclusive(1,3);
        int leavesType = pLevel.random.nextIntBetweenInclusive(1,3);

        if(!pOldState.is(this)) {
            pLevel.setBlock(pPos, pState.setValue(ModStateProperties.COLOR, dyeColor).setValue(MoreSnifferFlowerBlock.LEAVES_TYPE, leavesType).setValue(MoreSnifferFlowerBlock.STALK_TYPE, stalkType), 3);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        Optional<PosAndState> posAndState = this.getLowerHalf(pLevel, pPos, pState);
        return posAndState.isPresent() && this.canGrow(pLevel, posAndState.get().blockPos(), posAndState.get().state(), getAge(posAndState.get().state()) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        this.getLowerHalf(pLevel, pPos, pState).ifPresent(posAndState -> {
            if(pState.getValue(ModStateProperties.AGE_3) < 4) {
                this.grow(pLevel, posAndState.state(), posAndState.blockPos(), 1);
            }
        });
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModItems.MORE_SNIFFER_FLOWER_SEEDS.get().getDefaultInstance();
    }

    @Override
    public int getMaxAge() {
        return ModCropBlock.super.getMaxAge();
    }

    private boolean canGrow(LevelReader pLevel, BlockPos pPos, BlockState pState, int k) {
        return !this.isMaxAge(pState) && sufficientLight(pLevel, pPos) && (k < AGE_TO_GROW_UP || canGrowInto(pLevel.getBlockState(pPos.above()))) && isLower(pState);
    }

    private boolean canGrowInto(BlockState state) {
        return state.isAir() || state.is(getUpperBlock());
    }

    private boolean sufficientLight(LevelReader pLevel, BlockPos pPos) {
        return pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos);
    }


    @Override
    public boolean mayPlaceOn(BlockState pState) {
        return pState.is(BlockTags.DIRT);
    }

    public void grow(ServerLevel pLevel, BlockState pState, BlockPos pPos, int i) {
        int k = Math.min(getAge(pState) + i, getMaxAge());
        if(this.canGrow(pLevel, pPos, pState, k) && (pLevel.getRandom().nextFloat() < 0.6F)) {
            pLevel.setBlock(pPos, pState.setValue(getAgeProperty(), k), 2);
            if(k >= AGE_TO_GROW_UP && isLower(pState)) {
                DyeColor dyeColor = Util.getRandom(DyeColor.values(), pLevel.random);
                pLevel.setBlock(pPos.above(), getUpperBlock().defaultBlockState().setValue(getAgeProperty(), k).setValue(MoreSnifferFlowerBlock.STALK_TYPE, pState.getValue(MoreSnifferFlowerBlock.STALK_TYPE)).setValue(MoreSnifferFlowerBlock.LEAVES_TYPE, pState.getValue(MoreSnifferFlowerBlock.LEAVES_TYPE)).setValue(ModStateProperties.COLOR, dyeColor), 3);
            }

            getLowerHalf(pLevel, pPos, pState).ifPresent(posAndState -> {

            });
        }
    }

    Optional<PosAndState> getLowerHalf(LevelReader level, BlockPos blockPos, BlockState state) {
        if(isLower(state)) {
            return Optional.of(new PosAndState(blockPos, state));
        } else {
            BlockPos posBelow = blockPos.below();
            BlockState stateBelow = level.getBlockState(posBelow);
            return isLower(stateBelow) ? Optional.of(new PosAndState(posBelow, stateBelow)) : Optional.empty();
        }
    }

    @Override
    public Map<DyeColor, Integer> colorValues() {
        return Util.make(Maps.newLinkedHashMap(), dyeColorHexFormatMap -> {
            dyeColorHexFormatMap.put(DyeColor.WHITE, 0xFFFFFFFF);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_GRAY, 0xFFd2cad8);
            dyeColorHexFormatMap.put(DyeColor.GRAY, 0xFFb0a4be);
            dyeColorHexFormatMap.put(DyeColor.BLACK, 0xFF837e9b);
            dyeColorHexFormatMap.put(DyeColor.BROWN, 0xFFd6a27c);
            dyeColorHexFormatMap.put(DyeColor.RED, 0xFFffaca6);
            dyeColorHexFormatMap.put(DyeColor.ORANGE, 0xFFffd180);
            dyeColorHexFormatMap.put(DyeColor.YELLOW, 0xFFfff07a);
            dyeColorHexFormatMap.put(DyeColor.LIME, 0xFFddff97);
            dyeColorHexFormatMap.put(DyeColor.GREEN, 0xFFa2ffb2);
            dyeColorHexFormatMap.put(DyeColor.CYAN, 0xFF9bffda);
            dyeColorHexFormatMap.put(DyeColor.LIGHT_BLUE, 0xFFbefffa);
            dyeColorHexFormatMap.put(DyeColor.BLUE, 0xFFa7cdff);
            dyeColorHexFormatMap.put(DyeColor.PURPLE, 0xFFccb0ff);
            dyeColorHexFormatMap.put(DyeColor.MAGENTA, 0xFFe9adff);
            dyeColorHexFormatMap.put(DyeColor.PINK, 0xFFffd7f7);
        });
    }

    @Override
    public Block getLowerBlock() {
        return ModBlocks.MORE_SNIFFER_FLOWER_LOWER.get();
    }

    @Override
    public Block getCorruptedLowerBlock() {
        return null;
    }

    @Override
    public Block getUpperBlock() {
        return ModBlocks.MORE_SNIFFER_FLOWER_UPPER.get();
    }

    @Override
    public Block getCorruptedUpperBlock() {
        return null;
    }
}
