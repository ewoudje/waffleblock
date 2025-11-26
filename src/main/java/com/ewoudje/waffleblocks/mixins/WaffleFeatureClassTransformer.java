package com.ewoudje.waffleblocks.mixins;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
@Repeatable(WaffleFeatureClassTransformers.class)
public @interface WaffleFeatureClassTransformer {
    Class<? extends WaffleClassTransformer> transformer();
    String target();

    String docs() default "";
}
