package com.ewoudje.waffleblocks.api.components.world;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.block.state.BlockState;

public interface SetBlockStateComponent {
    GridComponentType<Grid, SetBlockStateComponent> TYPE = new GridComponentType<>(
            Grid.class,
            SetBlockStateComponent.class
    );

    boolean setBlockState(GridBlockPos pos, BlockState state, int flags, int recursionLeft, Operation<Boolean> original);
}
