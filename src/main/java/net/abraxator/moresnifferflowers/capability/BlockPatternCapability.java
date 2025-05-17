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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockPatternCapability {
    private Map<ResourceKey<Level>, Map<BlockPos, PatternData>> patterns = new HashMap<>();

    public void addTestPatterns(Level level){
        MoreSnifferFlowers.LOGGER.warn("No saved Block Patterns found... Adding test patterns");
        setPattern(new BlockPos(0, -55, 0), new PatternData(1, DyeColor.RED.getTextColor(), Direction.NORTH, false), level);
        setPattern(new BlockPos(1, -55, 0), new PatternData(0, DyeColor.GREEN.getTextColor(), Direction.NORTH, false), level);
        setPattern(new BlockPos(2, -55, 0), new PatternData(1, DyeColor.BROWN.getTextColor(), Direction.NORTH, false), level);
    }

    public void setPattern(BlockPos pos, PatternData pattern, Level level) {
        patterns.computeIfAbsent(level.dimension(), levelResourceKey -> new HashMap<>()).put(pos.immutable(), pattern);
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

    public void sync(){
        CompoundTag compoundtag = this.save(new CompoundTag());
        ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new UpdateBlockPatternsPacket(compoundtag));
    }

    public void setFromDisk(ServerLevel serverLevel) {
        BlockPatternCapability capability = BlockPatternSavedData.get(serverLevel).getStorage();
        this.patterns = capability.getMap();
        sync();
    }

    private Map<ResourceKey<Level>, Map<BlockPos, PatternData>> getMap(){
        return patterns;
    }

    public void clear(){
        this.patterns.clear();
    }

    public void recolor(Level level, BlockPos pos, int color) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, color, data.direction, data.isGlowing), level);
    }

    public void enableGlowing(Level level, BlockPos pos) {
        PatternData data = getPattern(pos, level);
        setPattern(pos, new PatternData(data.patternId, data.color, data.direction, true), level);
    }

    public CompoundTag save(CompoundTag tag) {
        ListTag dimensionList = new ListTag();
        for (var dimEntry : patterns.entrySet()) {
            CompoundTag dimTag = new CompoundTag();
            dimTag.putString("dimension", dimEntry.getKey().location().toString());

            ListTag patternList = new ListTag();
            for (var entry : dimEntry.getValue().entrySet()) {
                CompoundTag entryTag = new CompoundTag();
                entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
                entryTag.put("data", entry.getValue().save());
                patternList.add(entryTag);
            }

            dimTag.put("patterns", patternList);
            dimensionList.add(dimTag);
        }
        tag.put("dimensions", dimensionList);
        return tag;
    }

    public void load(CompoundTag tag) {
        patterns.clear();
        ListTag dimensionList = tag.getList("dimensions", Tag.TAG_COMPOUND);
        for (Tag dimT : dimensionList) {
            CompoundTag dimTag = (CompoundTag) dimT;
            ResourceLocation dimId = new ResourceLocation(dimTag.getString("dimension"));
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimId);

            ListTag patternList = dimTag.getList("patterns", Tag.TAG_COMPOUND);
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

    public record PatternData(int patternId, int color, Direction direction, boolean isGlowing) {
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("pattern", patternId);
            tag.putInt("color", color);
            tag.putInt("direction", DirectionStorageHelper.directionToInt(direction));
            tag.putBoolean("isGlowing", isGlowing);
            return tag;
        }

        public static PatternData load(CompoundTag tag) {
            int patternId = tag.getInt("pattern");
            int color = tag.getInt("color");
            Direction direction1 = DirectionStorageHelper.intToDirection(tag.getInt("direction"));
            boolean isGlowing = tag.getBoolean("isGlowing");
            return new PatternData(patternId, color, direction1, isGlowing);
        }
    }
}
