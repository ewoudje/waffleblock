package com.ewoudje.waffleblocks.mixins.impl.base.features.distances;


import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.GridChecks;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"))
    public double distanceSqr(AABB instance, Vec3 vec) {
        double og = instance.distanceToSqr(vec);
        GridLevel level = GridLevelManager.getLevel(this.level());
        if (level != null) {
            return Math.min(og, GridChecks.distanceSqr(level, instance, vec));
        }

        return og;
    }
}
