package net.cosmogrp.storage.model.meta;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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
    private final Map<String, Object> properties;

    public ModelMeta(Class<T> type) {
        this.type = type;
        this.properties = new HashMap<>();
    }

    public Class<T> getType() {
        return type;
    }

    public ModelMeta<T> addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public @Nullable Object getProperty(String key) {
        return properties.get(key);
    }
}
