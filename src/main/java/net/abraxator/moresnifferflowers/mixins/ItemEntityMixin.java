package net.abraxator.moresnifferflowers.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {
    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci){
/*        Player player = this.level().getNearestPlayer(this, 4D);
        if (player != null && player.hasEffect(ModMobEffects.STICKY.get()) && !this.level().isClientSide) {
            setDeltaMovement(getDeltaMovement().add(player.getDeltaMovement()));
        }*/
    }

}
