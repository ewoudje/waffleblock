package com.ewoudje.waffleblocks.mixins.impl.base.features.entities;

import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntitySectionStorage.class)
public class MixinEntitySectionStorage {

    @Inject(method = "forEachAccessibleNonEmptySection", at = @At("HEAD"), cancellable = true)
    void wb$getGridEntities(AABB boundingBox, AbortableIterationConsumer<EntitySection<?>> consumer, CallbackInfo ci) {
        boolean f1 = GridBlockPos.isPartOfGrid(new Vec3i((int) boundingBox.minX, (int) boundingBox.minY, (int) boundingBox.minZ));
        boolean f2 = GridBlockPos.isPartOfGrid(new Vec3i((int) boundingBox.maxX, (int) boundingBox.maxY, (int) boundingBox.maxZ));
        if (f1 && f2) {
            ci.cancel();
        } else if (f1 || f2) {
           // WaffleBlocks.LOGGER.warn("You are trying to get entities from EntitySectionStorage of a mixed area!!, this is not supported by WaffleBlocks!");
        }
    }

}
