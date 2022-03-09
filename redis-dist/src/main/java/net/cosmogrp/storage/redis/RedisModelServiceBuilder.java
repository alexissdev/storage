package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.redis.connection.RedisCache;

import static net.cosmogrp.commons.Validate.notNull;

public class RedisModelServiceBuilder<T extends Model>
        extends LayoutModelServiceBuilder<T, RedisModelServiceBuilder<T>> {

    private final Class<T> type;
    private Gson gson;
    private String tableName;
    private int expireAfterSave;
    private RedisCache redisCache;

    protected RedisModelServiceBuilder(Class<T> type) {
        this.type = type;
    }

    public RedisModelServiceBuilder<T> gson(Gson gson) {
        this.gson = gson;
        return back();
    }

    public RedisModelServiceBuilder<T> tableName(String tableName) {
        this.tableName = tableName;
        return back();
    }

    public RedisModelServiceBuilder<T> expireAfterSave(int expireAfterSave) {
        this.expireAfterSave = expireAfterSave;
        return back();
    }

    public RedisModelServiceBuilder<T> redisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
        return back();
    }

    @Override
    protected RedisModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        notNull(gson, "gson");
        notNull(tableName, "tableName");
        notNull(redisCache, "redisCache");

        if (expireAfterSave <= 0) {
            expireAfterSave = -1;
        }

        if (cacheModelService == null) {
            return new RedisModelService<>(
                    executor, type, gson,
                    redisCache, tableName,
                    expireAfterSave
            );
        } else {
            return new CachedRedisModelService<>(
                    executor, type, resolverRegistry,
                    cacheModelService,
                    gson, redisCache, tableName,
                    expireAfterSave
            );
        }
    }
}
