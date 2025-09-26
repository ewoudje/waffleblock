package com.ewoudje.waffleblocks.mixins.impl.debug.features.gamemode;


import com.ewoudje.waffleblocks.util.WaffleDebug;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode {


  /**
   * @author ewoudje
   * @reason Enable debugging
   */
  @Overwrite
  private void debugLogging(BlockPos pos, boolean terminate, int sequence, String message) {

      String builder = sequence +
              ": " +
              pos +
              " -> " +
              message;

    WaffleDebug.LOGGER.debug(builder);
  }
}
