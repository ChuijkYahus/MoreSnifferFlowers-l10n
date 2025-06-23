package net.abraxator.moresnifferflowers.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.abraxator.moresnifferflowers.blockentities.MultiBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ShutUpWarnsMixin implements ServerPlayerConnection, TickablePacketListener, ServerGamePacketListener {

    /*
    @Inject(method = "handleUseItemOn", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"), cancellable = true)
    public void shutupper(ServerboundUseItemOnPacket par1, CallbackInfo ci, @Local BlockPos pos, @Local ServerLevel level) {
        if(level.getBlockEntity(pos) instanceof MultiBlockEntity){
            ci.cancel();
        }
    }*/

    @ModifyVariable(method = "handleUseItemOn", at = @At(value = "STORE"), ordinal = 2)
    public Vec3 useAllower(Vec3 vec3, @Local ServerLevel level, @Local BlockPos pos) {
        if(level.getBlockEntity(pos) instanceof MultiBlockEntity){
            return new Vec3(0, 0 , 0);
        }
        return vec3;
    }

}
