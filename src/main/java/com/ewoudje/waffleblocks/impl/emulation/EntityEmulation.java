package com.ewoudje.waffleblocks.impl.emulation;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Stack;

public class EntityEmulation {
    private static final ThreadLocal<Stack<EntityEmulation>> emulation = ThreadLocal.withInitial(Stack::new);

    private final double x,y,z, xo, yo, zo, xOld, yOld, zOld;
    private final float xRot, yRot, xRotO, yRotO;
    private final Vec3 deltaMovement;
    private final Entity entity;

    private EntityEmulation(Entity entity) {
        this.entity = entity;
        deltaMovement = entity.getDeltaMovement();
        x = entity.getX();
        y = entity.getY();
        z = entity.getZ();
        xo = entity.xo;
        yo = entity.yo;
        zo = entity.zo;
        xOld = entity.xOld;
        yOld = entity.yOld;
        zOld = entity.zOld;
        xRot = entity.getXRot();
        yRot = entity.getYRot();
        xRotO = entity.xRotO;
        yRotO = entity.yRotO;
    }

    private void undo() {
        entity.setDeltaMovement(deltaMovement);
        entity.setXRot(xRot);
        entity.setYRot(yRot);
        entity.setPos(x, y, z);

        entity.xo = xo;
        entity.yo = yo;
        entity.zo = zo;
        entity.xOld = xOld;
        entity.yOld = yOld;
        entity.zOld = zOld;
        entity.xRotO = xRotO;
        entity.yRotO = yRotO;
    }

    public static void emulate(Entity entity, Grid grid) {
        EntityEmulation e = new EntityEmulation(entity);
        transformEntity(entity, grid);

        GridContext.pushGrid(grid);
        emulation.get().push(e);
    }

    public static void revert(Entity entity) {
        EntityEmulation e = emulation.get().pop();
        e.undo();
        GridContext.popGrid(null);

        assert e.entity == entity || entity == null;
    }

    public static void tickCheck() {
        assert emulation.get().empty();

        if (!emulation.get().empty()) {
            WaffleBlocks.LOGGER.error("One or more entity emulations haven't be closed properly!, this is *very* problematic.");
            emulation.get().forEach(EntityEmulation::undo);
            emulation.get().clear();
        }
    }

    private static void transformEntity(Entity entity, Grid grid) {
        double diffX = -grid.getPosition().x() + GridBlockPos.getGlobalX(grid.getId());
        double diffY = -grid.getPosition().y();
        double diffZ = -grid.getPosition().z() + GridBlockPos.getGlobalZ(grid.getId());

        entity.setPos(entity.getX() + diffX, entity.getY() + diffY, entity.getZ() + diffZ);
    }
}
