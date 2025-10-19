package com.ewoudje.waffleblocks.impl.payloads;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.WaffleRegistries;
import com.ewoudje.waffleblocks.api.GridSource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record NewGridPacket<C>(int gridId, GridSource.Factory<C> sourceFactory, C context) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NewGridPacket<?>> TYPE = new CustomPacketPayload.Type<>(WaffleBlocks.resource("new_grid"));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, NewGridPacket<?>> STREAM_CODEC = new StreamCodec<>() {
        @Override
        @NotNull
        public NewGridPacket<?> decode(@NotNull RegistryFriendlyByteBuf buf) {
            int gridId = buf.readInt();
            GridSource.Factory<?> f = buf.readById(WaffleRegistries.GRID_SOURCE::byIdOrThrow);
            Object c = f.contextCodec().decode(buf);

            return new NewGridPacket<>(gridId, (GridSource.Factory<Object>) f, c);
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull NewGridPacket p) {
            buf.writeInt(p.gridId);
            buf.writeById(WaffleRegistries.GRID_SOURCE::getId, p.sourceFactory);
            p.sourceFactory.contextCodec().encode(buf, p.context);
        }
    };

    @Override
    public Type<NewGridPacket<?>> type() {
        return TYPE;
    }
}
