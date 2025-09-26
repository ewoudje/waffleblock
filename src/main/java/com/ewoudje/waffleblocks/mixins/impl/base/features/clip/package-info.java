/**
 * Adds mixins for chunk-backed grids
 */

@WaffleFeatureSpec(name = "clip")
@WaffleFeatureMixin(
        value = MixinBlockGetter.class,
        docs = "Handles grids with the clip components"
)
@WaffleFeatureMixin(
        value = MixinLevelRenderer.class,
        client = true,
        docs = "Render gridblockpos hitoutline"
)
package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.waffleblocks.mixins.WaffleFeatureMixin;
import com.ewoudje.waffleblocks.mixins.WaffleFeatureSpec;
