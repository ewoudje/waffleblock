package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @WrapMethod(method = "renderHitOutline")
    private void wb$renderHitOutline(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, Operation<Void> original) {
        ClientGrid grid;
        GridLevel level;

        if ((level = Grids.getClientLevel(Minecraft.getInstance().level)) != null &&(grid = (ClientGrid) GridBlockPos.getGrid(level, pos)) != null) {
            float partialTick = (RenderSystem.getShaderGameTime() * 24000.0F) % 1f;

            poseStack.pushPose();
            Vector3dc gpos = grid.getPosition(partialTick);
            poseStack.translate(gpos.x() - camX, gpos.y() - camY, gpos.z() - camZ);
            poseStack.mulPose(new Quaternionf(grid.getRotation(partialTick)));
            poseStack.translate(-0.5f, -0.5f, -0.5f);


            original.call(poseStack, consumer, entity, 0.0, 0.0, 0.0, GridBlockPos.toLocal(pos), state);

            poseStack.popPose();
            return;
        }

        original.call(poseStack, consumer, entity, camX, camY, camZ, pos, state);
    }
}
