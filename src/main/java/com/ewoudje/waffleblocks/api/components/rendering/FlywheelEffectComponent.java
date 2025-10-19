package com.ewoudje.waffleblocks.api.components.rendering;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import dev.engine_room.flywheel.api.visual.Effect;
import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;

public interface FlywheelEffectComponent extends Effect {
    GridComponentType<ClientGrid, FlywheelEffectComponent> TYPE = new GridComponentType<>(
            ClientGrid.class,
            FlywheelEffectComponent.class,
            FlywheelEffectComponent::onAdd,
            FlywheelEffectComponent::onRemove
    );

    static void onAdd(ClientGrid grid, FlywheelEffectComponent component) {
        VisualizationHelper.queueAdd(component);
    }

    static void onRemove(ClientGrid grid, FlywheelEffectComponent component) {
        VisualizationHelper.queueRemove(component);
    }
}
