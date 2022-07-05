package team.unnamed.pixel.storage.dist;

import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.model.Model;

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

    public CompletableFuture<List<T>> find(String field, String value) {
        return CompletableFuture.supplyAsync(() -> findSync(field, value), executor);
    }

    public CompletableFuture<List<T>> findAll() {
        return CompletableFuture.supplyAsync(this::findAllSync, executor);
    }

    public CompletableFuture<List<T>> findAll(Consumer<T> postLoadAction) {
        return CompletableFuture.supplyAsync(() -> findAllSync(postLoadAction), executor);
    }

    public CompletableFuture<Void> save(T model) {
        return CompletableFuture.runAsync(() -> saveSync(model), executor);
    }

    public CompletableFuture<Void> delete(T model) {
        return CompletableFuture.runAsync(() -> deleteSync(model), executor);
    }

    public CompletableFuture<T> delete(String id) {
        return CompletableFuture.supplyAsync(() -> deleteSync(id), executor);
    }

}
