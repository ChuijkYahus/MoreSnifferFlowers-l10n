package net.abraxator.moresnifferflowers.client.renderer.renderstate;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BoblingRenderState extends LivingEntityRenderState {
    public boolean isCured = false;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState plantingAnimationState = new AnimationState();

}
