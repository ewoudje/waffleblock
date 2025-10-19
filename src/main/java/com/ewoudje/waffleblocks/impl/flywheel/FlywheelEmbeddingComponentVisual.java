package com.ewoudje.waffleblocks.impl.flywheel;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEmbeddingComponent;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.task.RunnablePlan;
import net.minecraft.core.Vec3i;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public abstract class FlywheelEmbeddingComponentVisual implements DynamicVisual {
    private final VisualEmbedding embedding;
    private final Vec3i prevRenderOrigin;
    protected final ClientGrid grid;

    protected FlywheelEmbeddingComponentVisual(FlywheelEmbeddingComponent embeddingComponent, ClientGrid grid, VisualizationContext context) {
        this.grid = grid;
        prevRenderOrigin = context.renderOrigin();
        embedding = embeddingComponent.getEmbedding(context);
    }

    protected Plan<Context> updateEmbedding() {
        return RunnablePlan.of(this::updateEmbedding_);
    }

    @Override
    public void delete() {
        embedding.delete();
    }

    private void updateEmbedding_(Context context) {
        var position = grid.getPosition(context.partialTick());

        var pose = new Matrix4f().identity()
                .translate(
                        (float) (position.x() - prevRenderOrigin.getX()),
                        (float) (position.y() - prevRenderOrigin.getY()),
                        (float) (position.z() - prevRenderOrigin.getZ())
                ).rotate(new Quaternionf());

        embedding.transforms(pose, new Matrix3f().identity());
    }

    public VisualEmbedding getEmbedding() {
        return embedding;
    }
}
