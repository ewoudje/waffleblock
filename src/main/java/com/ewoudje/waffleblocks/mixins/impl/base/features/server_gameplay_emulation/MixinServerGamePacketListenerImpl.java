package com.ewoudje.waffleblocks.mixins.impl.base.features.server_gameplay_emulation;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.impl.emulation.EntityEmulation;
import com.ewoudje.waffleblocks.util.GridBlockPos;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {


    @Shadow public ServerPlayer player;

    @Shadow public abstract void handleUseItemOn(ServerboundUseItemOnPacket packet);

    @WrapMethod(method = "handlePlayerAction")
    private void wb$handlePlayerAction(ServerboundPlayerActionPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleUseItemOn")
    public void wb$handleUseItemOn(ServerboundUseItemOnPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getHitResult().getBlockPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleBlockEntityTagQuery")
    public void wb$handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleSignUpdate")
    public void wb$handleSignUpdate(ServerboundSignUpdatePacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleJigsawGenerate")
    public void wb$handleJigsawGenerate(ServerboundJigsawGeneratePacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleSetCommandBlock")
    public void wb$handleSetCommandBlock(ServerboundSetCommandBlockPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleSetStructureBlock")
    public void wb$handleSetStructureBlock(ServerboundSetStructureBlockPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }

    @WrapMethod(method = "handleSetJigsawBlock")
    public void wb$handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet, Operation<Void> original) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, player.server);

        boolean success = false;
        try {
            success = wb$startPlayer(packet.getPos());
            original.call(packet);
        } finally {
            if (success) wb$endPlayer();
        }
    }


    @Unique
    private boolean wb$startPlayer(BlockPos pos) {
        try {
            Grid grid = GridBlockPos.getGrid(this.player.level(), pos);
            if (grid != null) {
                EntityEmulation.emulate(this.player, grid);
                return true;
            }
        } catch (Throwable throwable) {
            WaffleBlocks.LOGGER.error("server_gameplay_emulation/MixinServerPlayerGameMode#wb$startPlayer failed:", throwable);
        }

        return false;
    }

    @Unique
    private void wb$endPlayer() {
        try {
            EntityEmulation.revert(this.player);
        } catch (Throwable throwable) {
            WaffleBlocks.LOGGER.error("server_gameplay_emulation/MixinServerPlayerGameMode#wb$endPlayer failed:", throwable);
        }
    }

}
