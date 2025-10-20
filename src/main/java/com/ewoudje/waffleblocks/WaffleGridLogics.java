package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.ServerGrid;
import com.ewoudje.waffleblocks.api.components.ComponentsProvider;
import com.ewoudje.waffleblocks.api.components.GridLogicType;
import com.ewoudje.waffleblocks.impl.simple.LogicBasedComponentProvider;
import com.ewoudje.wafflecreate.ContraptionLogic;
import com.ewoudje.wafflecreate.IGridContraption;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WaffleGridLogics {
    public static void configureLogic(GridLogicType<?, ?> type) {
        var provider =
                type.selfProviding() != null ?
                        type.selfProviding().get() :
                        new LogicBasedComponentProvider<>(type);

        switch (type.getSide()) {
            case SERVER ->
                    WaffleComponentProviders.SERVER_COMPONENTS_PROVIDER.add((ComponentsProvider<ServerGrid>) provider);
            case CLIENT ->
                    WaffleComponentProviders.CLIENT_COMPONENTS_PROVIDER.add((ComponentsProvider<ClientGrid>) provider);
            case COMMON -> {
                WaffleComponentProviders.SERVER_COMPONENTS_PROVIDER.add((ComponentsProvider<ServerGrid>) provider);
                WaffleComponentProviders.CLIENT_COMPONENTS_PROVIDER.add((ComponentsProvider<ClientGrid>) provider);
                WaffleComponentProviders.SHARED_COMPONENTS_PROVIDER.add((ComponentsProvider<Grid>) provider);
            }
        }
    }

    private static final DeferredRegister<GridLogicType<?, ?>> REGISTER = DeferredRegister.create(WaffleRegistries.LOGICS_KEY, WaffleBlocks.MODID);

    public static final DeferredHolder<GridLogicType<?, ?>, GridLogicType<IGridContraption, ContraptionLogic>> CONTRAPTION_LOGIC = REGISTER.register("contraption", () -> ContraptionLogic.TYPE);

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
