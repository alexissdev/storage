package com.pixeldv.storage.redis;

import com.google.gson.Gson;
import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.dist.RemoteModelService;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.redis.connection.RedisCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class RedisModelService<T extends Model>
	extends RemoteModelService<T> {

	private final Gson gson;
	private final Class<T> type;
	private final RedisCache redisCache;
	private final String tableName;
	private final int expireAfterSave;

	protected RedisModelService(
		Executor executor,
		Class<T> type,
		Gson gson, RedisCache redisCache,
		String tableName, int expireAfterSave
	) {
		super(executor);
		this.gson = gson;
		this.type = type;
		this.redisCache = redisCache;
		this.tableName = tableName;
		this.expireAfterSave = expireAfterSave;
	}

	public static <T extends Model> RedisModelServiceBuilder<T> builder(
		Class<T> type
	) {
		return new RedisModelServiceBuilder<>(type);
	}

	@Override
	public void saveSync(@NotNull T model) {
		redisCache.set(
			tableName, model.getId(),
			gson.toJson(model),
			expireAfterSave
		);
	}

	@Override
	public void deleteSync(@NotNull T model) {
		redisCache.del(tableName, model.getId());
	}

	@Override
	public @Nullable T findSync(@NotNull String id) {
		String json = redisCache.get(tableName, id);

		if (json == null) {
			return null;
		}

		return gson.fromJson(json, type);
	}

	@Override
	public List<T> findSync(@NotNull String field, @NotNull String value) {
		if (!field.equals(ModelService.ID_FIELD)) {
			throw new IllegalArgumentException(
				"Only ID field is supported for sync find"
			);
		}

		return Collections.singletonList(findSync(value));
	}

	@Override
	public List<T> findAllSync(@NotNull Consumer<T> postLoadAction) {
		List<String> values = redisCache.getAllValues(tableName);
		List<T> models = new ArrayList<>();

		for (String value : values) {
			T model = gson.fromJson(value, type);

			postLoadAction.accept(model);
			models.add(model);
		}

		return models;
	}
}
