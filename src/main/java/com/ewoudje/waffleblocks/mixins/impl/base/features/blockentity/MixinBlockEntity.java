package com.ewoudje.waffleblocks.mixins.impl.base.features.blockentity;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.world.BlockEntitiesContainerComponent;
import com.ewoudje.waffleblocks.util.GridBlockEntityHelper;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity implements GridBlockEntityHelper.BlockEntityGridContainer {
    @Unique
    private static final ComponentGetter<BlockEntitiesContainerComponent> CONTAINER_GETTER = BlockEntitiesContainerComponent.TYPE.getter();

    @Shadow public abstract BlockPos getBlockPos();

    @Mutable
    @Shadow @Final protected BlockPos worldPosition;

    @Shadow @Nullable public abstract Level getLevel();

    @Unique
    private Grid grid = null;

    @Inject(method = "setLevel", at = @At("TAIL"))
    void wb$initLevel(Level level, CallbackInfo ci) {
        GridLevel gLevel = Grids.getLevel(level);
        if (gLevel != null) {
            grid = GridBlockPos.getGrid(gLevel, getBlockPos());

            if (grid != null) {
                var comp = CONTAINER_GETTER.getComponent(grid);
                if (comp != null) {
                    comp.addBlockEntity((BlockEntity) (Object) this);
                }

                if (!(getBlockPos() instanceof GridBlockPos))
                    this.worldPosition = new GridBlockPos(grid, getBlockPos());
            }
        }
    }

    @Inject(method = "setRemoved()V", at = @At("TAIL"))
    private void wb$removeVisual(CallbackInfo ci) {
        if (grid != null) {
            var comp = CONTAINER_GETTER.getComponent(grid);
            if (comp != null) {
                comp.removeBlockEntity((BlockEntity) (Object) this);
            }
        }
    }

    @Override
    public Grid wb$getGrid() {
        return grid;
    }

    @Override
    public void wb$setGrid(Grid grid) {
        this.grid = grid;
    }
}
