package net.cosmogrp.storage.sql.meta;

import net.cosmogrp.storage.sql.identity.DataType;
import net.cosmogrp.storage.sql.identity.SQLConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLTable {

    String name();

    Element[] elements();

    String protocol() default "mysql";

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Element {

        String column();

        DataType type();

        SQLConstraint[] constraints() default SQLConstraint.NOT_NULL;

    }

}
