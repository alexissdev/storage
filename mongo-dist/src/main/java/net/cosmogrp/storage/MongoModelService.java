package net.cosmogrp.storage;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.cosmogrp.storage.dist.LocalModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.meta.Cached;
import net.cosmogrp.storage.model.meta.ModelMeta;
import org.jetbrains.annotations.Nullable;
import org.mongojack.JacksonMongoCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class MongoModelService<T extends Model>
        extends AsyncModelService<T> {

    private final JacksonMongoCollection<T> collection;
    private final ModelService<T> cacheModelService;

    public MongoModelService(
            Executor executor,
            ModelMeta<T> modelMeta,
            JacksonMongoCollection<T> collection
    ) {
        super(executor);
        this.collection = collection;

        Cached.Strategy strategy = modelMeta.getCachedStrategy();

        // same models cannot be cached
        if (strategy == null) {
            this.cacheModelService = null;
        } else {
            if (strategy == Cached.Strategy.LOCAL) {
                this.cacheModelService = new LocalModelService<>();
            } else {
                this.cacheModelService = null;
            }
        }
    }

    @Override
    public @Nullable T findSync(String id) {
        T model = collection.findOneById(id);

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
        List<T> loadedModels = collection.find()
                .into(new ArrayList<>());

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

    private void internalSave(T model) {
        collection.replaceOne(
                Filters.eq("_id", model.getId()),
                model, new ReplaceOptions().upsert(true)
        );
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
}
