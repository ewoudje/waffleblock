@FlourFeatureSpec(name = "clip")
@FlourFeatureMixin(
        value = MixinBlockGetter.class,
        docs = "Handles grids with the clip components"
)
@FlourFeatureMixin(
        value = MixinLevelRenderer.class,
        client = true,
        docs = "Render gridblockpos hitoutline"
)
@FlourFeatureClassTransformer(
        transformer = TransformerBlockHitResult.class,
        target = "net.minecraft.world.phys.BlockHitResult",
        docs = "Make location local to the given blockpos"
)
package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.mixinflour.FlourFeatureClassTransformer;
import com.ewoudje.mixinflour.FlourFeatureMixin;
import com.ewoudje.mixinflour.FlourFeatureSpec;