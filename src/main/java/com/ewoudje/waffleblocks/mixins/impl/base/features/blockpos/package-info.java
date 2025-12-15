
@FlourFeatureSpec(name = "blockpos")
@FlourFeatureMixin(
        value = MixinDebugScreenOverlay.class,
        client = true,
        docs = "Show translated coordinates in f3"
)
@FlourFeatureMixin(
        value = MixinLevel.class,
        docs = "Calls set/getBlockState on grids based on blockpos, also checks entity AABBs"
)
@FlourFeatureMixin(
        value = MixinWorldBorder.class,
        docs = "Make GridBlockPos be within the world border"
)
package com.ewoudje.waffleblocks.mixins.impl.base.features.blockpos;

import com.ewoudje.mixinflour.FlourFeatureMixin;
import com.ewoudje.mixinflour.FlourFeatureSpec;