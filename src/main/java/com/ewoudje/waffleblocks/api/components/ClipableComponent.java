package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClipableComponent {
    GridComponentType<Grid, ClipableComponent> TYPE = new GridComponentType<>(ClipableComponent.class);

    /**
     * @param context the clip context
     * @param original the original operation, useful if you will eventually call clip on a level (To prevent it from calling yourself)
     * @return null if you don't want to give any information, otherwise return the hit or miss
     *  Do note that BlockHitResult location should be in world space
     */
    @Nullable
    BlockHitResult clip(@NotNull ClipContext context, @NotNull Operation<BlockHitResult> original);

    @Nullable
    BlockHitResult isBlockInLine(@NotNull ClipBlockStateContext context, @NotNull Operation<BlockHitResult> original);
}
