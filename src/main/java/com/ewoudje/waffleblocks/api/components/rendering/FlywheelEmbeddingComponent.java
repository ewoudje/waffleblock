package com.ewoudje.waffleblocks.api.components.rendering;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public interface FlywheelEmbeddingComponent {
    GridComponentType<ClientGrid, FlywheelEmbeddingComponent> TYPE = new GridComponentType<>(
            ClientGrid.class,
            FlywheelEmbeddingComponent.class
    );

    /**
     * This should return a newly created embedding or an already existing embedding.
     * Other classes *will* cache this, so suddenly returning another value will not give you the expected results.
     * @param ctx context to create an embedding if needed
     * @return The embedding of the grid
     */
    VisualEmbedding getEmbedding(VisualizationContext ctx);
}
