package net.abraxator.moresnifferflowers.capability;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.components.DirectionStorageHelper;
import net.abraxator.moresnifferflowers.networking.ModPacketHandler;
import net.abraxator.moresnifferflowers.networking.UpdateBlockPatternsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockPatternCapability {
    private final Map<ResourceKey<Level>, Map<BlockPos, PatternData>> patterns = new HashMap<>();

    public void setPattern(BlockPos pos, PatternData pattern, Level level) {
        patterns.computeIfAbsent(level.dimension(), levelResourceKey -> new HashMap<>()).put(pos.immutable(), pattern);
        if (level instanceof ServerLevel serverLevel) {
            sync();
            BlockPatternSavedData.get(serverLevel).setDirty();
        }
    }

    public void setBulkPatterns(Map<BlockPos, PatternData> patternMap, Level level) {
        patterns.computeIfAbsent(level.dimension(), levelResourceKey -> new HashMap<>());
        patternMap.forEach((pos, patternData) -> {
            patterns.get(level.dimension()).put(pos, patternData);
        });
        if (level instanceof ServerLevel serverLevel) {
            sync();
            BlockPatternSavedData.get(serverLevel).setDirty();
        }
    }

    public PatternData getPattern(BlockPos pos, Level level) {
        Map<BlockPos, PatternData> map = patterns.get(level.dimension());
        return map != null ? map.get(pos) : null;
    }

    public boolean hasPattern(BlockPos pos, Level level) {
        return patterns.containsKey(level.dimension()) && patterns.get(level.dimension()).containsKey(pos);
    }

    public void removePattern(BlockPos pos, Level level) {
        if (patterns.containsKey(level.dimension())) patterns.get(level.dimension()).remove(pos);
        if (level instanceof ServerLevel serverLevel) {
            sync();
            BlockPatternSavedData.get(serverLevel).setDirty();
        }
    }

    public Stream<BlockPos> getPatternPositionsNear(BlockPos pos, int renderDistance, Level level ) {
        if(!patterns.containsKey(level.dimension())) return Stream.empty();
        return patterns.get(level.dimension()).keySet().stream().filter(p -> p.closerThan(pos, renderDistance)) ;
    }

    public boolean isEmpty() {
        return patterns.isEmpty();
    }

    public boolean isNull(Level level) {
        return patterns.get(level.dimension()) == null;
    }

    public void sync(){
        CompoundTag compoundtag = this.save(new CompoundTag());
        ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateBlockPatternsPacket(compoundtag));
    }

/*    public void setFromDisk(ServerLevel serverLevel) {
        BlockPatternCapability capability = BlockPatternSavedData.get(serverLevel).getStorage();
        this.patterns = capability.getMap();
        sync();
    }*/

    private Map<ResourceKey<Level>, Map<BlockPos, PatternData>> getMap(){
        return patterns;
    }

    public void clear(){
        this.patterns.clear();
    }

    public int count(Level level, int flag) {
        if (isNull(level)) MoreSnifferFlowers.LOGGER.error("BlockPatterns are null for flag: "+ flag + " patterns:" + patterns);
        return patterns.get(level.dimension()).size();
    }


    public void recolor(Level level, BlockPos pos, int color) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, color, data.direction, data.isGlowing), level);
    }

    public void enableGlowing(Level level, BlockPos pos) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, data.color, data.direction, true), level);
    }

    // dimension=dim , pos=2, data=3, patterns=all

    public CompoundTag save(CompoundTag tag) {
        ListTag dimensionList = new ListTag();
        for (var dimEntry : patterns.entrySet()) {
            CompoundTag dimTag = new CompoundTag();
            dimTag.putString("dim", dimEntry.getKey().location().toString());

            ListTag patternList = new ListTag();
            for (var entry : dimEntry.getValue().entrySet()) {
                CompoundTag entryTag = new CompoundTag();
                entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
                entryTag.put("data", entry.getValue().save());
                patternList.add(entryTag);
            }

            dimTag.put("all", patternList);
            dimensionList.add(dimTag);
        }
        tag.put("dim", dimensionList);
        MoreSnifferFlowers.LOGGER.info("Tag=" + tag);
        return tag;
    }

    public void load(CompoundTag tag) {
        patterns.clear();
        ListTag dimensionList = tag.getList("dim", Tag.TAG_COMPOUND);
        for (Tag dimT : dimensionList) {
            CompoundTag dimTag = (CompoundTag) dimT;
            ResourceLocation dimId = new ResourceLocation(dimTag.getString("dim"));
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimId);

            ListTag patternList = dimTag.getList("all", Tag.TAG_COMPOUND);
            Map<BlockPos, PatternData> map = new HashMap<>();
            for (Tag pTag : patternList) {
                CompoundTag entry = (CompoundTag) pTag;
                BlockPos pos = NbtUtils.readBlockPos(entry.getCompound("pos"));
                PatternData data = PatternData.load(entry.getCompound("data"));
                map.put(pos, data);
            }
            patterns.put(dimension, map);
        }
    }

    // pattern=pat color=6, direction=dir, glowing=glow

    public record PatternData(int patternId, int color, Direction direction, boolean isGlowing) {
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
