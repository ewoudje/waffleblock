package com.ewoudje.waffleblocks.mixins.impl.cag.base;

import com.ewoudje.wafflecreate.ContraptionLogic;
import com.ewoudje.wafflecreate.IGridContraption;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.render.ContraptionVisual;
import dev.engine_room.flywheel.api.visualization.VisualEmbedding;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContraptionVisual.class)
public class MixinContraptionVisual {
    @Shadow @Final protected VisualEmbedding embedding;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void wb$init(VisualizationContext ctx, AbstractContraptionEntity entity, float partialTick, CallbackInfo ci) {
        (((IGridContraption) entity.getContraption()).waffle$getLogic()).setEmbedding(this.embedding);
    }
}
