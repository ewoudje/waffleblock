package com.ewoudje.waffleblocks.impl.chunk;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.interaction.ClipableComponent;
import com.ewoudje.waffleblocks.api.components.world.GetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.world.SetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEffectComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEmbeddingComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelVisualManagersComponent;
import com.ewoudje.waffleblocks.api.components.GridLogicType;
import com.ewoudje.waffleblocks.impl.flywheel.FlywheelManagedComponentVisual;
import com.ewoudje.waffleblocks.util.ClipContextHelper;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.engine_room.flywheel.api.visual.Effect;
import dev.engine_room.flywheel.api.visual.EffectVisual;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualManager;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.VisualManagerImpl;
import dev.engine_room.flywheel.impl.visualization.storage.BlockEntityStorage;
import dev.engine_room.flywheel.impl.visualization.storage.EffectStorage;
import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

import java.util.List;
import java.util.stream.Stream;

public class ChunkLogic implements FlywheelVisualManagersComponent, FlywheelEmbeddingComponent, FlywheelEffectComponent, ClipableComponent, GetBlockStateComponent, SetBlockStateComponent {
    public static final GridLogicType<Grid, ChunkLogic> TYPE = GridLogicType.createLogic(ChunkLogic.class, Grid.class, g -> null);
    private final ChunkGridBackend backend;
    private final ClientGridLevel level;
    private final VisualManager<BlockEntity> blockEntityManager = new VisualManagerImpl<>(new BlockEntityStorage() {
        @Override
        public boolean willAccept(BlockEntity blockEntity) {
            if (blockEntity.isRemoved()) {
                return false;
            }

            if (!VisualizationHelper.canVisualize(blockEntity)) {
                return false;
            }

            Level level = blockEntity.getLevel();
            if (level == null) {
                return false;
            }

            return !level.isEmptyBlock(blockEntity.getBlockPos());
        }
    });

    private final VisualManager<Effect> effectManager = new VisualManagerImpl<>(new EffectStorage());

    public ChunkLogic(ClientGridLevel level, ChunkGridBackend backend) {
        this.backend = backend;
        this.level = level;

        effectManager.queueAdd(new FlywheelChunksEffect(backend, level.getLevel()));
    }

    @Override
    public <T> VisualManager<T> getManager(Class<T> clazz) {
        if (clazz == BlockEntity.class) {
            return (VisualManager<T>) blockEntityManager;
        } else if (clazz == Effect.class) {
            return (VisualManager<T>) effectManager;
        } else {
            return null;
        }
    }

    @Override
    public Stream<VisualManager<?>> getAllManagers() {
        return List.of(effectManager, blockEntityManager).stream();
    }

    @Override
    public LevelAccessor level() {
        return level.getLevel();
    }

    @Override
    public EffectVisual<?> visualize(VisualizationContext context, float partialTick) {
        return new FlywheelManagedComponentVisual<>(this, (ClientGrid) backend.getGrid(), context);
    }

    @Override
    public @NotNull BlockHitResult clip(@NotNull ClipContext context, @NotNull Operation<BlockHitResult> original) {
        Vector3dc pos = backend.getGrid().getPosition();
        Vec3i origin = backend.getOrigin();
        Vec3 diff = new Vec3(origin.getX() - pos.x(), origin.getY() - pos.y(), origin.getZ() - pos.z());

        Vec3 from = context.getFrom().add(diff);
        Vec3 to = context.getTo().add(diff);

        BlockHitResult result = original.call(ClipContextHelper.clone(context, from, to));
        if (result.getType() == HitResult.Type.MISS) {
            return BlockHitResult.miss(result.getLocation().subtract(diff), result.getDirection(), new GridBlockPos(backend.getGrid(), result.getBlockPos().subtract(origin)));
        } else {
            return new BlockHitResult(result.getLocation().subtract(diff), result.getDirection(), new GridBlockPos(backend.getGrid(), result.getBlockPos().subtract(origin)), result.isInside());
        }
    }

    @Override
    public @NotNull BlockHitResult isBlockInLine(@NotNull ClipBlockStateContext context, @NotNull Operation<BlockHitResult> original) {
        Vector3dc pos = backend.getGrid().getPosition();
        Vec3i origin = backend.getOrigin();
        Vec3 diff = new Vec3(origin.getX() - pos.x(), origin.getY() - pos.y(), origin.getZ() - pos.z());

        Vec3 from = context.getFrom().add(diff);
        Vec3 to = context.getTo().add(diff);

        BlockHitResult result = original.call(ClipContextHelper.clone(context, from, to));
        if (result.getType() == HitResult.Type.MISS) {
            return BlockHitResult.miss(result.getLocation().subtract(diff), result.getDirection(), new GridBlockPos(backend.getGrid(), result.getBlockPos().subtract(origin)));
        } else {
            return new BlockHitResult(result.getLocation().subtract(diff), result.getDirection(), new GridBlockPos(backend.getGrid(), result.getBlockPos().subtract(origin)), result.isInside());
        }
    }

    @Override
    public BlockState getBlockState(GridBlockPos pos, Operation<BlockState> original) {
        Vec3i origin = backend.getOrigin();
        return original.call(new BlockPos(pos.getLocalX() + origin.getX(), pos.getLocalY() + origin.getY(), pos.getLocalZ() + origin.getZ()));
    }

    @Override
    public boolean setBlockState(GridBlockPos pos, BlockState state, int flags, int recursionLeft, Operation<Boolean> original) {
        Vec3i origin = backend.getOrigin();
        return original.call(new BlockPos(pos.getLocalX() + origin.getX(), pos.getLocalY() + origin.getY(), pos.getLocalZ() + origin.getZ()), state, flags, recursionLeft);
    }

    @Override
    public VisualEmbedding getEmbedding(VisualizationContext ctx) {
        return ctx.createEmbedding(new GridBlockPos(backend.getGrid(), 0, 0, 0));
    }
}
