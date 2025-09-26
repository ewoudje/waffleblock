package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.SetBlockStateComponent;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public class MixinLevel {

    @WrapMethod(method = "getBlockState")
    BlockState wb$getBlockState(BlockPos pos, Operation<BlockState> original) {
        Grid grid = GridBlockPos.getGrid((Level) (Object) this, pos);
        if (grid == null) return original.call(pos);
        GetBlockStateComponent comp = grid.getComponent(GetBlockStateComponent.TYPE);
        if (comp == null) return original.call(pos);

        return comp.getBlockState(GridBlockPos.asGridBlockPos(grid, pos), original);
    }

    @WrapMethod(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z")
    boolean wb$setBlockState(BlockPos pos, BlockState state, int flags, int recursionLeft, Operation<Boolean> original) {
        Grid grid = GridBlockPos.getGrid((Level) (Object) this, pos);
        if (grid == null) return original.call(pos, state, flags, recursionLeft);
        SetBlockStateComponent comp = grid.getComponent(SetBlockStateComponent.TYPE);
        if (comp == null) return original.call(pos, state, flags, recursionLeft);

        return comp.setBlockState(GridBlockPos.asGridBlockPos(grid, pos), state, flags, recursionLeft, original);
    }
    
    @Inject(method = "getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;Ljava/util/List;I)V", at = @At("HEAD"), cancellable = true)
    void wb$getEntities(EntityTypeTest<Entity, Entity> entityTypeTest, AABB bounds, Predicate<? super Entity> predicate, List<? super Entity> output, int maxResults, CallbackInfo ci) {
        if (bounds.getXsize() > 1000 || bounds.getYsize() > 1000 || bounds.getZsize() > 1000) {
            WaffleBlocks.LOGGER.warn("The AABB is too big!!, you are trying to get entities in a huge area!!, this is not supported by WaffleBlocks!");
            ci.cancel();
        }
    }
}
