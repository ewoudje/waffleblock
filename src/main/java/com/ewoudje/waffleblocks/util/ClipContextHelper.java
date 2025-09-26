package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.mixins.impl.base.accessors.ClipBlockStateContextAccessor;
import com.ewoudje.waffleblocks.mixins.impl.base.accessors.ClipContextAccessor;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

public class ClipContextHelper {


    public static ClipContext clone(ClipContext og, Vec3 from, Vec3 to) {
        ClipContextAccessor accessor = (ClipContextAccessor) og;
        return new ClipContext(from, to, accessor.getBlock(), accessor.getFluid(), accessor.getCollisionContext());
    }

    public static ClipBlockStateContext clone(ClipBlockStateContext og, Vec3 from, Vec3 to) {
        ClipBlockStateContextAccessor accessor = (ClipBlockStateContextAccessor) og;
        return new ClipBlockStateContext(from, to, accessor.getPredicate());
    }
}
