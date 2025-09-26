package com.ewoudje.wafflecreate;

import com.ewoudje.waffleblocks.api.*;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

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
            return ByteBufCodecs.INT;
        }
    };

    private final GridLevel level;

    public ContraptionGridSource(GridLevel level) {
        this.level = level;
    }


    @Override
    public @Nullable Stream<IGridContraption> findGridIn(AABB aabb) {
        return level.getLevel().getEntitiesOfClass(AbstractContraptionEntity.class, aabb)
                .stream()
                .map(e -> (IGridContraption) e.getContraption());
    }

    @Override
    public <C> Stream<Pair<IGridContraption, C>> findWithMyComponent(GridComponentType<IGridContraption, C> component) {
        return Stream.empty();
    }

    @Override
    public IGridContraption createGrid(int id, Integer entityId) {
        IGridContraption contraption = (IGridContraption) ((AbstractContraptionEntity) level.getLevel().getEntity(entityId)).getContraption();
        contraption.waffle$init(id);
        return contraption;
    }

    @Override
    public Factory<Integer> getFactory() {
        return FACTORY;
    }
}
