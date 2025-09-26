package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class WrappedCamera extends Camera {
    private final ClientGrid grid;

    public WrappedCamera(ClientGrid grid) {
        this.grid = grid;
    }

    public void update(Camera camera, float partialTick) {
        Vec3 pos = camera.getPosition();
        Vector3dc gridPos = grid.getPosition(partialTick);
        setPosition(pos.x - gridPos.x(), pos.y - gridPos.y(), pos.z - gridPos.z());

        //TODO setRotation
    }
}
