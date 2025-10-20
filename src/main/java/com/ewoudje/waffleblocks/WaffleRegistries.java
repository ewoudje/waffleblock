package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.api.components.GridLogicType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class WaffleRegistries {
    public static ResourceKey<Registry<GridSource.Factory>> GRID_SOURCE_KEY = ResourceKey.createRegistryKey(WaffleBlocks.resource("grid_sources"));
    public static ResourceKey<Registry<GridComponentType<?, ?>>> COMPONENTS_KEY = ResourceKey.createRegistryKey(WaffleBlocks.resource("components"));
    public static ResourceKey<Registry<GridLogicType<?, ?>>> LOGICS_KEY = ResourceKey.createRegistryKey(WaffleBlocks.resource("logics"));
    public static Registry<GridSource.Factory> GRID_SOURCE = new RegistryBuilder<>(GRID_SOURCE_KEY)
            .sync(true)
            .create();

    public static Registry<GridComponentType<?, ?>> COMPONENTS = new RegistryBuilder<>(COMPONENTS_KEY)
            .sync(true)
            .create();

    public static Registry<GridLogicType<?, ?>> LOGICS = new RegistryBuilder<>(LOGICS_KEY)
            .onBake(i -> {
                i.forEach(WaffleGridLogics::configureLogic);
                GridComponentType.configureAll();
            })
            .sync(true)
            .create();

    public static void register(IEventBus bus) {
        bus.addListener((NewRegistryEvent e) -> {
            e.register(GRID_SOURCE);
            e.register(COMPONENTS);
            e.register(LOGICS);
        });
    }
}
