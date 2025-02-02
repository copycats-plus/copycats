package com.copycatsplus.copycats.foundation.annotation;

import com.copycatsplus.copycats.compat.Mods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* This annotation is used to make mixins only enable themselves if the required mod(s) are installed
*/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModMixin {

    /**
     * The mixin should only be enabled if any of these mods are present
     */
    Mods[] requiredMods();

    boolean applyIfPresent() default true;
}
