
@WaffleFeatureSpec(name = "blockpos")
@WaffleFeatureMixin(
        value = MixinDebugScreenOverlay.class,
        client = true,
        docs = "Show translated coordinates in f3"
)
@WaffleFeatureMixin(
        value = MixinLevel.class,
        docs = "Calls set/getBlockState on grids based on blockpos, also checks entity AABBs"
)
@WaffleFeatureMixin(
        value = MixinWorldBorder.class,
        docs = "Make GridBlockPos be within the world border"
)
package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.waffleblocks.mixins.WaffleFeatureMixin;
import com.ewoudje.waffleblocks.mixins.WaffleFeatureSpec;
