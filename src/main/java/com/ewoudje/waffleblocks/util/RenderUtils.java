package com.ewoudje.waffleblocks.util;

import com.mojang.blaze3d.systems.RenderSystem;

public class RenderUtils {

    public static float getPartialTick() {
        return (RenderSystem.getShaderGameTime() * 24000.0F) % 1f;
    }
}
