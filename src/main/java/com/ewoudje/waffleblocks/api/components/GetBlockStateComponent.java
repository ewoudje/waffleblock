package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.block.state.BlockState;

public interface GetBlockStateComponent {
    GridComponentType<Grid, GetBlockStateComponent> TYPE = new GridComponentType<>(GetBlockStateComponent.class);

    BlockState getBlockState(GridBlockPos pos, Operation<BlockState> original);

}
