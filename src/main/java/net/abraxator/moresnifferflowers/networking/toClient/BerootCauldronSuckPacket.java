package net.abraxator.moresnifferflowers.networking.toClient;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.blockentities.BerootCauldronBlockEntity;
import net.abraxator.moresnifferflowers.networking.MSFClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BerootCauldronSuckPacket(ItemStack itemStack, BlockPos pos) implements MSFClientPacket {
    public static final CustomPacketPayload.Type<BerootCauldronSuckPacket> TYPE = new CustomPacketPayload.Type<>(MoreSnifferFlowers.loc("cauldron_suck"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BerootCauldronSuckPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, BerootCauldronSuckPacket::itemStack,
            BlockPos.STREAM_CODEC, BerootCauldronSuckPacket::pos,
            BerootCauldronSuckPacket::new
    );

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClientPacket(Player player, Level level) {
        BerootCauldronBlockEntity entity = ((BerootCauldronBlockEntity) level.getBlockEntity(pos));
        entity.addItem(itemStack, null);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
