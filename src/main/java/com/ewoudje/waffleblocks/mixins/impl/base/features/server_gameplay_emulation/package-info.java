/**
 * Every packet that contains a blockpos, will be checked if it contains a gridblockpos, and if so it will emulate the player existing in the grid instead in the world.
 */

@WaffleFeatureSpec(name = "server_gameplay_emulation", isCommonOnly = true)
package com.ewoudje.waffleblocks.mixins.impl.base.features.server_gameplay_emulation;

import com.ewoudje.waffleblocks.mixins.WaffleFeatureSpec;
