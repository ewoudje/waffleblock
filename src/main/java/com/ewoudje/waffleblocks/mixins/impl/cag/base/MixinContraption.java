package com.ewoudje.waffleblocks.mixins.impl.cag.base;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.ServerGridLevel;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import com.ewoudje.wafflecreate.ContraptionComponent;
import com.ewoudje.wafflecreate.ContraptionGridSource;
import com.ewoudje.wafflecreate.IGridContraption;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(Contraption.class)
public abstract class MixinContraption implements Grid, IGridContraption, ClientGrid {
    @Shadow public AABB bounds;
    @Shadow public AbstractContraptionEntity entity;

    @Shadow protected abstract void addBlock(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair);

    @Shadow protected Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks;
    @Shadow public boolean deferInvalidate;

    @Shadow public abstract void invalidateColliders();

    @Unique
    private int id = -1;
    @Unique
    private final ContraptionComponent component = new ContraptionComponent((Contraption) (Object) this);

    @Override
    public Vector3dc getPosition() {
        return getPosition(1.0f);
    }

    @Override
    public AABB getAABB() {
        return bounds;
    }

    @Override
    public Quaternionfc getRotation() {
        AbstractContraptionEntity.ContraptionRotationState state = entity.getRotationState();
        return new Quaternionf().rotationXYZ(AngleHelper.rad(state.xRotation), AngleHelper.rad(state.yRotation), AngleHelper.rad(state.zRotation));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean waffle$isInitialized() {
        return id != -1;
    }

    @Override
    public Grid waffle$init(int id) {
        this.id = id;

        return this;
    }

    @Override
    public void waffle$addBlock(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair) {
        this.addBlock(level, pair.getKey().pos(), pair);
        this.deferInvalidate = true;
        this.invalidateColliders();
    }

    @Override
    public void waffle$removeBlock(Level level, BlockPos pos) {
        StructureTemplate.StructureBlockInfo old = this.blocks.remove(pos);
        this.deferInvalidate = true;
        this.invalidateColliders();
    }

    @Override
    public StructureTemplate.StructureBlockInfo waffle$getBlockInfo(BlockPos pos) {
        return this.blocks.get(pos);
    }

    @Override
    public <C> C getClientComponent(GridComponentType<ClientGrid, C> componentType) {
        if (componentType.isAssignableFrom(ContraptionComponent.TYPE)) return (C) component;

        return null;
    }

    @Override
    public Vector3dc getPosition(float partialTick) {
        Vec3 pos = entity.toGlobalVector(new Vec3(0.5, 0.5, 0.5), partialTick);
        return new Vector3d(pos.x, pos.y, pos.z);
    }

    @Override
    public Quaternionfc getRotation(float partialTick) {
        if (this.entity instanceof OrientedContraptionEntity o) {
            return getRotation();// TODO new Quaternionf().rotateXYZ(o.getViewXRot(partialTick), o.getViewYRot(partialTick), 0.0f).rotateY(o.getInitialYaw());
        } else if (this.entity instanceof ControlledContraptionEntity c) {
            float angle = AngleHelper.rad(c.getAngle(partialTick));
            if (c.getRotationAxis() == Direction.Axis.X) {
                return new Quaternionf().rotateX(angle);
            } else if (c.getRotationAxis() == Direction.Axis.Y) {
                return new Quaternionf().rotateY(angle);
            } else {
                return new Quaternionf().rotateZ(angle);
            }
        } else {
            return getRotation();
        }
    }

    @Override
    public <C> @Nullable C getComponent(GridComponentType<? extends Grid, C> componentType) {
        if (componentType.isAssignableFrom(ContraptionComponent.TYPE)) return (C) component;

        return null;
    }

    @Inject(method = "onEntityInitialize", at = @At("TAIL"))
    private void initEntity(Level lvl, AbstractContraptionEntity entity, CallbackInfo ci) {
        if (lvl.isClientSide()) return;

        ServerGridLevel level = (ServerGridLevel) GridLevelManager.getLevel(lvl);
        level.createGrid(ContraptionGridSource.FACTORY, entity.getId());
    }
}
