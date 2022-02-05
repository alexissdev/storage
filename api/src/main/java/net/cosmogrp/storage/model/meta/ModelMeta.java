package net.cosmogrp.storage.model.meta;

import net.cosmogrp.storage.model.Model;

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

    public ModelMeta(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

}
