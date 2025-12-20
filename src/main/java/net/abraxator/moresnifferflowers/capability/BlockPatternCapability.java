package net.abraxator.moresnifferflowers.capability;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.DirectionStorageHelper;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.StreamCodec;
import net.abraxator.moresnifferflowers.networking.toClient.SyncBlockPatternsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPatternCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public final Map<BlockPos, PatternData> patterns = new HashMap<>();
    private final LazyOptional<BlockPatternCapability> optional = LazyOptional.of(() -> this);
    ResourceLocation ID = MoreSnifferFlowers.loc("block_patterns");


    public static final StreamCodec<BlockPatternCapability> STREAM_CODEC =
            StreamCodec.composite(
                    StreamCodec.map(HashMap::new, StreamCodec.BLOCK_POS, PatternData.STREAM_CODEC), (cap -> cap.patterns),
                    BlockPatternCapability::create
            );

    public static BlockPatternCapability create(Map<BlockPos, PatternData> patterns) {
        BlockPatternCapability blockPatternCapability = new BlockPatternCapability();
        blockPatternCapability.patterns.putAll(patterns);
        return blockPatternCapability;
    }

    public static BlockPatternCapability getBlockPatterns(BlockPos pos, Level level) {
        LevelChunk levelChunk = (LevelChunk) level.getChunk(pos);
        return levelChunk.getCapability(CapabilityList.BLOCK_PATTERNS).orElse(new BlockPatternCapability());
    }

    public static void setPattern(BlockPos pos, PatternData pattern, Level level) {
        BlockPatternCapability capability = getBlockPatterns(pos, level);
        ChunkPos chunkPos = new ChunkPos(pos);
        level.getChunkAt(pos).setUnsaved(true);


        capability.setPattern(pos, pattern);
        if (!level.isClientSide) capability.sync(chunkPos);
    }

    public void setPattern(BlockPos pos, PatternData pattern) {
        patterns.put(pos.immutable(), pattern);
    }

    public static void setBulkPatterns(Map<BlockPos, PatternData> patternMap, Level level) {
        if (level.isClientSide) return;

        for (var entry : patternMap.entrySet()) {
            BlockPos pos = entry.getKey();
            PatternData patternData = entry.getValue();

            BlockPatternCapability capability = getBlockPatterns(pos, level);
            capability.setPattern(pos, patternData);
            level.getChunkAt(pos).setUnsaved(true);
        }

        Set<BlockPos> blockPosList = patternMap.keySet();
        Set<ChunkPos> chunkPositions = blockPosList.stream()
                .map(ChunkPos::new)
                .collect(Collectors.toSet());

        List<LevelChunk> levelChunks = chunkPositions.stream()
                .map(pos -> level.getChunk(pos.x, pos.z))
                .toList();

        for (LevelChunk levelChunk : levelChunks) {
            levelChunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.sync(levelChunk.getPos()));
        }
    }


    public static PatternData getPattern(BlockPos pos, Level level){
        BlockPatternCapability capability = getBlockPatterns(pos, level);
        return capability.getPattern(pos);
    }

    public PatternData getPattern(BlockPos pos) {
        return patterns.get(pos);
    }

    public static boolean hasPattern(BlockPos pos, Level level){
        BlockPatternCapability capability = getBlockPatterns(pos, level);
        return capability.hasPattern(pos);
    }

    public boolean hasPattern(BlockPos pos) {
        return patterns.containsKey(pos);
    }

    public static void removePattern(BlockPos pos, Level level) {
        BlockPatternCapability capability = getBlockPatterns(pos, level);
        capability.removePattern(pos);
        level.getChunkAt(pos).setUnsaved(true);

        if (level instanceof ServerLevel serverLevel) {
           capability.sync(new ChunkPos(pos));
        }
    }

    public void removePattern(BlockPos pos) {
        patterns.remove(pos);
    }

    public Stream<BlockPos> getPatternPositionsNear(BlockPos pos, int renderDistance) {
        return patterns.keySet().stream().filter(p -> p.closerThan(pos, renderDistance)) ;
    }

    public boolean isEmpty() {
        return patterns.isEmpty();
    }

    public void sync(ChunkPos pos){
        ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncBlockPatternsPacket(this, pos));
    }

    public int count() {
        return patterns.size();
    }

    public static void recolor(Level level, BlockPos pos, int color) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, color, data.direction, data.isGlowing), level);
        level.getChunkAt(pos).setUnsaved(true);
    }

    public static void enableGlowing(Level level, BlockPos pos) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, data.color, data.direction, true), level);
        level.getChunkAt(pos).setUnsaved(true);
    }

    // dimension=dim , pos=2, data=3, patterns=all
    public CompoundTag save() {
       return save(new CompoundTag());
    }

    public CompoundTag save(CompoundTag tag) {
        ListTag patternList = new ListTag();

        for (var entry : patterns.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
            entryTag.put("data", entry.getValue().save());
            patternList.add(entryTag);
        }

        tag.put("all", patternList);
     //   MoreSnifferFlowers.LOGGER.info("Tag=" + tag);
        return tag;
    }

    public void load(CompoundTag tag) {
        patterns.clear();
        ListTag patternList = tag.getList("all", Tag.TAG_COMPOUND);
        for (Tag tag1 : patternList) {
                CompoundTag entry = (CompoundTag) tag1;
                BlockPos pos = NbtUtils.readBlockPos(entry.getCompound("pos"));
                PatternData data = PatternData.load(entry.getCompound("data"));
                patterns.put(pos, data);
        }
    }

    public void load(Map<BlockPos, PatternData> patterns) {
       this.patterns.putAll(patterns);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityList.BLOCK_PATTERNS ? optional.cast() : LazyOptional.empty() ;
    }

    @Override
    public CompoundTag serializeNBT() {
        return save();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        load(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }

    // pattern=pat color=6, direction=dir, glowing=glow

    public record PatternData(int patternId, int color, Direction direction, boolean isGlowing) {

        public static final StreamCodec<PatternData> STREAM_CODEC =
                StreamCodec.composite(
                        StreamCodec.INT, PatternData::patternId,
                        StreamCodec.INT, PatternData::color,
                        StreamCodec.DIRECTION, PatternData::direction,
                        StreamCodec.BOOLEAN, PatternData::isGlowing,
                        PatternData::new
                );


        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("pat", patternId);
            tag.putInt("color", color);
            tag.putInt("dir", DirectionStorageHelper.directionToInt(direction));
            tag.putBoolean("glow", isGlowing);
            return tag;
        }

        public static PatternData load(CompoundTag tag) {
            int patternId = tag.getInt("pat");
            int color = tag.getInt("color");
            Direction direction1 = DirectionStorageHelper.intToDirection(tag.getInt("dir"));
            boolean isGlowing = tag.getBoolean("glow");
            return new PatternData(patternId, color, direction1, isGlowing);
        }
    }
}
