package com.ewoudje.waffleblocks.impl;

import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.ServerGridLevel;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.rendering.FlywheelEffectComponent;
import com.ewoudje.waffleblocks.impl.simple.SimpleClientGridLevel;
import com.ewoudje.waffleblocks.impl.simple.SimpleServerGridLevel;
import dev.engine_room.flywheel.api.event.ReloadLevelRendererEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;

public class GridLevelManager {
    private static final HashMap<Level, GridLevel> levels = new HashMap<>();

    public static void register(IEventBus bus) {
        bus.addListener(LevelEvent.Load.class, e -> {
            getLevel((Level) e.getLevel());
        });


        ComponentGetter<FlywheelEffectComponent> getter = FlywheelEffectComponent.TYPE.getter();
        bus.addListener(ReloadLevelRendererEvent.class,
                e ->
                        getter.getAllComponentsPaired(getClientLevel(e.level()))
                        .forEach(p -> FlywheelEffectComponent.onAdd(p.getFirst(), p.getSecond()))
        );
    }

    public static GridLevel getLevel(Level level) {
        if (level.isClientSide) {
            if (level instanceof ClientLevel cl) {
                return getClientLevel(cl);
            } else {
                //CUSTOM CLIENT LEVEL?

                return null;
            }
        } else if (level instanceof ServerLevel server) {
            return getServerLevel(server);
        } else {
            // CUSTOM SERVER LEVEL?

            return null;
        }
    }

    public static ClientGridLevel getClientLevel(ClientLevel level) {
        return (ClientGridLevel) levels.computeIfAbsent(level, i -> createClientGridLevel(level));
    }

    public static ServerGridLevel getServerLevel(ServerLevel level) {
        return (ServerGridLevel) levels.computeIfAbsent(level, i -> createServerGridLevel(level));
    }


    private static ClientGridLevel createClientGridLevel(ClientLevel level) {
        return new SimpleClientGridLevel(level);
    }

    private static ServerGridLevel createServerGridLevel(ServerLevel level) {
        return new SimpleServerGridLevel(level);
    }

    public static ClientGridLevel getCurrentClientLevel() {
        return getClientLevel(Minecraft.getInstance().level);
    }
}
