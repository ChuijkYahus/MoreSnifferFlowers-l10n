package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.client.renderer.custom.BlockPatternRenderer;
import net.abraxator.moresnifferflowers.networking.StreamCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public record SyncBlockPatternsPacket(BlockPatternCapability capability, ChunkPos pos) {
    public static final StreamCodec<SyncBlockPatternsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPatternCapability.STREAM_CODEC, SyncBlockPatternsPacket::capability,
            StreamCodec.CHUNK_POS, SyncBlockPatternsPacket::pos,
            SyncBlockPatternsPacket::new
    );

    public static void handle(SyncBlockPatternsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handlePacket(msg));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(SyncBlockPatternsPacket msg) {
        Level level = Minecraft.getInstance().level;
        ChunkPos chunkPos = msg.pos;
        LevelChunk chunk = level.getChunkSource().getChunk(chunkPos.x, chunkPos.z, false);
        if(chunk != null) {
            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.load(msg.capability.patterns));
            BlockPatternRenderer.BUFFER_MANAGER.markDirty();
        }
    }
}
