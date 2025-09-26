package com.ewoudje.waffleblocks.api.compaters;

import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;

public class GridChecks {

    public static boolean isWithinDistance(GridLevel level, Vector3dc a, Vector3dc b, double distance) {
        var d2 = distance * distance;

        return GridTransformers.getAllTransformsOf(level, a).anyMatch(p ->
                        GridTransformers.getAllTransformsOf(level, b).anyMatch(
                                q -> p.distanceSquared(q) < d2));
    }

    public static boolean isWithinManhattanDistance(GridLevel level, Vector3dc a, Vector3dc b, double distance) {
        return isWithinManhattanDistance(level, a, b, distance, distance, distance);
    }

    public static boolean isWithinManhattanDistance(GridLevel level, Vector3dc a, Vector3dc b, double distanceX, double distanceY, double distanceZ) {
        return GridTransformers.getAllTransformsOf(level, a).anyMatch(p ->
                GridTransformers.getAllTransformsOf(level, b).anyMatch(
                        q -> (Math.abs(q.x() - p.x()) < distanceX)
                                || (Math.abs(q.y() - p.y()) < distanceY)
                                || (Math.abs(q.z() - p.z()) < distanceZ)));
    }

    public static double distanceSqr(GridLevel level, AABB aabb, Vec3 pos2) {
        Vec3 ca = aabb.getMinPosition();
        Vec3 cb = aabb.getMaxPosition();
        GridBlockPos c1 = GridBlockPos.asGridBlockPos(level, BlockPos.containing(ca));
        GridBlockPos c2 = GridBlockPos.asGridBlockPos(level, BlockPos.containing(cb));
        GridBlockPos p2 = GridBlockPos.asGridBlockPos(level, BlockPos.containing(pos2));

        if ((c1 == null || c2 == null) && p2 == null) return Double.MAX_VALUE;
        if (c1 != null && c2 != null) {
            if (!c2.getGrid().equals(c1.getGrid())) throw new RuntimeException("distanceSqr of an aabb over multiple grids");
            ca = c1.asWorldPosition(ca.x % 1.0, ca.y % 1.0,ca.z % 1.0);
            cb = c2.asWorldPosition(cb.x % 1.0, cb.y % 1.0,cb.z % 1.0);

            aabb = new AABB(ca, cb);
        } else if (c2 != c1) {
            throw new RuntimeException("distanceSqr of an aabb over a grid and the world");
        }

        if (p2 != null) {
            pos2 = p2.asWorldPosition(pos2.x % 1.0, pos2.y % 1.0,pos2.z % 1.0);
        }

        return aabb.distanceToSqr(pos2);
    }
}
