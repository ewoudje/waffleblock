package com.ewoudje.wafflecreate;

import com.ewoudje.waffleblocks.api.*;
import com.ewoudje.waffleblocks.api.components.ComponentsProvider;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;
import java.util.stream.Stream;

public class ContraptionGridSource implements GridSource<IGridContraption, Integer> {
    public static final GridSource.Factory<Integer> FACTORY = new GridSource.Factory<>() {

        @Override
        public <L extends ClientGridLevel> GridSource<? extends ClientGrid, Integer> createClientSource(L level) {
            return new ContraptionGridSource(level);
        }

        @Override
        public <L extends ServerGridLevel> GridSource<? extends ServerGrid, Integer> createServerSource(L level) {
            return new ContraptionGridSource(level);
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, Integer> contextCodec() {
            return ByteBufCodecs.VAR_INT;
        }
    };

    private final GridLevel level;

    public ContraptionGridSource(GridLevel level) {
        this.level = level;
    }

    @Override
    public IGridContraption createGrid(int id, Integer entityId) {
        var entity = (AbstractContraptionEntity) level.getLevel().getEntity(entityId);
        IGridContraption contraption = (IGridContraption) entity.getContraption();
        contraption.waffle$init(id);
        return contraption;
    }

    @Override
    public Factory<Integer> getFactory() {
        return FACTORY;
    }
}
