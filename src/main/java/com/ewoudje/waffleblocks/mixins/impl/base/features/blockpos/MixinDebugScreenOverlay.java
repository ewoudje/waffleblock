package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugScreenOverlay.class)
public class MixinDebugScreenOverlay {

    @Redirect(method = "getSystemInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getX()I"))
    private int wb$localX(BlockPos p) {
        return GridBlockPos.getLocalX(p);
    }

    @Redirect(method = "getSystemInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;getZ()I"))
    private int wb$localZ(BlockPos p) {
        return GridBlockPos.getLocalZ(p);
    }

}
