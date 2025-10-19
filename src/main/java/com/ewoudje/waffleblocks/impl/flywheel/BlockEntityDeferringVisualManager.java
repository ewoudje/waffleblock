package com.ewoudje.waffleblocks.impl.flywheel;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelVisualManagersComponent;
import com.ewoudje.waffleblocks.util.GridBlockEntityHelper;
import dev.engine_room.flywheel.api.visualization.VisualManager;
import dev.engine_room.flywheel.impl.visualization.VisualManagerImpl;
import dev.engine_room.flywheel.impl.visualization.storage.Storage;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

/**
 * Redirects to correct visual managers
 * Is needed because there are static methods that are used to update or create blockentities
 */
public class BlockEntityDeferringVisualManager<S extends Storage<BlockEntity>> extends VisualManagerImpl<BlockEntity, S> {
    private ComponentGetter<FlywheelVisualManagersComponent> managerGetter = FlywheelVisualManagersComponent.TYPE.getter();
    public BlockEntityDeferringVisualManager(S storage) {
        super(storage);
    }

    private boolean isNoGridManager(BlockEntity obj, Consumer<VisualManager<BlockEntity>> found) {
        ClientGrid grid = (ClientGrid) GridBlockEntityHelper.getGridOf(obj);
        if (grid != null) {
            FlywheelVisualManagersComponent managedComponent = managerGetter.getComponent(grid);
            if (managedComponent != null) {
                found.accept(managedComponent.getManager(BlockEntity.class));
                return false;
            }
        }

        return true;
    }

    @Override
    public void queueAdd(BlockEntity obj) {
        if (isNoGridManager(obj, m -> m.queueAdd(obj)))
            super.queueAdd(obj);
    }

    @Override
    public void queueUpdate(BlockEntity obj) {
        if (isNoGridManager(obj, m -> m.queueUpdate(obj)))
            super.queueUpdate(obj);
    }

    @Override
    public void queueRemove(BlockEntity obj) {
        if (isNoGridManager(obj, m -> m.queueRemove(obj)))
            super.queueRemove(obj);
    }
}
