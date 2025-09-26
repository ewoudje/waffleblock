package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockEntitiesContainerComponent {
    GridComponentType<Grid, BlockEntitiesContainerComponent> TYPE = new GridComponentType<Grid, BlockEntitiesContainerComponent>(BlockEntitiesContainerComponent.class);

    void addBlockEntity(BlockEntity blockEntity);
    void removeBlockEntity(BlockEntity blockEntity);
}
