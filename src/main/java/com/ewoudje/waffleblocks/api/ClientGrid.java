package com.ewoudje.waffleblocks.api;

import org.joml.*;

public interface ClientGrid extends Grid {

    Vector3dc getPosition(float partialTick);
    Quaternionfc getRotation(float partialTick);
}
