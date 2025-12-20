package net.abraxator.moresnifferflowers.networking;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.*;

public abstract class StreamCodec<A> {
    public static StreamCodec<Boolean> BOOLEAN = of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    public static StreamCodec<Integer> INT = of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
    public static StreamCodec<Vector3f> VEC3F = of(FriendlyByteBuf::writeVector3f, FriendlyByteBuf::readVector3f);
    public static StreamCodec<BlockPos> BLOCK_POS = of(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    public static StreamCodec<ChunkPos> CHUNK_POS = of(FriendlyByteBuf::writeChunkPos, FriendlyByteBuf::readChunkPos);
    public static StreamCodec<ItemStack> ITEM_STACK = of(FriendlyByteBuf::writeItem, FriendlyByteBuf::readItem);
    public static StreamCodec<Float> FLOAT = of(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    public static StreamCodec<Double> DOUBLE = of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
    public static StreamCodec<CompoundTag> NBT = of(FriendlyByteBuf::writeNbt, FriendlyByteBuf::readNbt);
    public static StreamCodec<ResourceLocation> RESOURCE_LOCATION = of(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);
    public static StreamCodec<Direction> DIRECTION = idMapper(ByIdMap.continuous(Direction::get3DDataValue, Direction.values(), ByIdMap.OutOfBoundsStrategy.WRAP), Direction::get3DDataValue);

    public abstract A decode(FriendlyByteBuf buf);
    public abstract void encode(FriendlyByteBuf buf, A value);

    public void encode(A value, FriendlyByteBuf buf) {
        encode(buf, value);
    }

    public<REMAP> StreamCodec<REMAP> remap(Function<A, REMAP> fromData, Function<REMAP, A> toData){
        StreamCodec<A> codec = this;
        return new StreamCodec<>() {
            @Override
            public REMAP decode(FriendlyByteBuf buf) {
                return fromData.apply(codec.decode(buf));
            }

            @Override
            public void encode(FriendlyByteBuf buf, REMAP value) {
                codec.encode(buf, toData.apply(value));
            }
        };
    }


    public<LIST extends Collection<A>> StreamCodec<LIST> collection(IntFunction<LIST> function) {
        return collection(function, this);
    }

    public StreamCodec<List<A>> list() {
        return collection(ArrayList::new, this);
    }

    public static <T> StreamCodec<T> idMapper(final IntFunction<T> idLookup, final ToIntFunction<T> idGetter) {
        return new StreamCodec<T>() {
            @Override
            public T decode(FriendlyByteBuf buf) {
                int i = buf.readInt();
                return idLookup.apply(i);
            }

            @Override
            public void encode(FriendlyByteBuf buf, T value) {
                int i = idGetter.applyAsInt(value);
                buf.writeInt(i);
            }
        };
    }


    public static <LIST extends Collection<V0>, V0> StreamCodec<LIST> collection(IntFunction<LIST> function, StreamCodec<V0> codec){
        return new StreamCodec<>() {
            @Override
            public LIST decode(FriendlyByteBuf buf) {
                int size = buf.readInt();
                LIST list = function.apply(size);

                for (int i = 0; i < size; i++) {
                    list.add(codec.decode(buf));
                }

                return list;
            }

            @Override
            public void encode(FriendlyByteBuf buf, LIST value) {
                buf.writeInt(value.size());

                for (V0 subValue : value) {
                    codec.encode(buf, subValue);
                }
            }
        };
    }



    public static <MAP extends Map<KEY, VALUE>, KEY, VALUE> StreamCodec<MAP> map(IntFunction<MAP> factory, StreamCodec<KEY> keyCodec, StreamCodec<VALUE> valueCodec){
        return new StreamCodec<>() {
            @Override
            public MAP decode(FriendlyByteBuf buf) {
                int size = buf.readInt();
                MAP map = factory.apply(size);

                for (int i = 0; i < size; i++) {
                    KEY key = keyCodec.decode(buf);
                    VALUE value = valueCodec.decode(buf);
                    map.put(key, value);
                }

                return map;
            }

            @Override
            public void encode(FriendlyByteBuf buf, MAP value) {
                buf.writeInt(value.size());
                value.forEach((key, v) ->{
                    keyCodec.encode(buf, key);
                    valueCodec.encode(buf, v);
                });
            }
        };
    }


    public static<A> StreamCodec<A> of(BiConsumer<FriendlyByteBuf, A> encoder, Function<FriendlyByteBuf, A> decoder) {
        return new StreamCodec<>() {
            @Override
            public A decode(FriendlyByteBuf buf) {
                return decoder.apply(buf);
            }

            @Override
            public void encode(FriendlyByteBuf buf, A value) {
                encoder.accept(buf, value);
            }
        };
    }

    public static<OUTPUT, V0> StreamCodec<OUTPUT> composite(StreamCodec<V0> codec, Function<OUTPUT, V0> getter, Function<V0, OUTPUT> factory) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec.decode(buf);
                return factory.apply(v0);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec.encode(buf, getter.apply(value));
            }
        };
    }

    public static<OUTPUT, V0, V1> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            BiFunction<V0, V1, OUTPUT> factory) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                return factory.apply(v0, v1);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
            }
        };
    }

    public static<OUTPUT, V0, V1, V2> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,

            Function3<V0, V1, V2, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                return factory.apply(v0, v1, v2);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
            }
        };
    }

    public static<OUTPUT, V0, V1, V2, V3> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,
            StreamCodec<V3> codec3, Function<OUTPUT, V3> getter3,

            Function4<V0, V1, V2, V3, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                V3 v3 = codec3.decode(buf);

                return factory.apply(v0, v1, v2, v3);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));

            }
        };
    }

    public static<OUTPUT, V0, V1, V2, V3, V4> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,
            StreamCodec<V3> codec3, Function<OUTPUT, V3> getter3,
            StreamCodec<V4> codec4, Function<OUTPUT, V4> getter4,

            Function5<V0, V1, V2, V3, V4, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                V3 v3 = codec3.decode(buf);
                V4 v4 = codec4.decode(buf);

                return factory.apply(v0, v1, v2, v3, v4);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));
                codec4.encode(buf, getter4.apply(value));

            }
        };
    }



}
