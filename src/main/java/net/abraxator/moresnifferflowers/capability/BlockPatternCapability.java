package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.components.DirectionStorageHelper;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.toClient.SyncBlockPatternsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPatternCapability {
    public Map<BlockPos, PatternData> patterns;
    public static final Codec<BlockPos> BLOCKPOS_STRING_CODEC = Codec.STRING.xmap(
            s -> {
                String[] parts = s.split(",");
                return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            },
            pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
    );

    public static final Codec<BlockPatternCapability> CODEC =RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(BLOCKPOS_STRING_CODEC, PatternData.CODEC).fieldOf("patterns").forGetter(cap -> cap.patterns))
            .apply(instance, BlockPatternCapability::new));

    public static final StreamCodec<? super ByteBuf, BlockPatternCapability> STREAM_CODEC =
            StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.fromCodec(BlockPos.CODEC),
                    ByteBufCodecs.fromCodec(PatternData.CODEC)
            ),
                    (cap -> cap.patterns),
                    BlockPatternCapability::new
            );

    public BlockPatternCapability(Map<BlockPos, PatternData> patterns) {
        this.patterns = new HashMap<>(patterns);
    }

    public Map<BlockPos, PatternData> getPatterns() {
        return patterns;
    }

    public static BlockPatternCapability getBlockPatterns(BlockPos pos, Level level){
        return getBlockPatterns(level.getChunkAt(pos));
    }

    public static BlockPatternCapability getBlockPatterns(LevelChunk chunk){
        return chunk.getData(ModDataAttachments.BLOCK_PATTERNS.get());
    }


    public static void setPattern(BlockPos pos, PatternData pattern, Level level) {
       // ChunkPos chunkPos = new ChunkPos(pos);
       // LevelChunk chunk = level.getChunkAt(pos);
       // chunk.setUnsaved(true);

        BlockPatternCapability capability = getBlockPatterns(pos, level);
        capability.setPattern(pos, pattern);
        if (!level.isClientSide)
            capability.sync(pos, level);
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
             getBlockPatterns(levelChunk).sync(levelChunk.getPos().getWorldPosition(), level);
        }
    }


    public static PatternData getPattern(BlockPos pos, Level level){
        LevelChunk chunk = level.getChunkAt(pos);
        return chunk.getData(ModDataAttachments.BLOCK_PATTERNS).getPattern(pos);
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

        if (!level.isClientSide){
           capability.sync(pos, level);
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

    public void sync(BlockPos pos, Level level) {
        save(pos, level);
       // PacketDistributor.sendToAllPlayers(new SyncBlockPatternsPacket(this, pos));
    }

    public void save(BlockPos pos, Level level){
        level.getChunkAt(pos).setData(ModDataAttachments.BLOCK_PATTERNS.get(), this);
    }

    public void load(BlockPatternCapability capability){
        patterns.clear();
        patterns.putAll(capability.patterns);
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

    // pattern=pat color=6, direction=dir, glowing=glow
    public record PatternData(int patternId, int color, Direction direction, boolean isGlowing) {
        public static final Codec<PatternData> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        Codec.INT.fieldOf("id").forGetter(PatternData::patternId),
                        Codec.INT.fieldOf("col").forGetter(PatternData::color),
                        Direction.CODEC.fieldOf("dir").forGetter(PatternData::direction),
                        Codec.BOOL.fieldOf("glw").forGetter(PatternData::isGlowing)
                ).apply(instance, PatternData::new));

    }
}
