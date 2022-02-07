package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.meta.ModelMeta;
import net.cosmogrp.storage.redis.connection.RedisCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class RedisModelService<T extends Model>
        extends RemoteModelService<T> {
    private final AbstractRedisModelService<T> delegate;

    public RedisModelService(
            Executor executor,
            ModelMeta<T> modelMeta,
            Gson gson,
            RedisCache redisCache
    ) {
        super(executor);
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
