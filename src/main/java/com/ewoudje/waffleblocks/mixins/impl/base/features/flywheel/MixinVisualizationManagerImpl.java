package com.ewoudje.waffleblocks.mixins.impl.base.features.flywheel;

import com.ewoudje.waffleblocks.impl.flywheel.BlockEntityDeferringVisualManager;
import dev.engine_room.flywheel.impl.visualization.VisualManagerImpl;
import dev.engine_room.flywheel.impl.visualization.VisualizationManagerImpl;
import dev.engine_room.flywheel.impl.visualization.storage.BlockEntityStorage;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VisualizationManagerImpl.class)
public class MixinVisualizationManagerImpl {

    @Mutable
    @Shadow @Final private VisualManagerImpl<BlockEntity, BlockEntityStorage> blockEntities;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    void wb_flywheel$replaceBlockEntities(LevelAccessor level, CallbackInfo ci) {
        this.blockEntities = new BlockEntityDeferringVisualManager<>(new BlockEntityStorage());
    }

}
