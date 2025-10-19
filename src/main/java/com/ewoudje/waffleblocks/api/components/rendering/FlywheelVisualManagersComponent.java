package com.ewoudje.waffleblocks.api.components.rendering;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import dev.engine_room.flywheel.api.visualization.VisualManager;

import java.util.stream.Stream;

public interface FlywheelVisualManagersComponent {
    GridComponentType<ClientGrid, FlywheelVisualManagersComponent> TYPE = new GridComponentType<>(
            ClientGrid.class,
            FlywheelVisualManagersComponent.class
    );

    <T> VisualManager<T> getManager(Class<T> clazz);

    Stream<VisualManager<?>> getAllManagers();
}
