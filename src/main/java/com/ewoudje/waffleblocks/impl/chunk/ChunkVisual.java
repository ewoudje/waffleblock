package com.ewoudje.waffleblocks.impl.chunk;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.BlockModelBuilder;
import dev.engine_room.flywheel.lib.task.ConditionalPlan;
import dev.engine_room.flywheel.lib.task.RunnablePlan;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Iterator;
import java.util.Random;
import java.util.function.BooleanSupplier;

public class ChunkVisual implements DynamicVisual, BlockAndTintGetter {
    private final BlockModelBuilder builder = new BlockModelBuilder(this, SectionIterator::new);
    private final ChunkAccess chunk;
    private final BooleanSupplier isDirty;
    private final Runnable clearDirty;
    private final InstancerProvider instancerProvider;
    private final BlockPos offset;
    private final int sectionCoord; //NOT INDEX
    private final Vector3d render2Pos = new Vector3d();
    private TransformedInstance instance;
    private Model model;


    public ChunkVisual(ChunkAccess chunk, int sectionCoord, Vector3dc render2Pos, BooleanSupplier isDirty, Runnable clearDirty, VisualizationContext context) {
        this.chunk = chunk;
        this.sectionCoord = sectionCoord;
        this.isDirty = isDirty;
        this.clearDirty = clearDirty;
        this.render2Pos.set(render2Pos);

        builder.renderFluids(true);
        instancerProvider = context.instancerProvider();
        offset = new BlockPos(chunk.getPos().getMinBlockX(), SectionPos.sectionToBlockCoord(sectionCoord), chunk.getPos().getMinBlockZ());

        build();
    }

    public void build() {
        if (chunk.isSectionEmpty(sectionCoord)) {
            if (instance != null) {
                instance.delete();
                instance = null;
            }

            clearDirty.run();
            return;
        }

        model = builder.build();

        var instancer = instancerProvider.instancer(InstanceTypes.TRANSFORMED, model);
        if (instance != null) {
            instancer.stealInstance(instance);
        } else {
            instance = instancer.createInstance();
            instance.translate(render2Pos.x(), render2Pos.y(), render2Pos.z());
        }

        instance.setChanged();

        clearDirty.run();
    }

    @Override
    public void update(float v) {

    }

    @Override
    public void delete() {
        if (instance != null) instance.delete();
    }

    @Override
    public float getShade(@NotNull Direction direction, boolean b) {
        return 1f;
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return chunk.getLevel().getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos blockPos, @NotNull ColorResolver colorResolver) {
        return chunk.getLevel().getBlockTint(blockPos.offset(offset), colorResolver); //TODO
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(@NotNull BlockPos blockPos) {
        return null;
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos blockPos) {
        return chunk.getBlockState(blockPos.offset(offset));
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos blockPos) {
        return chunk.getFluidState(blockPos.offset(offset));
    }

    @Override
    public int getHeight() {
        return chunk.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return chunk.getMinBuildHeight();
    }

    @Override
    public Plan<Context> planFrame() {
        return ConditionalPlan.<Context>on(isDirty::getAsBoolean).then(RunnablePlan.of(this::build));
    }

    private class SectionIterator implements Iterator<BlockPos> {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(-1, 0, 0);

        @Override
        public boolean hasNext() {
            return pos.getX() <= 15 && pos.getY() <= 15 && pos.getZ() <= 15;
        }

        @Override
        public BlockPos next() {
            pos.setX(pos.getX() + 1);
            if (pos.getX() > 15) {
                pos.setX(0);
                pos.setZ(pos.getZ() + 1);

                if (pos.getZ() > 15) {
                    pos.setZ(0);
                    pos.setY(pos.getY() + 1);
                }
            }

            return pos;
        }
    }
}
