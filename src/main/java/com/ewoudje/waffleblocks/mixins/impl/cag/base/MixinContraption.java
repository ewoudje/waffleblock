package com.ewoudje.waffleblocks.mixins.impl.cag.base;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.ServerGridLevel;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import com.ewoudje.waffleblocks.mixins.impl.cag.accessors.ClockworkContraptionAccessor;
import com.ewoudje.wafflecreate.ContraptionLogic;
import com.ewoudje.wafflecreate.ContraptionGridSource;
import com.ewoudje.wafflecreate.IGridContraption;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.bearing.ClockworkContraption;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Contraption.class)
public abstract class MixinContraption implements Grid, IGridContraption, ClientGrid {
    @Shadow public AABB bounds;
    @Shadow public AbstractContraptionEntity entity;

    @Shadow protected abstract void addBlock(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair);

    @Shadow protected Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks;
    @Shadow public boolean deferInvalidate;

    @Shadow public abstract void invalidateColliders();

    @Shadow
    public BlockPos anchor;

    @Shadow
    public abstract void expandBoundsAroundAxis(Direction.Axis axis);

    @Unique
    private int id = -1;
    @Unique
    private final ContraptionLogic logic = new ContraptionLogic((Contraption) (Object) this);

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
        this.addBlock(level, pos, pair);
        waffle$update();
    }

    @Override
    public void waffle$removeBlock(Level level, BlockPos pos) {
        StructureTemplate.StructureBlockInfo old = this.blocks.remove(pos.subtract(anchor));
        waffle$update();
    }

    @Unique
    private void waffle$update() {
        this.deferInvalidate = true;
        this.invalidateColliders();

        // Make hitboxes make sense
        switch ((Object) this) {
            case BearingContraption b -> expandBoundsAroundAxis(b.getFacing().getAxis());
            case ClockworkContraptionAccessor c -> expandBoundsAroundAxis(c.getFacing().getAxis());
            case MountedContraption m -> expandBoundsAroundAxis(Direction.Axis.Y);
            default -> {}
        }

        // Update the entities hitbox
        this.entity.setPos(this.entity.getPosition(1.0f));
    }

    @Override
    public ContraptionLogic waffle$getLogic() {
        return logic;
    }

    @Override
    public StructureTemplate.StructureBlockInfo waffle$getBlockInfo(BlockPos pos) {
        return this.blocks.get(pos);
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

    @Inject(method = "onEntityInitialize", at = @At("TAIL"))
    private void initEntity(Level lvl, AbstractContraptionEntity entity, CallbackInfo ci) {
        if (lvl.isClientSide()) return;

        ServerGridLevel level = (ServerGridLevel) GridLevelManager.getLevel(lvl);
        level.createGrid(ContraptionGridSource.FACTORY, entity.getId());
    }

    @Override
    public boolean isRemoved() {
        return entity.isRemoved();
    }
}
