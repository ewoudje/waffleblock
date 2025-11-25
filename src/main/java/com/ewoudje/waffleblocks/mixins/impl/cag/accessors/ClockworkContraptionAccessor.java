package com.ewoudje.waffleblocks.mixins.impl.cag.accessors;

import com.simibubi.create.content.contraptions.bearing.ClockworkContraption;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClockworkContraption.class)
public interface ClockworkContraptionAccessor {

    @Accessor("facing")
    Direction getFacing();
}
