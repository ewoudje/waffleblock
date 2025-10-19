package com.ewoudje.waffleblocks.api;

import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;

public interface ClientGridLevel extends GridLevel.I<ClientGrid> {

    @Override
    @NotNull
    ClientLevel getLevel();

    /**
     * Warning: Recommended to only be called server-side, the server registers this back to the client
     * You should only ever call this if you want this to be only on the client
     */
    <C> @NotNull ClientGrid createNewGrid(int gridId, GridSource.Factory<C> factory, C ctx);
}
