package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.ClientGrid;
import dev.engine_room.flywheel.api.visualization.VisualManager;

import java.util.stream.Stream;

public interface FlywheelManagedComponent {
    GridComponentType<ClientGrid, FlywheelManagedComponent> TYPE = new GridComponentType<>(FlywheelManagedComponent.class);

    <T> VisualManager<T> getManager(Class<T> clazz);

    Stream<VisualManager<?>> getAllManagers();
}
