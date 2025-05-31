package net.abraxator.moresnifferflowers.capability;

public class BlockPatternSavedData {
/*    private BlockPatternCapability storage = new BlockPatternCapability();

    public static final String DATA_NAME = "pattern_storage";

    public static BlockPatternSavedData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage()
                .computeIfAbsent(tag -> {
                    BlockPatternSavedData saved = new BlockPatternSavedData();
                    saved.storage.load(tag);
                    return saved;
                    },
                        BlockPatternSavedData::new, DATA_NAME
                );
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        this.storage = CapabilityList.getBlockPatterns();
        storage.save(compoundTag);
        return compoundTag;
    }

    public BlockPatternCapability getStorage() {
        return storage;
    }

    @Override
    public void setDirty() {
        super.setDirty();
        this.storage = CapabilityList.getBlockPatterns();
        save(new CompoundTag());
    }*/
}
