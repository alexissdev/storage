package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CachedModelService<T extends Model>
        extends ModelService<T> {

    default @Nullable T getSync(String id) {
        List<T> list = getSync(ID_FIELD, id);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    List<T> getSync(String field, String value);

    default @Nullable T getOrFindSync(String id) {
        List<T> list = getOrFindSync(ID_FIELD, id);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    List<T> getOrFindSync(String field, String value);

    List<T> getAllSync();

}
