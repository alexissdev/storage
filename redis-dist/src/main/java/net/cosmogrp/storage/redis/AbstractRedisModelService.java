package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.meta.ModelMeta;
import net.cosmogrp.storage.redis.connection.RedisCache;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class AbstractRedisModelService<T extends Model>
        extends RemoteModelService<T> {
    private final Gson gson;
    private final Class<T> type;
    private final RedisCache redisCache;
    private final String tableName;
    private final int expireAfterSave;

    public AbstractRedisModelService(
            Executor executor,
            ModelMeta<T> modelMeta,
            Gson gson,
            RedisCache redisCache
    ) {
        super(executor);
        this.gson = gson;
        this.type = modelMeta.getType();
        this.redisCache = redisCache;
        this.tableName = (String) modelMeta.getProperty("redis-table");

        Integer expireAfterSave = (Integer) modelMeta
                .getProperty("redis-expire");

        if (expireAfterSave == null) {
            this.expireAfterSave = -1;
        } else {
            this.expireAfterSave = expireAfterSave;
        }
    }

    @Override
    protected void internalSave(T model) {
        redisCache.set(
                tableName, model.getId(),
                gson.toJson(model),
                expireAfterSave
        );
    }

    @Override
    protected void internalDelete(T model) {
        redisCache.del(tableName, model.getId());
    }

    @Override
    protected @Nullable T internalFind(String id) {
        String json = redisCache.get(tableName, id);

        if (json == null) {
            return null;
        }

        return gson.fromJson(json, type);
    }

    @Override
    protected List<T> internalFindAll() {
        List<String> values = redisCache.getAllValues(tableName);
        List<T> models = new ArrayList<>();

        for (String value : values) {
            models.add(gson.fromJson(value, type));
        }

        return models;
    }

    @Override
    public List<T> findSync(String field, String value) {
        if (!field.equals(ID_FIELD)) {
            throw new IllegalArgumentException(
                    "Only ID field is supported for sync find"
            );
        }

        return Collections.singletonList(internalFind(value));
    }
}
