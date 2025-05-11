package net.abraxator.moresnifferflowers.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PatternDyeStorage {
    private final Map<BlockPos, PatternData> patterns = new HashMap<>();

    public void addTestPatterns(){
        setPattern(new BlockPos(0, -55, 0), new PatternData(2, DyeColor.RED));
        setPattern(new BlockPos(1, -55, 0), new PatternData(1, DyeColor.GREEN));
        setPattern(new BlockPos(2, -55, 0), new PatternData(2, DyeColor.BROWN));

        int i = 80;
        BlockPos.betweenClosedStream(new BlockPos(-i, -61, -i -50), new BlockPos(i, -60, i -50)).forEach(pos -> {

       //    if (pos.getX() + pos.getZ() % 10 == 0)
               setPattern(pos, new PatternData(pos.getX() % 2 == 0 ? 1 : 2, DyeColor.byId(Math.abs(pos.getX() + pos.getZ()) % 16)));
        });

    }

    public void setPattern(BlockPos pos, PatternData pattern) {
        patterns.put(pos.immutable(), pattern);
    }

    public PatternData getPattern(BlockPos pos) {
        return patterns.get(pos);
    }

    public boolean hasPattern(BlockPos pos) {
        return patterns.containsKey(pos);
    }

    public void removePattern(BlockPos pos) {
        patterns.remove(pos);
    }

    public Stream<BlockPos> getPatternPositions() {
      return patterns.keySet().stream();
    }

    public Stream<BlockPos> getPatternPositionsNear(BlockPos pos, int renderDistance) {
        return patterns.keySet().stream().filter(p -> p.closerThan(pos, renderDistance)) ;
    }

    public boolean isEmpty() {
        return patterns.isEmpty();
    }

    public void save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (var entry : patterns.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
            entryTag.put("data", entry.getValue().save());
            list.add(entryTag);
        }
        tag.put("patterns", list);
    }

    public void load(CompoundTag tag) {
        patterns.clear();
        ListTag list = tag.getList("patterns", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag entry = (CompoundTag) t;
            BlockPos pos = NbtUtils.readBlockPos(entry.getCompound("pos"));
            PatternData data = PatternData.load(entry.getCompound("data"));
            patterns.put(pos, data);
        }
    }

    public record PatternData(int patternId, DyeColor color) {
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("pattern", patternId);
            tag.putInt("color", color.getId());
            return tag;
        }

        public static PatternData load(CompoundTag tag) {
            int patternId = tag.getInt("pattern");
            DyeColor color = DyeColor.byId(tag.getInt("color"));
            return new PatternData(patternId, color);
        }
    }
}
