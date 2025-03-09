package net.abraxator.moresnifferflowers.client.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.abraxator.moresnifferflowers.client.particle.BoblingMarkerVariation;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;

import java.util.Locale;

public record BoblingMarkerOptions(BoblingMarkerVariation variation, String message) implements ParticleOptions {
    public static final Deserializer<BoblingMarkerOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public BoblingMarkerOptions fromCommand(ParticleType<BoblingMarkerOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int variationId = reader.readInt();
            reader.expect('"');
            String message = reader.readString();
            return new BoblingMarkerOptions(BoblingMarkerVariation.byId(variationId), message);
        }

        @Override
        public BoblingMarkerOptions fromNetwork(ParticleType<BoblingMarkerOptions> particleType, FriendlyByteBuf buffer) {
            return new BoblingMarkerOptions(BoblingMarkerVariation.byId(buffer.readByte()), buffer.readUtf());
        }
    };

    public static final Codec<BoblingMarkerOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("variation").forGetter(boblingMarkerOptions -> boblingMarkerOptions.variation().ordinal()),
                            Codec.STRING.fieldOf("message").forGetter(BoblingMarkerOptions::message))
                    .apply(instance, (integer, s) -> new BoblingMarkerOptions(BoblingMarkerVariation.byId(integer), s)));

    @Override
    public ParticleType<?> getType() {
        return ModParticles.BOBLING_MARKER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeByte(this.variation.ordinal());
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %s", variation.name, message.isEmpty() ? "\" \"" : message);
    }
}
