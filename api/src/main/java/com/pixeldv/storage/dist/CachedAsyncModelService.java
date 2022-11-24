package com.pixeldv.storage.dist;

import com.pixeldv.storage.CachedModelService;
import com.pixeldv.storage.model.Model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class CachedAsyncModelService<T extends Model>
		extends AsyncModelService<T>
		implements CachedModelService<T> {
	public CachedAsyncModelService(Executor executor) {
		super(executor);
	}

	public CompletableFuture<T> get(String id) {
		return CompletableFuture.supplyAsync(() -> getSync(id), executor);
	}

	public CompletableFuture<List<T>> get(String field, String value) {
		return CompletableFuture.supplyAsync(() -> getSync(field, value), executor);
	}

	public CompletableFuture<T> getOrFind(String id) {
		return CompletableFuture.supplyAsync(() -> getOrFindSync(id), executor);
	}

	public CompletableFuture<List<T>> getOrFind(String field, String value) {
		return CompletableFuture.supplyAsync(() -> getOrFindSync(field, value), executor);
	}

	public CompletableFuture<List<T>> getAll() {
		return CompletableFuture.supplyAsync(this::getAllSync, executor);
	}

	public CompletableFuture<Void> upload(T model) {
		return CompletableFuture.runAsync(() -> uploadSync(model), executor);
	}

	public CompletableFuture<Void> uploadAll() {
		return uploadAll(t -> { });
	}

	public CompletableFuture<Void> uploadAll(Consumer<T> preUploadAction) {
		return CompletableFuture.runAsync(() -> uploadAllSync(preUploadAction), executor);
	}

	public CompletableFuture<Void> saveAll() {
		return saveAll(t -> { });
	}

	public CompletableFuture<Void> saveAll(Consumer<T> preSaveAction) {
		return CompletableFuture.runAsync(() -> saveAllSync(preSaveAction), executor);
	}
}
