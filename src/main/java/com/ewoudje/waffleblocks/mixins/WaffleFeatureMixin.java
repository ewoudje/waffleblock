package com.ewoudje.waffleblocks.mixins;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
@Repeatable(WaffleFeatureMixins.class)
public @interface WaffleFeatureMixin {

    Class<?> value();

    boolean client() default false;
    boolean accessor() default false;

    String docs() default "";

}
