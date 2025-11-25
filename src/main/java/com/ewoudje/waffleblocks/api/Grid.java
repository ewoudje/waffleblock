package com.ewoudje.waffleblocks.api;

import net.minecraft.world.phys.AABB;
import org.joml.Quaternionfc;
import org.joml.Vector3dc;

/**
 * Represents one 'thing' that lives on both client and server
 * which can have many capabilities based on the components it supports.
 */
public interface Grid {
    Vector3dc getPosition();

    /**
     * @return AABB in <i>local</i> space
     */
    AABB getAABB();

    Quaternionfc getRotation();

    int getId();

    boolean isRemoved();
}
