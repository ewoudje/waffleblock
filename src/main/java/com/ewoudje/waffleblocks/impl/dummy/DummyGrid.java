package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.Grid;
import net.minecraft.world.phys.AABB;
import org.joml.*;

public abstract class DummyGrid implements Grid {

    private final int id;
    private final Vector3d pos;
    private final Quaternionf rot;
    private final AABB aabb = new AABB(-16, -128, -16, 32, 128, 32);


    public DummyGrid(int id, Vector3dc pos, Quaternionfc rot) {
        this.id = id;
        this.pos = new Vector3d(pos);
        this.rot = new Quaternionf(rot);
    }

    @Override
    public Vector3dc getPosition() {
        return pos;
    }

    @Override
    public AABB getAABB() {
        return aabb.move(pos.x(), pos.y(), pos.z());
    }

    @Override
    public Quaternionfc getRotation() {
        return rot;
    }

    @Override
    public int getId() {
        return id;
    }
}
