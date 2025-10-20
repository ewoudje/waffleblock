package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.api.components.interaction.ClipableComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEffectComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEmbeddingComponent;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelVisualManagersComponent;
import com.ewoudje.waffleblocks.api.components.world.BlockEntitiesContainerComponent;
import com.ewoudje.waffleblocks.api.components.world.GetBlockStateComponent;
import com.ewoudje.waffleblocks.api.components.world.SetBlockStateComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

public class WaffleGridComponents {

    private static final DeferredRegister<GridComponentType<?, ?>> REGISTER = DeferredRegister.create(WaffleRegistries.COMPONENTS_KEY, WaffleBlocks.MODID);

    public static final Supplier<GridComponentType<Grid, ClipableComponent>> CLIPABLE = REGISTER.register("clipable", () -> ClipableComponent.TYPE);
    public static final Supplier<GridComponentType<Grid, GetBlockStateComponent>> GET_BLOCKSTATE = REGISTER.register("get_blockstate", () -> GetBlockStateComponent.TYPE);
    public static final Supplier<GridComponentType<Grid, SetBlockStateComponent>> SET_BLOCKSTATE = REGISTER.register("set_blockstate", () -> SetBlockStateComponent.TYPE);
    public static final Supplier<GridComponentType<Grid, BlockEntitiesContainerComponent>> BLOCKENTITIES_CONTAINER = REGISTER.register("blockentities_container", () -> BlockEntitiesContainerComponent.TYPE);

    public static void register(IEventBus bus) {
        bus.addListener((RegisterEvent e) -> {
            e.register(WaffleRegistries.COMPONENTS_KEY, ResourceLocation.fromNamespaceAndPath("flywheel", "effect"), () -> FlywheelEffectComponent.TYPE);
            e.register(WaffleRegistries.COMPONENTS_KEY, ResourceLocation.fromNamespaceAndPath("flywheel", "embedding"), () -> FlywheelEmbeddingComponent.TYPE);
            e.register(WaffleRegistries.COMPONENTS_KEY, ResourceLocation.fromNamespaceAndPath("flywheel", "visual_managers"), () -> FlywheelVisualManagersComponent.TYPE);
        });

        REGISTER.register(bus);
    }
}
