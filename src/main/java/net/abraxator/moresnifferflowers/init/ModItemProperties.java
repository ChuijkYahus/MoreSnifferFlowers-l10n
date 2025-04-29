package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.MapCodec;
import net.abraxator.moresnifferflowers.components.Dye;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ModItemProperties {

    public record DyespriaConditionalProperty() implements ConditionalItemModelProperty{
        public static final MapCodec<DyespriaConditionalProperty> MAP_CODEC = MapCodec.unit(new DyespriaConditionalProperty());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
            return !Dye.getDyeFromDyespria(stack).isEmpty();
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

    public record OGConditionalProperty() implements ConditionalItemModelProperty{
        public static final MapCodec<OGConditionalProperty> MAP_CODEC = MapCodec.unit(new OGConditionalProperty());

        @Override
        public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
            Component component = stack.get(DataComponents.CUSTOM_NAME);

            return component != null && component.getString().equals("og");
        }

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }
    }

/*    public static void register() {
        ItemProperties.register(ModItems.DYESPRIA.get(), MoreSnifferFlowers.loc("color"), (pStack, pLevel, pEntity, pSeed) -> {
            if(!Dye.getDyeFromDyespria(pStack).isEmpty()) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.DRAGONFLY.get(), MoreSnifferFlowers.loc("og"), (pStack, pLevel, pEntity, pSeed) -> {
            Component component = pStack.get(DataComponents.CUSTOM_NAME);
            if(component != null && component.getString().equals("og")) {
                return 1.0F;
            } else {
                return 0.0F;
            }
        });
    }*/
}
