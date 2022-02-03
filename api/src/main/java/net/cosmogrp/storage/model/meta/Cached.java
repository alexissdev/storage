package net.cosmogrp.storage.model.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking a model as a cached model.
 * Also, we can configure the type of cache to use.
 * <p>
 * NOTE: If the model hasn't this annotation,
 * it will not be cached. Maybe we can use this
 * feature to create punishments or something similar.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

    Strategy value() default Strategy.LOCAL;

    enum Strategy {
        LOCAL
    }

}
