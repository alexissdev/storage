package net.cosmogrp.storage.model.meta;

import net.cosmogrp.storage.model.AnnotationHelper;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.exception.NoSuchModelDataException;
import org.jetbrains.annotations.Nullable;

/**
 * Responsible for storing metadata about a model.
 * <p>
 * This class is immutable, meaning that it cannot
 * be changed after creation, all properties are
 * read one time only in model registration.
 *
 * @param <T>
 */
public class ModelMeta<T extends Model> {

    private final Class<T> type;
    private final Cached.Strategy cachedStrategy;

    public ModelMeta(Class<T> type) throws NoSuchModelDataException {
        this.type = type;

        Cached cached = AnnotationHelper.getAnnotation(
                type, Cached.class, false
        );

        if (cached != null) {
            cachedStrategy = cached.value();
        } else {
            cachedStrategy = null;
        }
    }

    public Class<T> getType() {
        return type;
    }

    /**
     * @return the cached strategy for this model
     * NOTE: If no cached strategy is defined, so
     * it might not be possible to cache the model.
     */
    public @Nullable Cached.Strategy getCachedStrategy() {
        return cachedStrategy;
    }
}
