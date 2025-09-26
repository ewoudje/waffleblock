package com.ewoudje.waffleblocks.mixins.impl.base.accessors;

import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Predicate;

@Mixin(ClipBlockStateContext.class)
public interface ClipBlockStateContextAccessor {

    @Accessor("block")
    Predicate<BlockState> getPredicate();
}
