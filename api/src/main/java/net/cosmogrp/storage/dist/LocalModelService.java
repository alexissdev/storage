package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LocalModelService<T extends Model>
        implements ModelService<T> {

    private final Map<String, T> cache;

    public LocalModelService() {
        this.cache = new HashMap<>();
    }

    @Override
    public @Nullable T findSync(String id) {
        return cache.get(id);
    }

    @Override
    public @Nullable T getSync(String id) {
        return findSync(id);
    }

    @Override
    public @Nullable T getOrFindSync(String id) {
        return findSync(id);
    }

    @Override
    public List<T> getAllSync() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<T> findAllSync() {
        return getAllSync();
    }

    @Override
    public void saveSync(T model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void uploadSync(T model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void uploadAllSync(Consumer<T> preUploadAction) {
        // nothing is supposed to be done here
    }

    @Override
    public void deleteSync(T model) {
        cache.remove(model.getId());
    }

    @Override
    public T deleteSync(String id) {
        return cache.remove(id);
    }
}
