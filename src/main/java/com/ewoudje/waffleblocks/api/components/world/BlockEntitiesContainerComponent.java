package com.ewoudje.waffleblocks.api.components.world;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockEntitiesContainerComponent {
    GridComponentType<Grid, BlockEntitiesContainerComponent> TYPE = new GridComponentType<>(
            Grid.class,
            BlockEntitiesContainerComponent.class
    );

    void addBlockEntity(BlockEntity blockEntity);
    void removeBlockEntity(BlockEntity blockEntity);
}
