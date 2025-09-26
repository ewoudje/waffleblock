package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;

public class PacketBlockPosReplacer {

    public static Packet<ServerGamePacketListener> replace(Packet<ServerGamePacketListener> p, ServerLevel level) {
        GridLevel gLevel = GridLevelManager.getLevel(level);
        if (gLevel != null) {
            if (p instanceof ServerboundUseItemOnPacket f) {
                GridBlockPos g = GridBlockPos.asGridBlockPos(gLevel, f.getHitResult().getBlockPos());
                if (g != null) {
                    return new ServerboundUseItemOnPacket(f.getHand(), f.getHitResult().withPosition(g), f.getSequence());
                }
            }
        }

        return p;
    }

    public static Packet<ClientGamePacketListener> replace(Packet<ClientGamePacketListener> p, ClientLevel level) {
        return p;
    }

}
