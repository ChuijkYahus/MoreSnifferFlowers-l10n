package net.abraxator.moresnifferflowers.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class ModAdvancementCritters {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> EARN_SNIFFER_ADVANCEMENT = TRIGGERS.register("earn_sniffer_advancement", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> USED_DYESPRIA = TRIGGERS.register("used_dyespria", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> USED_BONMEEL = TRIGGERS.register("used_bonmeel", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> PLACED_DYESPRIA_PLANT = TRIGGERS.register("placed_dyespria_plant", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> BOBLING_ATTACK = TRIGGERS.register("bobling_attack", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> DYE_BOAT = TRIGGERS.register("dye_boat", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> USED_CURE = TRIGGERS.register("used_cure", SimpleAdvancementTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, SimpleAdvancementTrigger> CORRUPTED_BLOCK = TRIGGERS.register("corrupted_block", SimpleAdvancementTrigger::new);

}

