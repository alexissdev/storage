package net.cosmogrp.storage.model;

import net.cosmogrp.storage.model.exception.NoSuchModelDataException;

import java.lang.annotation.Annotation;

/**
 * This is a utility class for
 * manage class annotations easier
 * and avoiding boilerplate code
 */
public final class AnnotationHelper {

    private AnnotationHelper() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Annotation> T getAnnotation(
            Class<?> clazz,
            Class<T> annotationClass,
            boolean required
    ) throws NoSuchModelDataException {
        T annotation = clazz.getAnnotation(annotationClass);
        if (annotation == null && required) {
            throw new NoSuchModelDataException(
                    clazz, annotationClass.getSimpleName()
            );
        }
        return annotation;
    }

}
