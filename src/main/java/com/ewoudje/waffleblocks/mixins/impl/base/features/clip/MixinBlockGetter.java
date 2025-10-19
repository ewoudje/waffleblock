package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.interaction.ClipableComponent;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.sound.sampled.Clip;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(BlockGetter.class)
public interface MixinBlockGetter {

    @WrapMethod(method = "clip")
    default BlockHitResult wb$clip(ClipContext context, Operation<BlockHitResult> original) {
        var og = original.call(context);
        if (!(this instanceof Level)) return og;

        GridLevel level = Grids.getLevel((Level) this);
        if (level == null) return og;

        Vec3 from = context.getFrom();

        Stream<BlockHitResult> stream =
                Grids.findIn(level, new AABB(context.getFrom(), context.getTo()))
                        .sequence(ClipableComponent.TYPE.getter())
                        .mapNotNull(g -> g.clip(context, original))
                        .stream();
        
        return Stream.concat(stream, Stream.of(og))
                .filter(h -> h.getType() == BlockHitResult.Type.BLOCK)
                .min(Comparator.comparingDouble(a -> a.getLocation().distanceToSqr(from)))
                .orElseGet(() -> BlockHitResult.miss(context.getTo(), Direction.getNearest(from.subtract(context.getTo())), BlockPos.containing(context.getTo())));
    }

}
