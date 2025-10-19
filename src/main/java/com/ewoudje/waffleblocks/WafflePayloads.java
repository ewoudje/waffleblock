package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import com.ewoudje.waffleblocks.impl.payloads.NewGridPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class WafflePayloads {

    private static void registerMainThreadPayloads(PayloadRegistrar registrar) {
        registrar.playToClient(
                NewGridPacket.TYPE,
                NewGridPacket.STREAM_CODEC,
                (c, ctx) -> {
                    GridLevelManager.getCurrentClientLevel().createNewGrid(c.gridId(), (GridSource.Factory<Object>) c.sourceFactory(), c.context());
                }
        );
    }

    public static void register(IEventBus bus) {
        bus.addListener((RegisterPayloadHandlersEvent e) -> {
            PayloadRegistrar registrar = e.registrar("1");
            registerMainThreadPayloads(registrar.executesOn(HandlerThread.MAIN));
        });
    }

}
