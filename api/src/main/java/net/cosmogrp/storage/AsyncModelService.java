package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class AsyncModelService<T extends Model>
        implements ModelService<T> {

    protected final Executor executor;

    public AsyncModelService(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<T> find(String id) {
        return CompletableFuture.supplyAsync(() -> findSync(id), executor);
    }

    public CompletableFuture<List<T>> findAll() {
        return CompletableFuture.supplyAsync(this::findAllSync, executor);
    }

    public CompletableFuture<Void> save(T model) {
        return CompletableFuture.runAsync(() -> saveSync(model), executor);
    }

    public CompletableFuture<Void> upload(T model) {
        return CompletableFuture.runAsync(() -> uploadSync(model), executor);
    }

    public CompletableFuture<Void> uploadAll(Consumer<T> preUploadAction) {
        return CompletableFuture.runAsync(() -> uploadAllSync(preUploadAction), executor);
    }

    public CompletableFuture<Void> delete(T model) {
        return CompletableFuture.runAsync(() -> deleteSync(model), executor);
    }

    public CompletableFuture<T> delete(String id) {
        return CompletableFuture.supplyAsync(() -> deleteSync(id), executor);
    }

}
