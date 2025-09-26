package com.ewoudje.waffleblocks.impl.flywheel;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.FlywheelEffectComponent;
import com.ewoudje.waffleblocks.api.components.FlywheelEmbeddingComponent;
import com.ewoudje.waffleblocks.api.components.FlywheelManagedComponent;
import com.ewoudje.waffleblocks.util.WrappedCamera;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.*;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visual.DynamicVisualContextImpl;
import dev.engine_room.flywheel.impl.visualization.VisualManagerImpl;
import dev.engine_room.flywheel.lib.task.MapContextPlan;
import dev.engine_room.flywheel.lib.task.NestedPlan;
import net.minecraft.client.Camera;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3dc;

import java.util.stream.Stream;

/**
 * Top level visual
 * Manages the embedding and managers, everything under the ship is part of this visual
 */
public class FlywheelManagedComponentVisual<T extends FlywheelEffectComponent & FlywheelManagedComponent & FlywheelEmbeddingComponent> extends FlywheelEmbeddingComponentVisual implements EffectVisual<T>, TickableVisual, Visual {
    private final T effect;
    private final WrappedCamera camera;

    public FlywheelManagedComponentVisual(T effect, ClientGrid grid, VisualizationContext context) {
        super(effect, grid, context);
        this.effect = effect;
        this.camera = new WrappedCamera(grid);
    }

    @Override
    public Plan<DynamicVisual.Context> planFrame() {
        return updateEmbedding().then(planManagers());
    }

    @Override
    public void update(float partialTick) {

    }

    @Override
    public void delete() {
        super.delete();

        effect.getAllManagers().forEach(m -> ((VisualManagerImpl) m).invalidate());
    }

    private DynamicVisual.Context newContext(DynamicVisual.Context ctx) {
        var camPos = ctx.camera().getPosition();
        camera.update(ctx.camera(), ctx.partialTick());
        Vector3dc position = grid.getPosition(ctx.partialTick());
        Matrix4f matrix = new Matrix4f()
                .translate(
                        (float) (position.x() - camPos.x),
                        (float) (position.y() - camPos.y),
                        (float) (position.z() - camPos.z)
                );

        return new DynamicVisualContextImpl(camera, new FrustumIntersection().set(matrix), ctx.partialTick(), d -> true);
    }

    private TickableVisual.Context newContext(TickableVisual.Context ctx) {
        return ctx;
    }

    public Plan<DynamicVisual.Context> planManagers() {
        MapContextPlan.Builder<DynamicVisual.Context, DynamicVisual.Context> builder = MapContextPlan.map(this::newContext);
        Stream<Plan<DynamicVisual.Context>> plans = effect.getAllManagers().map(m -> ((VisualManagerImpl) m).framePlan(getEmbedding()));

        return builder.to(
                NestedPlan.of(
                        plans.toArray(Plan[]::new)
                )
        ).plan();
    }

    @Override
    public Plan<TickableVisual.Context> planTick() {
        MapContextPlan.Builder<TickableVisual.Context, TickableVisual.Context> builder = MapContextPlan.map(this::newContext);
        Stream<Plan<TickableVisual.Context>> plans = effect.getAllManagers().map(m -> ((VisualManagerImpl) m).tickPlan(getEmbedding()));

        return builder.to(
                NestedPlan.of(
                        plans.toArray(Plan[]::new)
                )
        ).plan();
    }

}
