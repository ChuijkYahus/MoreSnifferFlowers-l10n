package net.abraxator.moresnifferflowers.networking;

import net.abraxator.moresnifferflowers.capability.CapabilityList;
import net.abraxator.moresnifferflowers.client.ClientRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateBlockPatternsPacket(CompoundTag tag, ChunkPos pos) {

    public static void encode(UpdateBlockPatternsPacket msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
        buf.writeChunkPos(msg.pos);
    }

    public static UpdateBlockPatternsPacket decode(FriendlyByteBuf buf) {
        return new UpdateBlockPatternsPacket(buf.readNbt(), buf.readChunkPos());
    }

    public static void handle(UpdateBlockPatternsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handlePacket(msg));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(UpdateBlockPatternsPacket msg) {
        Level level = Minecraft.getInstance().level;
        ChunkPos chunkPos = msg.pos;
        LevelChunk chunk = level.getChunkSource().getChunk(chunkPos.x, chunkPos.z, false);
        if(chunk != null) {
            chunk.getCapability(CapabilityList.BLOCK_PATTERNS).ifPresent(blockPatternCapability -> blockPatternCapability.load(msg.tag));
            ClientRegistration.getBlockPatternRenderer().markDirty();
        }
    }
}
