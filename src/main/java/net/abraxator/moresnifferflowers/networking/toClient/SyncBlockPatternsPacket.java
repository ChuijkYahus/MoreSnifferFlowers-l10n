package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.networking.IMSFPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

public record SyncBlockPatternsPacket(BlockPatternCapability patterns, BlockPos pos) implements IMSFPacket {
    public static final CustomPacketPayload.Type<SyncBlockPatternsPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("block_patterns"));
    public static final StreamCodec<ByteBuf, SyncBlockPatternsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPatternCapability.STREAM_CODEC, SyncBlockPatternsPacket::patterns,
            BlockPos.STREAM_CODEC, SyncBlockPatternsPacket::pos,
            SyncBlockPatternsPacket::new
    );


    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
/*
            Level level = Minecraft.getInstance().level;
            LevelChunk chunk = level.getChunkSource().getChunk(pos.x, pos.z, false);
            if(chunk != null) {
                chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.load(tag));
                ClientRegistration.getBlockPatternRenderer().markDirty();
            }
*/

        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
