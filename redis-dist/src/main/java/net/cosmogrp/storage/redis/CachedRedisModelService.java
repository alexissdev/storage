package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.meta.ModelMeta;
import net.cosmogrp.storage.redis.connection.RedisCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class CachedRedisModelService<T extends Model>
        extends CachedRemoteModelService<T> {
    private final AbstractRedisModelService<T> delegate;

    public CachedRedisModelService(
            Executor executor,
            ModelMeta<T> modelMeta,
            Gson gson,
            ModelService<T> cacheModelService,
            RedisCache redisCache
    ) {
        super(executor, cacheModelService);
        delegate = new AbstractRedisModelService<>(
                executor, modelMeta, gson, redisCache
        );
    }

    @Override
    protected void internalSave(T model) {
        delegate.internalSave(model);
    }

    @Override
    protected void internalDelete(T model) {
        delegate.internalDelete(model);
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return delegate.internalFind(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return delegate.internalFindAll();
    }
}
