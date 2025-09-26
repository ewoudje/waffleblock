package com.ewoudje.wafflecreate;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.*;
import com.ewoudje.waffleblocks.util.ClipContextHelper;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionBlockChangedPacket;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContraptionComponent implements ClipableComponent, GetBlockStateComponent, SetBlockStateComponent, FlywheelEmbeddingComponent {
    public static final GridComponentType<IGridContraption, ContraptionComponent> TYPE = new GridComponentType<>(ContraptionComponent.class);
    private final Contraption contraption;

    public ContraptionComponent(Contraption contraption) {
        this.contraption = contraption;
    }

    @Override
    public @Nullable BlockHitResult clip(@NotNull ClipContext context, @NotNull Operation<BlockHitResult> original) {
        var pt = 1f;
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
        BlockPos inWorldPos = lpos.offset(contraption.anchor);
        StructureTemplate.StructureBlockInfo old = ((IGridContraption) contraption).waffle$getBlockInfo(lpos);

        if (!state.isAir()) {
            if (old != null) {
                ((IGridContraption) contraption).waffle$removeBlock(level, lpos);
            }

            Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair = Pair.of(new StructureTemplate.StructureBlockInfo(inWorldPos, state, null), null);
            ((IGridContraption) contraption).waffle$addBlock(level, lpos, pair);
        } else {
            ((IGridContraption) contraption).waffle$removeBlock(level, lpos);
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
