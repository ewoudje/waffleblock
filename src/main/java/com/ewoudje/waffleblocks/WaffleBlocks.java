package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.impl.GridLevelManager;
import com.ewoudje.waffleblocks.impl.emulation.EntityEmulation;
import com.ewoudje.waffleblocks.impl.emulation.GridContext;
import com.ewoudje.wafflecreate.ContraptionGridSource;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WaffleBlocks.MODID)
public class WaffleBlocks {
    public static final String MODID = "waffleblocks";
    public static final Logger LOGGER = LogUtils.getLogger();



    public WaffleBlocks(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        WaffleAttachments.register(modEventBus);
        WaffleRegistries.register(modEventBus);
        WafflePayloads.register(modEventBus);
        WaffleGridComponents.register(modEventBus);
        WaffleGridLogics.register(modEventBus);
        GridLevelManager.register(modEventBus);
        modEventBus.addListener((RegisterEvent event) -> event.register(WaffleRegistries.GRID_SOURCE_KEY, resource("contraptions"), () -> ContraptionGridSource.FACTORY));


        NeoForge.EVENT_BUS.addListener(this::tick);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("{}{}", Config.magicNumberIntroduction, Config.magicNumber);
    }

    /**
     * For debugging
     */
    private void tick(ServerTickEvent.Post event) {
        GridContext.tickCheck();
        EntityEmulation.tickCheck();
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
