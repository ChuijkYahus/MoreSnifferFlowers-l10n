package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.blocks.ModEntityBlock;
import net.abraxator.moresnifferflowers.blocks.SaltemoneBlock;
import net.abraxator.moresnifferflowers.entities.SaltBubbleProjectile;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.SaltemoneParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.NotNull;

public class SaltemoneBlockEntity extends AbstractMultiBlockEntity implements IModBlockEntity {
    public SaltemoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SALTEMONE.get(), pos, state);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox() {
        return new AABB(getCenter()).inflate(1);
    }

    @Override
    public void tick(Level level) {
        if (getLevel() == null) return;

        BlockState state = getBlockState();
        BlockPos pos = getBlockPos();
        SaltemoneBlock saltemoneBlock = (SaltemoneBlock) state.getBlock();
        RandomSource random = getLevel().getRandom();

        if (state.getValue(ModStateProperties.SHEARED)) return;

        if (!(getLevel().getGameTime() % 160 == 0 && random.nextFloat() < 0.20f)) return;

        if (pos.equals(this.getCenter())) {
            if (saltemoneBlock.isMaxAge(state)) {
                Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
                Vec3 vec3 = this.getCenter().getCenter().relative(direction, 0.5D).relative(direction.getClockWise(), 0.5D).relative(Direction.UP, 0.0);
                float speed = 0.2F;

                SaltBubbleProjectile projectile = new SaltBubbleProjectile(vec3.x, vec3.y, vec3.z, level);

                projectile.setNoGravity(true);
                projectile.setCorrupted(saltemoneBlock.isCorrupted());
                projectile.setState(0);
                projectile.setDeltaMovement((random.nextFloat() - 0.5)*speed,1*speed, (random.nextFloat() - 0.5)*speed);

                getLevel().addFreshEntity(projectile);

                ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SaltemoneParticlePacket(vec3.toVector3f()));

            } else {
                if (IMultiBlock.isCenter(state)) saltemoneBlock.performBonemeal((ServerLevel) getLevel(), random, pos, state);
            }
        }
    }
}
