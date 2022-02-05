package net.cosmogrp.storage.redis;

import com.google.gson.Gson;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.redis.connection.RedisCache;
import net.cosmogrp.storage.redis.meta.RedisModelMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class RedisModelService<T extends Model> extends RemoteModelService<T> {

    private final Gson gson;
    private final Class<T> type;
    private final RedisCache redisCache;
    private final String tableName;

    public RedisModelService(
            Executor executor,
            RedisModelMeta<T> modelMeta,
            Gson gson,
            RedisCache redisCache
    ) {
        super(executor, null);
        this.gson = gson;
        this.type = modelMeta.getType();
        this.redisCache = redisCache;
        this.tableName = modelMeta.getTableName();
    }

    @Override
    protected void internalSave(T model) {
        redisCache.set(
                tableName, model.getId(),
                gson.toJson(model)
        );
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
        List<String> values = redisCache.getAll(tableName);
        List<T> models = new ArrayList<>();

        for (String value : values) {
            models.add(gson.fromJson(value, type));
        }

        return models;
    }
}
