package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class GridBlockPos extends BlockPos {
    private static final int X_ID_SIZE = 13;
    private static final int Z_ID_SIZE = 15;
    private static final int X_SHIFT = X_ID_SIZE;
    private static final int Z_SHIFT = Z_ID_SIZE;
    private static final int X_SIZE = 26 - 1 - X_SHIFT;
    private static final int Z_SIZE = 26 - Z_SHIFT;
    private static final int X_ID_SHIFT = Z_ID_SIZE;
    private static final int X_ID_MASK = ((1 << X_ID_SIZE) - 1) << X_ID_SHIFT;
    private static final int Z_ID_MASK = ((1 << Z_ID_SIZE) - 1);
    private static final int X_MASK_ID = ((1 << X_ID_SIZE) - 1) << X_SIZE;
    private static final int Z_MASK_ID = ((1 << Z_ID_SIZE) - 1) << Z_SIZE;
    private static final int X_MASK = ((1 << X_SIZE) - 1);
    private static final int Z_MASK = ((1 << Z_SIZE) - 1);
    private static final int GRID_FLAG_X = 0x1000000;


    private final Grid grid;

    public GridBlockPos(Grid grid, int x, int y, int z) {
        super(x, y, z);
        this.grid = grid;
    }

    public GridBlockPos(Grid grid, Vec3i pos) {
        super(pos);
        this.grid = grid;
    }

    public static GridBlockPos asGridBlockPos(GridLevel level, BlockPos pos) {
        if (pos instanceof GridBlockPos) return (GridBlockPos) pos;
        if (!isGridFlagged(pos.getX())) return null;
        Grid grid = getGrid(level, pos.getX(), pos.getZ());
        if (grid == null) return null;

        return new GridBlockPos(grid, getLocalX(pos), getLocalY(pos), getLocalZ(pos));
    }

    public static GridBlockPos asGridBlockPos(Grid grid, BlockPos pos) {
        if (pos instanceof GridBlockPos) return (GridBlockPos) pos;
        if (!isGridFlagged(pos.getX())) return null;

        return new GridBlockPos(grid, getLocalX(pos), getLocalY(pos), getLocalZ(pos));
    }

    public static boolean isPartOfGrid(Vec3i pos) {
        if (pos instanceof GridBlockPos) return true;
        return isGridFlagged(pos.getX());
    }

    public static BlockPos toLocal(BlockPos pos) {
        GridBlockPos gbp = asGridBlockPos((Grid) null, pos);
        if (gbp == null) return pos;

        return gbp.asLocal();
    }

    public static double getGlobalX(int id) {
        return  ((1024 & X_MASK) | (X_MASK_ID & ((id & X_ID_MASK) << X_SHIFT))) ^ GRID_FLAG_X;
    }

    public static double getGlobalZ(int id) {
        return (1024 & Z_MASK) | (Z_MASK_ID & ((id & Z_ID_MASK) << Z_SHIFT));
    }

    public Grid getGrid() {
        return grid;
    }

    @Override
    public int getX() {
        return (((super.getX() + 1024) & X_MASK) | (X_MASK_ID & ((grid.getId() & X_ID_MASK) << X_SHIFT))) ^ GRID_FLAG_X;
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public int getZ() {
        return ((super.getZ() + 1024) & Z_MASK) | (Z_MASK_ID & ((grid.getId() & Z_ID_MASK) << Z_SHIFT));
    }

    public Vec3 asWorldPosition() {
        return asWorldPosition(0, 0, 0);
    }

    public Vec3 asWorldPosition(double offsetX, double offsetY, double offsetZ) {
        double x = getLocalX() + offsetX;
        double y = getLocalY() + offsetY;
        double z = getLocalZ() + offsetZ;
        Quaternionfc q = grid.getRotation();

        float xx = q.x() * q.x(), yy = q.y() * q.y(), zz = q.z() *q.z(), ww = q.w() * q.w();
        float xy = q.x() * q.y(), xz = q.x() * q.z(), yz = q.y() * q.z(), xw = q.x() * q.w();
        float zw = q.z() * q.w(), yw = q.y() * q.w(), k = 1 / (xx + yy + zz + ww);

        x = org.joml.Math.fma((xx - yy - zz + ww) * k, x, org.joml.Math.fma(2 * (xy - zw) * k, y, (2 * (xz + yw) * k) * z));
        y = org.joml.Math.fma(2 * (xy + zw) * k, x, org.joml.Math.fma((yy - xx - zz + ww) * k, y, (2 * (yz - xw) * k) * z));
        z = org.joml.Math.fma(2 * (xz - yw) * k, x, Math.fma(2 * (yz + xw) * k, y, ((zz - xx - yy + ww) * k) * z));

        Vector3dc pos = grid.getPosition();
        return new Vec3(x + pos.x(), y + pos.y(), z  + pos.z());
    }

    public int getLocalX() {
        return super.getX();
    }

    public int getLocalY() {
        return super.getY();
    }

    public int getLocalZ() {
        return super.getZ();
    }

    @Nullable
    public static Grid getGrid(GridLevel level, BlockPos pos) {
        if (pos instanceof GridBlockPos) return ((GridBlockPos) pos).getGrid();

        return getGrid(level, pos.getX(), pos.getZ());
    }

    @Nullable
    public static Grid getGrid(GridLevel level, int x, int y, int z) {
        return getGrid(level, x, z);
    }

    @Nullable
    public static Grid getGrid(GridLevel level, int x, int z) {
        if (!isGridFlagged(x)) return null;
        int deFlaged = x ^ GRID_FLAG_X;


        var gridId = ((deFlaged & X_MASK_ID) >> X_SHIFT) << X_ID_SHIFT;
        gridId |= ((z & Z_MASK_ID) >> Z_SHIFT);

        return level.getGrid(gridId);
    }


    public static Grid getGrid(Level level, BlockPos pos) {
        GridLevel lvl = Grids.getLevel(level);
        if (lvl == null) return null;

        return getGrid(lvl, pos);
    }

    public static Grid getGrid(Level level, int x, int y, int z) {
        GridLevel lvl = Grids.getLevel(level);
        if (lvl == null) return null;

        return getGrid(lvl, x, z);
    }

    public static int getLocalX(BlockPos pos) {
        if (pos instanceof GridBlockPos) return ((GridBlockPos) pos).getLocalX();
        if (!isGridFlagged(pos.getX())) return pos.getX();
        int deFlaged = pos.getX() ^ GRID_FLAG_X;

        return (deFlaged & X_MASK) - 1024;
    }

    public static int getLocalY(BlockPos pos) {
        return pos.getY();
    }

    public static int getLocalZ(BlockPos pos) {
        if (pos instanceof GridBlockPos) return ((GridBlockPos) pos).getLocalZ();
        if (!isGridFlagged(pos.getX())) return pos.getZ();

        return (pos.getZ() & Z_MASK) - 1024;
    }

    public BlockPos asLocal() {
        return new BlockPos(getLocalX(), getLocalY(), getLocalZ());
    }

    private static boolean isGridFlagged(int x) {
        return (x > 0 && (x & GRID_FLAG_X) != 0) || (x < 0 && (x & GRID_FLAG_X) == 0);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", this.getLocalX())
                .add("y", this.getLocalY())
                .add("z", this.getLocalZ())
                .add("grid", this.getGrid().getId())
                .toString();
    }
}