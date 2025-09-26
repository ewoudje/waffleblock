package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Quaternionfc;
import org.joml.Vector3dc;

public interface Grid {

    @Nullable
    <C> C getComponent(GridComponentType<? extends Grid, C> componentType);

    Vector3dc getPosition();
    AABB getAABB();
    Quaternionfc getRotation();
    int getId();

}
