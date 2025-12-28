package com.ewoudje.waffleblocks.mixins.impl.base.accessors;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HitResult.class)
public interface HitResultAccessor {

    @Accessor("location")
    Vec3 getInternalPosition();
}
