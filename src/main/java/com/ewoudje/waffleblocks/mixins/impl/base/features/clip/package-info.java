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
@WaffleFeatureClassTransformer(
        transformer = TransformerBlockHitResult.class,
        target = "net.minecraft.world.phys.BlockHitResult",
        docs = "Make location local to the given blockpos"
)
package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.waffleblocks.mixins.WaffleFeatureClassTransformer;
import com.ewoudje.waffleblocks.mixins.WaffleFeatureMixin;
import com.ewoudje.waffleblocks.mixins.WaffleFeatureSpec;
