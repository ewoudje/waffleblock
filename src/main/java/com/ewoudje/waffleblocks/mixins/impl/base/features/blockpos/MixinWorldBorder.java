package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class MixinWorldBorder {


    @Inject(method = "isWithinBounds(Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    private void wb$isWithinBounds(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (GridBlockPos.isPartOfGrid(pos)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
