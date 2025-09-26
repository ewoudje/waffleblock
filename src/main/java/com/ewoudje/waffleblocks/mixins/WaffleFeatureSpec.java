package com.ewoudje.waffleblocks.mixins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface WaffleFeatureSpec {
    String name();

    boolean isClientOnly() default false;
    boolean isCommonOnly() default false;

}
