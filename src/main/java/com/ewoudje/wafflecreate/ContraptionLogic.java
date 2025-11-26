package com.ewoudje.wafflecreate;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.compaters.GridTransformers;
import com.ewoudje.waffleblocks.api.components.*;
import com.ewoudje.waffleblocks.api.components.interaction.ClipableComponent;
import com.ewoudje.waffleblocks.api.components.world.GetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.world.SetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEmbeddingComponent;
import com.ewoudje.waffleblocks.util.ClipContextHelper;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.ewoudje.waffleblocks.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.contraptions.Contraption;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ContraptionLogic implements ClipableComponent, GetBlockStateComponent, SetBlockStateComponent, FlywheelEmbeddingComponent {
    public static final GridLogicType<IGridContraption, ContraptionLogic> TYPE =
            GridLogicType.createLogic(
                    ContraptionLogic.class,
                    IGridContraption.class,
                    IGridContraption::waffle$isInitialized,
                    () -> new ComponentsProvider<>() {
                @Override
                public @NotNull Set<GridComponentType<? extends IGridContraption, ?>> getSupportedComponentTypes() {
                    return (Set<GridComponentType<? extends IGridContraption, ?>>) (Set) TYPE.getSupportedComponents();
                }

                @Override
                public int getPriority() {
                    return 0;
                }

                @Override
                public @Nullable <C> ComponentGetter<C> createGetter(GridComponentType<? extends IGridContraption, C> type) {
                    if (type.isPartOf(TYPE)) {
                        return g -> (C) ((IGridContraption) g).waffle$getLogic();
                    } else return null;
                }
            });
    private final Contraption contraption;

    public ContraptionLogic(Contraption contraption) {
        this.contraption = contraption;
    }

    @Override
    public @Nullable BlockHitResult clip(@NotNull ClipContext context, @NotNull Operation<BlockHitResult> original) {

        var pt = contraption.entity.level().isClientSide ? RenderUtils.getPartialTick() : 1f;
        var from = contraption.entity.toLocalVector(context.getFrom(), pt);
        var to = contraption.entity.toLocalVector(context.getTo(), pt);


        var result = contraption.getContraptionWorld().clip(ClipContextHelper.clone(context, from, to));
        return new BlockHitResult(
                contraption.entity.toGlobalVector(result.getLocation(), pt),
                result.getDirection(),
                new GridBlockPos((Grid) contraption, result.getBlockPos()),
                result.isInside()
        );
    }

    @Override
    public @Nullable BlockHitResult isBlockInLine(@NotNull ClipBlockStateContext context, @NotNull Operation<BlockHitResult> original) {
        return null;
    }

    @Override
    public BlockState getBlockState(GridBlockPos pos, Operation<BlockState> original) {
        return contraption.getContraptionWorld().getBlockState(pos.asLocal());
    }

    @Override
    public boolean setBlockState(GridBlockPos pos, BlockState state, int flags, int recursionLeft, Operation<Boolean> original) {
        Level level = contraption.entity.level();
        BlockPos lpos = pos.asLocal();
        BlockPos inWorldPos = lpos.offset(contraption.anchor); // To interact with create, its the block where it would be if dissembled
        StructureTemplate.StructureBlockInfo old = ((IGridContraption) contraption).waffle$getBlockInfo(lpos);

        if (!state.isAir()) {
            if (old != null) {
                ((IGridContraption) contraption).waffle$removeBlock(level, inWorldPos);
            }

            Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair = Pair.of(new StructureTemplate.StructureBlockInfo(lpos, state, null), null);
            ((IGridContraption) contraption).waffle$addBlock(level, inWorldPos, pair);
        } else {
            ((IGridContraption) contraption).waffle$removeBlock(level, inWorldPos);
        }

        ChunkSource source = level.getChunkSource();
        if (source instanceof ServerChunkCache chunkCache) {
            chunkCache.broadcast(contraption.entity, new ClientboundBlockUpdatePacket(pos, state));
        }

        return true;
    }

    private final Object embeddingLock = new Object();
    private VisualEmbedding embedding = null;

    public void setEmbedding(VisualEmbedding embedding) {
        synchronized (embeddingLock) {
            this.embedding = embedding;
        }
    }

    @Override
    public VisualEmbedding getEmbedding(VisualizationContext ctx) {
        synchronized (embeddingLock) {
            return embedding;
        }
    }
}
