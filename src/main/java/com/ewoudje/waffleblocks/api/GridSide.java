package com.ewoudje.waffleblocks.api;

public enum GridSide {
    SERVER,
    CLIENT,
    COMMON;

    public static GridSide of(Class<? extends Grid> gridClass) {
        if (ClientGrid.class.isAssignableFrom(gridClass)) {
            if (ServerGrid.class.isAssignableFrom(gridClass)) {
                return COMMON;
            } else return CLIENT;
        } else if (ServerGrid.class.isAssignableFrom(gridClass)) {
            return SERVER;
        } else {
            return COMMON;
        }
    }
}
