package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class CachedRemoteModelService<T extends Model>
        extends CachedAsyncModelService<T> {

    protected final ModelService<T> cacheModelService;

    public CachedRemoteModelService(
            Executor executor,
            ModelService<T> cacheModelService
    ) {
        super(executor);
        this.cacheModelService = cacheModelService;
    }

    @Override
    public @Nullable T findSync(String id) {
        T model = internalFind(id);

        if (model != null) {
            // add to cache
            cacheModelService.saveSync(model);
        }

        return model;
    }

    @Override
    public List<T> findSync(String field, String value) {
        return null;
    }

    @Override
    public @Nullable T getSync(String id) {
        return cacheModelService.findSync(id);
    }

    @Override
    public List<T> getSync(String field, String value) {
        return null;
    }

    @Override
    public @Nullable T getOrFindSync(String id) {
        T model = getSync(id);

        if (model != null) {
            return model;
        }

        return findSync(id);
    }

    @Override
    public List<T> getOrFindSync(String field, String value) {
        return null;
    }

    @Override
    public List<T> getAllSync() {
        return cacheModelService.findAllSync();
    }

    @Override
    public List<T> findAllSync() {
        List<T> loadedModels = internalFindAll();

        for (T model : loadedModels) {
            cacheModelService.saveSync(model);
        }

        return loadedModels;
    }

    @Override
    public void saveSync(T model) {
        cacheModelService.saveSync(model);

        internalSave(model);
    }

    @Override
    public void uploadSync(T model) {
        cacheModelService.deleteSync(model);
        internalSave(model);
    }

    @Override
    public void uploadAllSync(Consumer<T> preUploadAction) {
        List<T> models = cacheModelService.findAllSync();
        for (T model : models) {
            preUploadAction.accept(model);
            uploadSync(model);
        }
    }

    @Override
    public void deleteSync(T model) {
        cacheModelService.deleteSync(model);
        internalDelete(model);
    }

    @Override
    public T deleteSync(String id) {
        T model = getOrFindSync(id);

        if (model != null) {
            deleteSync(model);
        }

        return model;
    }

    public void saveInCache(T model) {
        cacheModelService.saveSync(model);
    }

    public void deleteInCache(T model) {
        cacheModelService.deleteSync(model);
    }

    protected abstract void internalSave(T model);

    protected abstract void internalDelete(T model);

    protected abstract @Nullable T internalFind(String id);

    protected abstract List<T> internalFind(String field, String value);

    protected abstract List<T> internalFindAll();

}
