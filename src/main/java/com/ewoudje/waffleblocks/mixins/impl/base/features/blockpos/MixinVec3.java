package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Vec3.class)
public class MixinVec3 {


    /**
     * @author ewoudje
     * @reason having grid blockpositions return world vec3
     */

    @Overwrite
    public static Vec3 atLowerCornerWithOffset(Vec3i toCopy, double offsetX, double offsetY, double offsetZ) {
        if (toCopy instanceof GridBlockPos g) {
            return g.asWorldPosition(offsetX, offsetY, offsetZ);
        } else {
            return new Vec3((double)toCopy.getX() + offsetX, (double)toCopy.getY() + offsetY, (double)toCopy.getZ() + offsetZ);
        }
    }

    /**
     * @author ewoudje
     * @reason having grid blockpositions return world vec3
     */
    @Overwrite
    public static Vec3 atLowerCornerOf(Vec3i toCopy) {
        return Vec3.atLowerCornerWithOffset(toCopy, 0, 0, 0);
    }
}
