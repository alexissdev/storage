package team.unnamed.pixel.storage.redis;

import com.google.gson.Gson;
import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.builder.LayoutModelServiceBuilder;
import team.unnamed.pixel.storage.dist.DelegatedCachedModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.redis.connection.RedisCache;
import team.unnamed.pixel.storage.util.Validate;

import static team.unnamed.pixel.storage.util.Validate.notNull;

public class RedisModelServiceBuilder<T extends Model>
        extends LayoutModelServiceBuilder<T, RedisModelServiceBuilder<T>> {

    private Gson gson;
    private String tableName;
    private int expireAfterSave;
    private RedisCache redisCache;

    protected RedisModelServiceBuilder(Class<T> type) {
        super(type);
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
        Validate.notNull(gson, "gson");
        Validate.notNull(tableName, "tableName");
        Validate.notNull(redisCache, "redisCache");

        if (expireAfterSave <= 0) {
            expireAfterSave = -1;
        }

        ModelService<T> modelService = new RedisModelService<>(
                executor, type, gson,
                redisCache, tableName,
                expireAfterSave
        );

        if (cacheModelService == null) {
            return modelService;
        } else {
            return new DelegatedCachedModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, modelService
            );
        }
    }
}
