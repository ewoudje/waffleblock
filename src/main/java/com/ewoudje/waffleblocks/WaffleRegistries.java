package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.GridSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class WaffleRegistries {
    public static ResourceKey<Registry<GridSource.Factory>> GRID_SOURCE_KEY = ResourceKey.createRegistryKey(WaffleBlocks.resource("grid_sources"));
    public static Registry<GridSource.Factory> GRID_SOURCE = new RegistryBuilder<>(GRID_SOURCE_KEY)
            .sync(true)
            .create();

    public static void register(IEventBus bus) {
        bus.addListener((NewRegistryEvent e) -> {
            e.register(GRID_SOURCE);
        });
    }
}
