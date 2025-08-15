package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public record SyncBlockPatternsPacket(BlockPatternCapability capability, BlockPos pos) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncBlockPatternsPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_blockpatterns"));
    public static final StreamCodec<ByteBuf, SyncBlockPatternsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPatternCapability.STREAM_CODEC, SyncBlockPatternsPacket::capability,
            BlockPos.STREAM_CODEC, SyncBlockPatternsPacket::pos,
            SyncBlockPatternsPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            Level level = Minecraft.getInstance().level;
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getData(ModDataAttachments.BLOCK_PATTERNS).load(capability);
            ClientRegistration.getBlockPatternRenderer().markDirty();
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
