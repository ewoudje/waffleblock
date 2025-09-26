package com.ewoudje.waffleblocks.impl.chunk;

import com.ewoudje.waffleblocks.util.GridBlockPos;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.Effect;
import dev.engine_room.flywheel.api.visual.EffectVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.task.NestedPlan;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.joml.Vector3d;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class FlywheelChunksEffect implements Effect {
    private final LevelAccessor level;
    private final ChunkGridBackend backend;
    private final Set<SectionPos> dirty = Collections.synchronizedSet(new HashSet<>());

    public FlywheelChunksEffect(ChunkGridBackend backend, LevelAccessor level) {
        this.level = level;
        this.backend = backend;

        backend.setDirtyListener(this::markDirty);
    }

    @Override
    public LevelAccessor level() {
        return level;
    }

    @Override
    public EffectVisual<?> visualize(VisualizationContext ctx, float v) {
        return new Visual(ctx, v);
    }

    public void markDirty(SectionPos pos) {
        dirty.add(pos);
    }

    private BooleanSupplier getIsDirty(ChunkAccess chunk, int coord) {
        final SectionPos pos = SectionPos.of(chunk.getPos(), coord);
        return () -> dirty.contains(pos);
    }

    private Runnable getClearDirty(ChunkAccess chunk, int coord) {
        final SectionPos pos = SectionPos.of(chunk.getPos(), coord);
        return () -> dirty.remove(pos);
    }

    private class Visual implements EffectVisual<FlywheelChunksEffect>, DynamicVisual {
        private final List<ChunkVisual> visuals;
        private final VisualizationContext ctx;

        public Visual(VisualizationContext ctx, float v) {
            this.ctx = ctx;
            visuals = backend.getChunks().flatMap(this::getChunkVisuals).toList();
        }

        public Stream<ChunkVisual> getChunkVisuals(ChunkAccess chunk) {
            double x = chunk.getPos().getMinBlockX() - backend.getOrigin().getX();
            double z = chunk.getPos().getMinBlockZ() - backend.getOrigin().getZ();

            return Stream.iterate(chunk.getMinSection(), i -> i < chunk.getMaxSection(), i -> i + 1)
                    .map(i -> new ChunkVisual(chunk, i, new Vector3d(x, i * 16, z), getIsDirty(chunk, i), getClearDirty(chunk, i), ctx));
        }

        @Override
        public void update(float v) {
            for (ChunkVisual visual : visuals) {
                visual.update(v);
            }
        }

        @Override
        public void delete() {
            for (ChunkVisual visual : visuals) {
                visual.delete();
            }
        }

        @Override
        public Plan<Context> planFrame() {
            return NestedPlan.of(visuals.stream().map(ChunkVisual::planFrame).toArray(Plan[]::new));
        }
    }
}
