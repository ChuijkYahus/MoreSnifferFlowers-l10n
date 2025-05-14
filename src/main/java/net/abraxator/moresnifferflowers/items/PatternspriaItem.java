package net.abraxator.moresnifferflowers.items;

import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PatternspriaItem extends Item {
    public PatternspriaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPatternCapability capability = CapabilityList.getBlockPatterns();
        if (!capability.hasPattern(pos, level)){
            capability.setPattern(pos,  new BlockPatternCapability.PatternData(1, DyeColor.CYAN), level);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
