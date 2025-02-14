package com.pixeldv.storage;

import com.pixeldv.storage.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface CachedModelService<T extends Model>
	extends ModelService<T> {

	@Nullable T getSync(@NotNull String id);

	@Nullable List<T> getSync(@NotNull String field, @NotNull String value);

	@Nullable T getOrFindSync(@NotNull String id);

	@Nullable List<T> getOrFindSync(@NotNull String field, @NotNull String value);

	@Nullable List<T> getAllSync();

	/**
	 * Uploads the model to the server
	 *
	 * @param model
	 * 	The model to be uploaded.
	 */
	void uploadSync(@NotNull T model);

	/**
	 * Upload all the files in the current directory to the remote server
	 *
	 * @param preUploadAction
	 * 	a function that takes a single parameter, which is the file to be uploaded.
	 */
	void uploadAllSync(@Nullable Consumer<T> preUploadAction);

	default void uploadAllSync() {
		uploadAllSync(null);
	}

	void saveAllSync(@Nullable Consumer<T> preSaveAction);

	default void saveAllSync() {
		saveAllSync(null);
	}
}
