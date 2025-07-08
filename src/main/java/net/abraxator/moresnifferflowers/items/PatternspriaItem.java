package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.ModColorHandler;
import net.abraxator.moresnifferflowers.components.BlockPattern;
import net.abraxator.moresnifferflowers.components.DyespriaMode;
import net.abraxator.moresnifferflowers.components.EntityDistanceComparator;
import net.abraxator.moresnifferflowers.components.PatternspriaMode;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PatternspriaItem extends Item {
    public PatternspriaItem(Properties properties) {
        super(properties);
    }

    private final Map<BlockPos, BlockPatternCapability.PatternData> cached_patterns = new HashMap<>();
    public static final int DEFAULT_COLOR = 0xA9948D;

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack stack = pContext.getItemInHand();
        Direction horizontalDirection = pContext.getHorizontalDirection();
        BlockPattern fromPatternspria = BlockPattern.fromPatternspria(stack);

        if (pContext.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (player.isCrouching() && BlockPatternCapability.hasPattern(blockPos, level)) {
            if (!stack.getOrCreateTag().contains("color")){
                stack.getOrCreateTag().putInt("color", -1);
            }
            if (stack.getOrCreateTag().getInt("color") != BlockPatternCapability.getPattern(blockPos, level).color() ) {
                copyColor(stack, level, blockPos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (blockState.is(ModBlocks.PATTERNFLOWER.get()) && fromPatternspria != BlockPattern.EMPTY){
            if (BlockPattern.fromState(blockState).equals(fromPatternspria)) return InteractionResult.PASS;
            level.setBlock(blockPos, blockState.setValue(ModStateProperties.BLOCK_PATTERN, fromPatternspria).setValue(ModStateProperties.EMPTY, false), 3);
            finishColoring(fromPatternspria.getItemStack(stack), level, stack, blockPos, pContext.getClickedFace());
            return InteractionResult.SUCCESS;
        }

        if (canUse(blockPos, level, stack)) {
            int oldCount = stack.getOrCreateTag().getInt("amount");
            AtomicInteger currentCount = new AtomicInteger(oldCount);
            AtomicBoolean canContinueDyeing = new AtomicBoolean(true);
            PatternspriaMode dyespriaMode = getMode(stack);
            PatternspriaMode.DyespriaSelector dyespriaSelector = new PatternspriaMode.DyespriaSelector(blockPos, level, pContext.getClickedFace());

            Set<BlockPos> set = dyespriaMode.getSelector().apply(dyespriaSelector);
            set.stream().sorted(new EntityDistanceComparator(blockPos)).takeWhile(t -> canContinueDyeing.get()).forEach(blockPos1 -> {
                var state = level.getBlockState(blockPos1);

                if(canUse(blockPos1, level, stack) && fromPatternspria != BlockPattern.EMPTY) {
                    patternOne(stack, level, blockPos1, fromPatternspria, pContext.getClickedFace(), horizontalDirection);
                    currentCount.getAndDecrement();

                } else if (stack.getOrCreateTag().getInt("amount") <= 0 || fromPatternspria == BlockPattern.EMPTY){
                    canContinueDyeing.set(false);
                }

                if (oldCount - currentCount.get() >= 64) {
                    canContinueDyeing.set(false);
                }
            });

            if (!level.isClientSide) {
                BlockPatternCapability.setBulkPatterns(cached_patterns, level);
                cached_patterns.clear();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockPattern pattern = fromPatternspria;
        if (BlockPatternCapability.hasPattern(blockPos, level) && pattern != BlockPattern.EMPTY ) {
            BlockPatternCapability.PatternData patternData = BlockPatternCapability.getPattern(blockPos, level);
            if (!patternData.direction().equals(horizontalDirection) && pattern.getId() == patternData.patternId()){
                BlockPatternCapability.setPattern(blockPos,new BlockPatternCapability.PatternData(patternData.patternId(), patternData.color(), horizontalDirection, patternData.isGlowing() ) ,  level);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(pContext);
    }


    public PatternspriaMode getMode(ItemStack stack) {
        return PatternspriaMode.byIndex(stack.getOrCreateTag().getByte("mode"));
    }

    public boolean patternOne(ItemStack stack, Level level, BlockPos blockPos, BlockPattern pattern, Direction face, Direction horizontalDirection) {
        if (!canUse(blockPos, level, stack) && pattern == BlockPattern.EMPTY) {
            return false;
        }

        int color = getColor(stack);
        if (BlockPatternCapability.hasPattern(blockPos, level)) color = BlockPatternCapability.getPattern(blockPos, level).color();

        if (!level.isClientSide) {
            cached_patterns.put(blockPos.immutable(), new BlockPatternCapability.PatternData(pattern.getId(), color, horizontalDirection, false));
        }

        finishColoring(pattern.getItemStack(stack), level, stack, blockPos, face);

        return true;
    }

    private boolean canUse(BlockPos pos, Level level, ItemStack patternspria) {

        if (BlockPattern.fromPatternspria(patternspria) == BlockPattern.EMPTY) return false;
        int groundId = -1;
        if (BlockPatternCapability.hasPattern(pos, level)){
            groundId = BlockPatternCapability.getPattern(pos, level).patternId();
        }
        int inputId = BlockPattern.fromPatternspria(patternspria).getId();


        boolean isSturdy = false;
        for(Direction dir : Direction.values()) {
            if (level.getBlockState(pos).isFaceSturdy(level, pos, dir)) {
                isSturdy = true;
                break;
            }
        }

        return (!BlockPatternCapability.hasPattern(pos, level) || inputId != groundId)
                && patternspria.getOrCreateTag().getInt("amount") > 0 && isSturdy;
    }

    public void finishColoring(ItemStack blockPattern, Level level, ItemStack patternspria, BlockPos blockPos, Direction face) {
        int uses = getPatternspriaUses(patternspria) - 1;
        BlockPattern pattern = BlockPattern.fromItem(blockPattern.getItem());

        if(uses <= 0) {
            blockPattern.shrink(1);
            if (blockPattern.isEmpty()) {
                BlockPattern.removePatternFromStack(patternspria);
            } else {
                setPatternspriaUses(patternspria, 4);
            }
        } else {
            setPatternspriaUses(patternspria, uses);
        }


        BlockPattern.setPatternToHolderStack(patternspria, blockPattern, blockPattern.getCount(), getPatternspriaUses(patternspria));
        if (level.isClientSide && pattern != BlockPattern.EMPTY) {
            spawnParticles(level.getRandom(), level, pattern, blockPos, face, patternspria);
        }
    }

    public ItemStack addPattern(ItemStack patternspria, ItemStack itemToInsert) {
        BlockPattern pattern = BlockPattern.fromItem(itemToInsert.getItem());
        BlockPattern patternInside = BlockPattern.fromPatternspria(patternspria);

        if (patternInside == BlockPattern.EMPTY){
            BlockPattern.removePatternFromStack(patternspria);
        }

        if (pattern == BlockPattern.EMPTY) {
            return itemToInsert;
        }

        if (patternInside == BlockPattern.EMPTY || patternspria.getOrCreateTag().getInt("amount") <= 0) {
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            return ItemStack.EMPTY;
        }

        if (!pattern.isSamePattern(patternspria)) {
            ItemStack returnStack = patternInside.getItemStack(patternspria);
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            return returnStack;
        }

        int amountInside = patternspria.getOrCreateTag().getInt("amount");
        int freeSpace = 64 - amountInside;

        if (freeSpace <= 0) {
            return itemToInsert;
        }

        int amountToAdd = Math.min(itemToInsert.getCount(), freeSpace);
        onAddPattern(patternspria, itemToInsert, amountInside + amountToAdd);
        itemToInsert.shrink(amountToAdd);

        return itemToInsert;
    }

    public void onAddPattern(ItemStack destinationStack, ItemStack pattern, int amount) {
        destinationStack.getOrCreateTag().remove("color");
        BlockPattern.setPatternToHolderStack(destinationStack, pattern, amount);
    }

    private ItemStack removePattern(ItemStack pStack) {
        var pattern = BlockPattern.fromPatternspria(pStack);
        int uses = getPatternspriaUses(pStack);

        if(pattern != BlockPattern.EMPTY) {
            ItemStack returnStack = pattern.getItemStack(pStack);
            BlockPattern.removePatternFromStack(pStack);
            returnStack.shrink(uses == 4 ? 0 : 1);
            return returnStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if(pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if(pOther.isEmpty()) {
                pAccess.set(removePattern(pStack));
                playRemoveOneSound(pPlayer);
            } else {
                ItemStack itemStack = addPattern(pStack, pOther);
                pAccess.set(itemStack);
                if(itemStack.isEmpty()) {
                    this.playInsertSound(pPlayer);
                }
            }
            return true;
        }
        return false;
    }

    public void copyColor(ItemStack patternspria, Level level, BlockPos blockPos){
        if (BlockPatternCapability.hasPattern(blockPos, level)) {
            BlockPatternCapability.PatternData patternData = BlockPatternCapability.getPattern(blockPos, level);
            patternspria.getOrCreateTag().putInt("color", patternData.color());
        }
    }

    public int getColor(ItemStack patternspria){
        if (patternspria.getOrCreateTag().contains("color")) return patternspria.getOrCreateTag().getInt("color");
        return DEFAULT_COLOR;
    }


    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int input = getPatternspriaUses(stack)-1;
        int maxInput= 4;

        return ModColorHandler.barColorHelper(input, maxInput);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BlockPattern.fromPatternspria(stack) != BlockPattern.EMPTY;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getPatternspriaUses(stack) * 13.0F / 4);
    }

    public static int getPatternspriaUses(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (tag.contains("uses")){
            int uses = tag.getInt("uses");
            if (uses > 4 || uses < 0) {
                MoreSnifferFlowers.LOGGER.warn("Invalid uses speed for patternspria: " + uses);
                tag.putInt("uses", 4);
                return 4;
            }
            return uses;
        }
        return 4;
    }

    public static void setPatternspriaUses(ItemStack stack, int uses) {
        var tag = stack.getOrCreateTag();
        if (tag.contains("uses") && tag.getInt("uses") < 0 || tag.getInt("uses") > 4) {
            MoreSnifferFlowers.LOGGER.warn("Invalid uses speed for patternspria: " + tag.getInt("uses") + "new: " + uses);
            tag.putInt("uses", 4);
            return;
        }
        tag.putInt("uses", uses);
        stack.setTag(tag);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        BlockPattern pattern = BlockPattern.fromPatternspria(pStack);
        Component usage = Component.translatableWithFallback("tooltip.patternspria.usage", "Right click with dye to insert \nRight click caulorflower to repaint \nSneak to apply to the whole column \n").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(usage.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> pTooltipComponents.add(Component.literal(s).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(getMode(pStack).ordinal())));
        pTooltipComponents.add(Component.empty());

        if(pattern != BlockPattern.EMPTY) {
            ItemStack patternStack = pattern.getItemStack(pStack);
            var name = Component
                    .literal(patternStack.getCount() + " - " + WordUtils.capitalizeFully(pattern.getSerializedName()
                            .toLowerCase()
                            .replaceAll("[^a-z_]", "")
                            .replaceAll("_", " ")))
                    .withStyle(Style.EMPTY
                            .withColor(pattern.getColor()));
            pTooltipComponents.add(name);
        } else {
            pTooltipComponents.add(Component.translatableWithFallback("tooltip.dyespria.empty", "Empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public void spawnParticles(RandomSource randomSource, Level level, BlockPattern pattern, BlockPos blockPos, Direction face, ItemStack stack) {
        Vector3f vector3f = blockPos.getCenter().toVector3f();
        if (face != null) vector3f = vector3f.add(face.step().div(new Vector3f(2,2,2)));
        int color = stack.getOrCreateTag().contains("color") ? stack.getOrCreateTag().getInt("color") : pattern.getColor();
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.addParticle(
                    new DustParticleOptions(Vec3.fromRGB24(color).toVector3f(), 1.0F),
                    vector3f.x + randomSource.nextDouble() - 0.5D,
                    vector3f.y + randomSource.nextDouble() - 0.5D,
                    vector3f.z + randomSource.nextDouble() - 0.5D,
                    0, 0, 0);
        }
    }

    public void changeMode(ServerPlayer player, ItemStack stack, int amount) {
        var currentMode = getMode(stack);
        var newMode = PatternspriaMode.shift(currentMode, amount);
        stack.getOrCreateTag().putByte("mode", (byte) newMode.ordinal());
        player.displayClientMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(newMode.ordinal())), true);

    }
}
