package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.toClient.CorruptionParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CorruptionCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation ID = MoreSnifferFlowers.loc("corruption");
    public static final int MAX_RESISTANCE = 5;
    public static final int MAX_CORRUPTION = 150;

    private final LazyOptional<CorruptionCapability> optional = LazyOptional.of(() -> this);
    public int count = 0;
    public boolean isSource = false;
    public boolean isNeighbor = false;
    public int resistance = 0;
    public Set<BlockPos> flowers = new HashSet<>();

    public static void sendFlowerParticles(LevelChunk chunk){
        chunk.getCapability(CapabilityList.CORRUPTION).ifPresent(cap -> {
            int count = 0;

            for (BlockPos pos : cap.flowers){
                ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(),new CorruptionParticlePacket(pos, true, true));

                count++;
                if (count >= MAX_RESISTANCE) return;
            }
        });
    }

    public static void onCorruptionSource(Level level, BlockPos pos){
        LevelChunk chunk = level.getChunkAt(pos);
        chunk.getCapability(CapabilityList.CORRUPTION).ifPresent(cap -> {
            boolean hasResistance = cap.resistance > 0;
            cap.isSource = !hasResistance;
            cap.isNeighbor = true;
            cap.count++;

            if (hasResistance) {
                CorruptionCapability.sendFlowerParticles(chunk);
            }
        });
    }

    public static void cure(LevelChunk chunk){
        chunk.getCapability(CapabilityList.CORRUPTION).ifPresent(cap -> {
            cap.count = 0;
            cap.isSource = false;
            cap.isNeighbor = false;
        });
    }

    public static boolean areDifferentChunks(Level level, BlockPos pos1, BlockPos pos2) {
       return !level.getChunkAt(pos1).getPos().equals(level.getChunkAt(pos2).getPos());
    }

    public static CorruptionCapability get(LevelChunk chunk) {
        return chunk.getCapability(CapabilityList.CORRUPTION).orElseThrow(IllegalStateException::new);
    }

    public static void printDebug(LevelChunk chunk){
        if (chunk.getLevel().isClientSide()) return;
        chunk.getCapability(CapabilityList.CORRUPTION).ifPresent(cap ->{
            MoreSnifferFlowers.LOGGER.debug("Count: " + cap.count + " Resistance: " + cap.resistance + " isSource: " + cap.isSource + " isNeighbor: " + cap.isNeighbor + " Flowers size: " + cap.flowers.size());
        });
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityList.CORRUPTION.orEmpty(cap, optional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("count", count);
        tag.putInt("resistance", resistance);
        tag.putBoolean("isSource", isSource);
        tag.putBoolean("isNeighbor", isNeighbor);

        ListTag listTag = new ListTag();
        for (BlockPos pos : flowers){
            CompoundTag posTag = NbtUtils.writeBlockPos(pos);
            listTag.add(posTag);
        }
        tag.put("flowers", listTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        count = nbt.getInt("count");
        resistance = nbt.getInt("resistance");
        isSource = nbt.getBoolean("isSource");
        isNeighbor = nbt.getBoolean("isNeighbor");

        flowers.clear();
        ListTag listTag = nbt.getList("flowers", 10);
        for (int i = 0; i < listTag.size(); ++i) {
            flowers.add(NbtUtils.readBlockPos(listTag.getCompound(i)));
        }

    }
}
