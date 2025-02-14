package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class SimpleAdvancementTrigger extends SimpleCriterionTrigger<SimpleAdvancementTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                .apply(instance, TriggerInstance::new));


        public static Criterion<?> usedDyespria() {
            return ModAdvancementCritters.USED_DYESPRIA.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> usedBonmeel() {
            return ModAdvancementCritters.USED_BONMEEL.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> placedDyespriaPlant() {
            return ModAdvancementCritters.PLACED_DYESPRIA_PLANT.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> boblingAttack() {
            return ModAdvancementCritters.BOBLING_ATTACK.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> dyeBoat() {
            return ModAdvancementCritters.DYE_BOAT.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> usedCure() {
            return ModAdvancementCritters.USED_CURE.get().createCriterion(new TriggerInstance(Optional.empty()));
        }

        public static Criterion<?> corruptedBlock() {
            return ModAdvancementCritters.CORRUPTED_BLOCK.get().createCriterion(new TriggerInstance(Optional.empty()));
        }
    }
}
