package com.ewoudje.waffleblocks.api;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public interface ServerGridLevel extends GridLevel.I<ServerGrid> {
    @Override
    @NotNull
    ServerLevel getLevel();

    <C> @NotNull ServerGrid createGrid(GridSource.Factory<C> sourceFactory, C context);
}
