package com.ewoudje.waffleblocks.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaffleDebug {
    public static final Logger LOGGER  = LoggerFactory.getLogger(WaffleDebug.class);

    public static void debug() {
        LOGGER.debug("DEBUG- Called");
    }
}
