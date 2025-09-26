package com.ewoudje.waffleblocks.impl.flywheel;

import dev.engine_room.flywheel.api.material.Material;
import dev.engine_room.flywheel.api.visual.Effect;
import dev.engine_room.flywheel.api.visual.EffectVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;
import dev.engine_room.flywheel.lib.model.part.InstanceTree;
import dev.engine_room.flywheel.lib.model.part.ModelTrees;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BellRenderer;
import net.minecraft.world.level.LevelAccessor;

public record TestEffect(LevelAccessor level) implements Effect {

    @Override
    public EffectVisual<?> visualize(VisualizationContext ctx, float v) {
        return new TestEffectVisual(ctx, v);
    }

    private class TestEffectVisual implements EffectVisual<TestEffect>, SimpleDynamicVisual {
        private static final Material MATERIAL = SimpleMaterial.builder()
                .mipmap(false)
                .build();

        private final InstanceTree instances;

        public TestEffectVisual(VisualizationContext ctx, float partialTick) {
            instances = InstanceTree.create(ctx.instancerProvider(), ModelTrees.of(ModelLayers.BELL, BellRenderer.BELL_RESOURCE_LOCATION, MATERIAL));
            instances.pos(0.0f, 0.0f, 0.0f);
        }

        @Override
        public void beginFrame(Context context) {

        }

        @Override
        public void update(float v) {

        }

        @Override
        public void delete() {
            instances.delete();
        }
    }
}
