package com.ewoudje.waffleblocks.api;

import net.minecraft.world.phys.AABB;
import org.joml.Quaternionfc;
import org.joml.Vector3dc;

public interface Grid {
    Vector3dc getPosition();
    AABB getAABB();
    Quaternionfc getRotation();
    int getId();
}
