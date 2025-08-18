package net.abraxator.moresnifferflowers.networking.toClient;

import io.netty.buffer.ByteBuf;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.capability.BlockPatternCapability;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.abraxator.moresnifferflowers.init.ModDataAttachments;
import net.abraxator.moresnifferflowers.networking.MSFClientPacket;
import net.abraxator.moresnifferflowers.networking.MSFServerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SyncBlockPatternsPacket(BlockPatternCapability capability, BlockPos pos) implements MSFClientPacket {
    public static final CustomPacketPayload.Type<SyncBlockPatternsPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("sync_blockpatterns"));
    public static final StreamCodec<ByteBuf, SyncBlockPatternsPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPatternCapability.STREAM_CODEC, SyncBlockPatternsPacket::capability,
            BlockPos.STREAM_CODEC, SyncBlockPatternsPacket::pos,
            SyncBlockPatternsPacket::new
    );

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClientPacket(Player player, Level level) {
        LevelChunk chunk = level.getChunkAt(pos);
        chunk.getData(ModDataAttachments.BLOCK_PATTERNS).load(capability);
        ClientRegistration.getBlockPatternRenderer().markDirty();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
