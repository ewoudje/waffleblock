package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.util.PacketBlockPosReplacer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.thread.BlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PacketUtils.class)
public class MixinPacketUtils {
    @WrapMethod(method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")
    private static <T extends PacketListener> void wb$modifyArgs1(Packet<T> packet, T processor, BlockableEventLoop<?> executor, Operation<Void> original) {
        if (processor instanceof ServerGamePacketListenerImpl s) {
            original.call(PacketBlockPosReplacer.replace((Packet<ServerGamePacketListener>) packet, (ServerLevel) s.player.level()), processor, executor);
        } else if (processor instanceof ClientGamePacketListener) {
            original.call(PacketBlockPosReplacer.replace((Packet<ClientGamePacketListener>) packet, Minecraft.getInstance().level), processor, executor);
        } else {
            original.call(packet, processor, executor);
        }
    }
}
