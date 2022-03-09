package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.redis.connection.RedisCache;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class CachedRedisModelService<T extends Model>
        extends CachedRemoteModelService<T> {
    private final RedisModelService<T> delegate;

    protected CachedRedisModelService(
            Executor executor,
            Class<T> type,
            ResolverRegistry<T> resolverRegistry,
            ModelService<T> cacheModelService,
            Gson gson, RedisCache redisCache,
            String tableName, int expireAfterSave
    ) {
        super(executor, cacheModelService, resolverRegistry);
        delegate = new RedisModelService<>(
                executor, type, gson, redisCache,
                tableName, expireAfterSave
        );
    }

    @Override
    protected void internalSave(T model) {
        delegate.saveSync(model);
    }

    @Override
    protected void internalDelete(T model) {
        delegate.deleteSync(model);
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return delegate.findSync(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return delegate.findAllSync();
    }

    @Override
    public List<T> findSync(String field, String value) {
        return delegate.findSync(field, value);
    }
}
