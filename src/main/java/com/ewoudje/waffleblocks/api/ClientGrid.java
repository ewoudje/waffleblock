package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import net.minecraft.core.Vec3i;
import org.joml.*;

public interface ClientGrid extends Grid {
    <C> C getClientComponent(GridComponentType<ClientGrid, C> componentType);

    Vector3dc getPosition(float partialTick);
    Quaternionfc getRotation(float partialTick);
}
