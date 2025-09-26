package com.ewoudje.waffleblocks.mixins.impl.base.accessors;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClipContext.class)
public interface ClipContextAccessor {

    @Accessor("block")
    ClipContext.Block getBlock();

    @Accessor("fluid")
    ClipContext.Fluid getFluid();

    @Accessor("collisionContext")
    CollisionContext getCollisionContext();
}
