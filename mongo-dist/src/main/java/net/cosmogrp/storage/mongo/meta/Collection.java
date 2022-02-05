package net.cosmogrp.storage.mongo.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will be necessary to mark
 * all models which will be stored in database.
 * For indicate the name of the collection in database.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Collection {

    String value();

}
