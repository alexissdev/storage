package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.AsyncModelService;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class RemoteModelService<T extends Model>
        extends AsyncModelService<T> {

    protected final ModelService<T> cacheModelService;

    public RemoteModelService(
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
            // cache model if possible
            if (cacheModelService != null) {
                cacheModelService.saveSync(model);
            }
        }

        return model;
    }

    @Override
    public @Nullable T getSync(String id) {
        if (cacheModelService == null) {
            return null;
        }

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
        if (cacheModelService == null) {
            return Collections.emptyList();
        } else {
            return cacheModelService.getAllSync();
        }
    }

    @Override
    public List<T> findAllSync() {
        List<T> loadedModels = internalFindAll();

        if (cacheModelService != null) {
            for (T model : loadedModels) {
                cacheModelService.saveSync(model);
            }
        }

        return loadedModels;
    }

    @Override
    public void saveSync(T model) {
        if (cacheModelService != null) {
            cacheModelService.saveSync(model);
        }

        internalSave(model);
    }

    @Override
    public void uploadSync(T model) {
        if (cacheModelService != null) {
            cacheModelService.deleteSync(model);
        }

        internalSave(model);
    }

    @Override
    public void uploadAllSync(Consumer<T> preUploadAction) {
        if (cacheModelService != null) {
            List<T> models = cacheModelService.getAllSync();
            for (T model : models) {
                preUploadAction.accept(model);
                uploadSync(model);
            }
        }
    }

    @Override
    public void deleteSync(T model) {
        model.delete();
        uploadSync(model);
    }

    @Override
    public T deleteSync(String id) {
        T model = getOrFindSync(id);

        if (model != null) {
            deleteSync(model);
        }

        return model;
    }

    protected abstract void internalSave(T model);

    protected abstract @Nullable T internalFind(String id);

    protected abstract List<T> internalFindAll();

}
