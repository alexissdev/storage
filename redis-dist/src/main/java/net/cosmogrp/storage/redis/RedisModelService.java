package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.redis.connection.RedisCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class RedisModelService<T extends Model>
        extends RemoteModelService<T> {
    private final AbstractRedisModelService<T> delegate;

    public RedisModelService(
            Executor executor,
            Class<T> type,
            Gson gson, RedisCache redisCache,
            String tableName, int expireAfterSave
    ) {
        super(executor);
        delegate = new AbstractRedisModelService<>(
                executor, type, gson, redisCache,
                tableName, expireAfterSave
        );
    }

    @Override
    public List<T> findSync(String field, String value) {
        return delegate.findSync(field, value);
    }

    @Override
    public List<T> findAllSync() {
        return delegate.findAllSync();
    }

    @Override
    public @Nullable T findSync(String id) {
        return delegate.findSync(id);
    }

    @Override
    public void saveSync(T model) {
        delegate.saveSync(model);
    }

    @Override
    public void deleteSync(T model) {
        delegate.deleteSync(model);
    }
}
