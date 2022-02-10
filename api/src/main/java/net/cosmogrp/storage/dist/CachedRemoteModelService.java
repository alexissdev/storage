package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class CachedRemoteModelService<T extends Model>
        extends RemoteModelService<T> {

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
    public @Nullable T getSync(String id) {
        return cacheModelService.getSync(id);
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
    public List<T> getAllSync() {
        return cacheModelService.getAllSync();
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
        List<T> models = cacheModelService.getAllSync();
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

}
