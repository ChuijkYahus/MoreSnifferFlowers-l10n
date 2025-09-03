package net.abraxator.moresnifferflowers.blockentities;

import net.abraxator.moresnifferflowers.entities.CorruptedProjectile;
import net.abraxator.moresnifferflowers.init.ModBlockEntities;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModStateProperties;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.abraxator.moresnifferflowers.init.config.ModServerConfig;
import net.abraxator.moresnifferflowers.networking.toClient.CorruptedSludgePacket;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.recipes.CorruptionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CorruptedSludgeBlockEntity extends ModBlockEntity implements GameEventListener.Holder<CorruptedSludgeBlockEntity.CorruptedSludgeListener> {
    public CorruptedSludgeListener corruptedSludgeListener;
    public int usesLeft = -1;
    public int stateChange;
    public GameEventListener listener;
    
    public CorruptedSludgeBlockEntity(BlockPos pos, BlockState pBlockState) {
        super(ModBlockEntities.CORRUPTED_SLUDGE.get(), pos, pBlockState);
        this.corruptedSludgeListener = new CorruptedSludgeListener(new BlockPositionSource(pos));
        this.listener = new CorruptedSludgeListener(new BlockPositionSource(pos));
    }

    public void updateUses() {
        this.usesLeft--;

        if (stateChange == 0){
            stateChange = usesLeft / 4;
        }

        if(this.usesLeft % stateChange == 0 && this.getBlockState().getValue(ModStateProperties.USES_4) - 1 != -1) {
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(ModStateProperties.USES_4, this.getBlockState().getValue(ModStateProperties.USES_4) - 1));
        }

        if(this.usesLeft <= 0) {
            if (ModServerConfig.CORRUPTED_SLUDGE_GRIEFING.get()) CorruptedSludgeListener.shootProjectiles(this.getBlockPos().getCenter(), this.level.random.nextIntBetweenInclusive(8, 16), this.level);
            super.setRemoved();
            this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
        }

    }
    
    @Override
    public CorruptedSludgeListener getListener() {
        return corruptedSludgeListener;
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("uses", usesLeft);
        tag.putInt("stateChange", stateChange);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.usesLeft = tag.getInt("uses");
        this.stateChange = tag.getInt("stateChange");
    }

    public static class CorruptedSludgeListener implements GameEventListener {
        private PositionSource positionSource;

        public CorruptedSludgeListener(PositionSource positionSource) {
            this.positionSource = positionSource;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return GameEvent.BLOCK_DESTROY.getNotificationRadius();
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, GameEvent pGameEvent, GameEvent.Context context, Vec3 pos) {
            CorruptedSludgeBlockEntity entity;

            if (context.affectedState() == null || !CorruptionRecipe.canBeCorrupted(context.affectedState().getBlock(), level)) return false;
            
            if(level.getBlockEntity(BlockPos.containing(this.positionSource.getPosition(level).get())) instanceof CorruptedSludgeBlockEntity entity1) {
                entity = entity1;
            } else return false;
            
            boolean validEvent = (pGameEvent != GameEvent.BLOCK_PLACE || pGameEvent != GameEvent.BLOCK_DESTROY);
            
            if (entity.usesLeft == -1) {
                entity.usesLeft = level.random.nextIntBetweenInclusive(16, 32) - 1;
                entity.stateChange = entity.usesLeft / 4;
            }
            
            if(entity.usesLeft <= 0 || entity.getBlockState().getValue(ModStateProperties.CURED) || !validEvent) {
                return false;
            }

            if(pGameEvent == GameEvent.BLOCK_PLACE && CorruptionRecipe.canBeCorrupted(context.affectedState().getBlock(), level)) {
                Vec3 startPos = this.getListenerSource().getPosition(level).get();         
                Vec3 dirNormal = new Vec3(pos.x - startPos.x, pos.y - startPos.y, pos.z - startPos.z).normalize();
                Optional<Block> corrupted = CorruptionRecipe.getCorruptedBlock(context.affectedState().getBlock(), level);
                BlockPos blockPos = BlockPos.containing(pos);
                corrupted.ifPresent(block -> {
                    ModPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> 
                            new PacketDistributor.TargetPoint(startPos.x, startPos.y, startPos.z, 64, level.dimension())
                    ), new CorruptedSludgePacket(startPos.toVector3f(), pos.toVector3f(), dirNormal.toVector3f()));
                    if(level.getBlockState(BlockPos.containing(pos)).getBlock() instanceof net.abraxator.moresnifferflowers.blocks.Corruptable corruptable) {
                        corruptable.onCorrupt(level, blockPos, level.getBlockState(BlockPos.containing(pos)), block);
                    } else {
                        level.setBlockAndUpdate(BlockPos.containing(pos), block.withPropertiesOf(context.affectedState()));
                    }
                    level.sendParticles(
                            new DustParticleOptions(Vec3.fromRGB24(0x0443248).toVector3f(), 1.0F),
                            blockPos.getX() + level.random.nextDouble(), blockPos.getY() + level.random.nextDouble(), blockPos.getZ() + level.random.nextDouble(),
                            10,
                            0.0D, 0.0D, 0.0D,
                            0.0D
                    );

                    entity.updateUses();
                });

                return !corrupted.isPresent();
            }

            if (ModServerConfig.CORRUPTED_SLUDGE_GRIEFING.get()) {
                if (pGameEvent == GameEvent.BLOCK_DESTROY && context.affectedState().is(ModTags.ModBlockTags.CORRUPTED_SLUDGE) && !pos.equals(this.positionSource.getPosition(level).get())) {
                    var projectileNumber = (context.affectedState().is(ModBlocks.CORRUPTED_LEAVES.get()) || context.affectedState().is(ModBlocks.CORRUPTED_LEAVES_BUSH.get()) ? level.random.nextInt(1) : level.random.nextInt(5)) + 2;
                    shootProjectiles(this.positionSource.getPosition(level).get(), projectileNumber, level);
                    entity.updateUses();
                    return false;
                }
            }

            return false;
        }
        
        public static void shootProjectiles(Vec3 center, int projectileNumber, Level level) {
            var radius = 2.5;
            Set<Vec3> placed = new HashSet<>();

            for(int i = 0; i < projectileNumber; i++) {
                generatePoint(placed, center, radius, level);
            }
        }

        private static void generatePoint(Set<Vec3> placed, Vec3 center, double radius, Level level) {
            var random = level.random;

            double theta = 2 * Mth.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xg = center.x + radius * Mth.sin((float) phi) * Mth.cos((float) theta);
            double yg = center.y + radius * Mth.sin((float) phi) * Mth.sin((float) theta);
            double zg = center.z + radius * Mth.cos((float) phi);
            var vec3 = new Vec3(xg, yg, zg);
            
            if (placed.stream().noneMatch(vec31 -> AABB.ofSize(vec3, 1, 1, 1).contains(vec31)) && level.getBlockState(BlockPos.containing(vec3)).canBeReplaced()) {
                var pos = center;
                var x = random.nextDouble() * 0.5;
                var y = random.nextDouble() * 0.5;
                var z = random.nextDouble() * 0.5;
                CorruptedProjectile projectile = new CorruptedProjectile(level);
                projectile.setPos(vec3);
                Vec3 dir = new Vec3(projectile.getX() - pos.x, projectile.getY() - pos.y, projectile.getZ() - pos.z).normalize().multiply(x, y, z);
                projectile.setDeltaMovement(dir);
                level.addFreshEntity(projectile);

                //level.sendParticles(ModParticles.CARROT.get(), vec3.x, vec3.y, vec3.z, 1, 0D, 0D, 0D, 0D);
                placed.add(vec3);
            }
        }
    }
}