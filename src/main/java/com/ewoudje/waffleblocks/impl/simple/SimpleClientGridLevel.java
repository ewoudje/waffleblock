package com.ewoudje.waffleblocks.impl.simple;

import com.ewoudje.waffleblocks.impl.abst.AbstractClientGridLevel;
import com.ewoudje.waffleblocks.impl.abst.AbstractServerGridLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;

public class SimpleClientGridLevel extends AbstractClientGridLevel {
    public SimpleClientGridLevel(ClientLevel level) {
        super(level);
    }
}
