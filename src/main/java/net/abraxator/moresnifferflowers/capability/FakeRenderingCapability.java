package net.abraxator.moresnifferflowers.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FakeRenderingCapability {
    public static final Codec<Set<BlockPos>> BLOCKPOS_SET_CODEC = BlockPatternCapability.BLOCKPOS_LONG_CODEC.listOf().xmap(HashSet::new, ArrayList::new);
    public static final Codec<FakeRenderingCapability> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BLOCKPOS_SET_CODEC.fieldOf("blockpos_set").forGetter(cap -> cap.blockPosSet)
            ).apply(instance, FakeRenderingCapability::new)
    );

    public Set<BlockPos> blockPosSet;

    public FakeRenderingCapability(Set<BlockPos> blockPosSet){
        this.blockPosSet = blockPosSet;
    }

    public static Set<BlockPos> getCopy(LevelChunk chunk){
       return new HashSet<>(chunk.getData(ModDataAttachments.FAKE_RENDERING).blockPosSet);
    }

    public static void set(LevelChunk chunk, Set<BlockPos> set){
        chunk.setData(ModDataAttachments.FAKE_RENDERING, new FakeRenderingCapability(set));
    }
}
