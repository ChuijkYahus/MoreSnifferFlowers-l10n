package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.components.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PatternspriaItem extends Item {
    public PatternspriaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack stack = pContext.getItemInHand();
        Dye dye = Dye.getDyeFromDyespria(stack);

        if (pContext.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
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

                if(canUse(blockPos1, level, stack)) {
                    patternOne(stack, level, blockPos1, state, pContext.getClickedFace());
                    currentCount.getAndDecrement();

                } else if (stack.getOrCreateTag().getInt("amount") <= 0 || BlockPattern.fromPatternspria(stack) == null){
                    canContinueDyeing.set(false);
                }

                if (oldCount - currentCount.get() >= 16) {
                    canContinueDyeing.set(false);
                }
            });

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(pContext);
    }


    public PatternspriaMode getMode(ItemStack stack) {
        return PatternspriaMode.byIndex(stack.getOrCreateTag().getByte("mode"));
    }

    public boolean patternOne(ItemStack stack, Level level, BlockPos blockPos, BlockState blockState, Direction face) {
        BlockPattern pattern = BlockPattern.fromPatternspria(stack);
        BlockPatternCapability blockPatterns = CapabilityList.getBlockPatterns();

        if (!canUse(blockPos, level, stack) && pattern == null) {
            return false;
        }

        int color = 0xA9948D;
        if (blockPatterns.hasPattern(blockPos, level)) color = blockPatterns.getPattern(blockPos, level).color();

        blockPatterns.setPattern(blockPos, new BlockPatternCapability.PatternData(pattern.ordinal(), color), level);
        finishColoring(pattern.getItemStack(stack), level, stack, blockPos, face);

        return true;
    }

    private boolean canUse(BlockPos pos, Level level, ItemStack patternspria) {
        if (BlockPattern.fromPatternspria(patternspria) == null) return false;
        int groundId = -1;
        if (CapabilityList.getBlockPatterns().hasPattern(pos, level)){
            groundId = CapabilityList.getBlockPatterns().getPattern(pos, level).patternId();
        }
        int inputId = BlockPattern.fromPatternspria(patternspria).ordinal();

        boolean isSturdy = false;
        for(Direction dir : Direction.values()) {
            if (level.getBlockState(pos).isFaceSturdy(level, pos, dir)) {
                isSturdy = true;
                break;
            }
        }
        return (!CapabilityList.getBlockPatterns().hasPattern(pos, level) || inputId != groundId)
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
        if (level.isClientSide && pattern != null) {
            spawnParticles(level.getRandom(), level, pattern, blockPos, face);
        }
    }

    public ItemStack addPattern(ItemStack patternspria, ItemStack itemToInsert) {
        BlockPattern pattern = BlockPattern.fromItem(itemToInsert.getItem());
        BlockPattern patternInside = BlockPattern.fromPatternspria(patternspria);

        if (patternInside == null){
            BlockPattern.removePatternFromStack(patternspria);
        }

        if (pattern == null) {
            return itemToInsert;
        }

        if (patternInside == null || patternspria.getOrCreateTag().getInt("amount") <= 0) {
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            return ItemStack.EMPTY;
        }

        if (!pattern.isSamePattern(patternspria)) {
            onAddPattern(patternspria, itemToInsert, itemToInsert.getCount());
            itemToInsert.shrink(itemToInsert.getCount());
            return patternInside.getItemStack(patternspria);
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

    public void onAddPattern(@Nullable ItemStack destinationStack, ItemStack pattern, int amount) {
        BlockPattern.setPatternToHolderStack(destinationStack, pattern, amount);
    }

    private ItemStack removePattern(ItemStack pStack) {
        var pattern = BlockPattern.fromPatternspria(pStack);
        int uses = getPatternspriaUses(pStack);

        if(pattern != null) {
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


    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int lowColor = 0x8c1111;
        int highColor = 0x179529;
        int input = getPatternspriaUses(stack)-1;
        int maxInput= 4;

        int lowRed = (lowColor >> 16) & 0xFF;
        int lowGreen = (lowColor >> 8) & 0xFF;
        int lowBlue = lowColor & 0xFF;

        int highRed = (highColor >> 16) & 0xFF;
        int highGreen = (highColor >> 8) & 0xFF;
        int highBlue = highColor & 0xFF;

        float[] lowHSB =  Color.RGBtoHSB(lowRed, lowGreen, lowBlue, null);
        float[] highHSB =  Color.RGBtoHSB(highRed, highGreen, highBlue, null);


        float finalHue = ((lowHSB[0] * (Math.abs(input - maxInput))) + (highHSB[0] * input)) / maxInput;

        return Mth.hsvToRgb(finalHue, 1.0F, 1.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return BlockPattern.fromPatternspria(stack) != null;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getPatternspriaUses(stack) * 13.0F / 4);
    }

    public static int getPatternspriaUses(ItemStack stack) {
        return DyespriaItem.getDyespriaUses(stack);
    }

    public static void setPatternspriaUses(ItemStack stack, int uses) {
        DyespriaItem.setDyespriaUses(stack, uses);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        BlockPattern pattern = BlockPattern.fromPatternspria(pStack);
        Component usage = Component.translatableWithFallback("tooltip.dyespria.usage", "Right click with dye to insert \nRight click caulorflower to repaint \nSneak to apply to the whole column \n").withStyle(ChatFormatting.GOLD);
        var usageComponents = Arrays.stream(usage.getString().split("\n", -1))
                .filter(s -> !s.isEmpty())
                .map(String::trim);

        usageComponents.forEach(s -> pTooltipComponents.add(Component.literal(s).withStyle(ChatFormatting.GOLD)));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(getMode(pStack).ordinal())));
        pTooltipComponents.add(Component.empty());

        if(pattern != null) {
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

    public void spawnParticles(RandomSource randomSource, Level level, BlockPattern pattern, BlockPos blockPos, Direction face) {
        Vector3f vector3f = blockPos.getCenter().toVector3f();
        if (face != null) vector3f = vector3f.add(face.step().div(new Vector3f(2,2,2)));
        for(int i = 0; i <= randomSource.nextIntBetweenInclusive(5, 10); i++) {
            level.addParticle(
                    new DustParticleOptions(Vec3.fromRGB24(pattern.getColor()).toVector3f(), 1.0F),
                    vector3f.x + randomSource.nextDouble() - 0.5D,
                    vector3f.y + randomSource.nextDouble() - 0.5D,
                    vector3f.z + randomSource.nextDouble() - 0.5D,
                    0, 0, 0);
        }
    }

    public void changeMode(ServerPlayer player, ItemStack stack, int amount) {
        var currentMode = getMode(stack);
        var newMode = PatternspriaMode.shift(currentMode, amount);
        var tag = stack.getOrCreateTag();
        tag.putByte("mode", (byte) newMode.ordinal());
        stack.setTag(tag);
        player.displayClientMessage(DyespriaItem.getCurrentModeComponent(DyespriaMode.byIndex(newMode.ordinal())), true);
    }
}
