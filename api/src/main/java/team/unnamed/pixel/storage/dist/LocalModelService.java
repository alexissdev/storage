package team.unnamed.pixel.storage.dist;

import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LocalModelService<T extends Model>
        implements ModelService<T> {

    private final Map<String, T> cache;

    private LocalModelService() {
        this.cache = new HashMap<>();
    }

    @Override
    public @Nullable T findSync(String id) {
        return cache.get(id);
    }

    @Override
    public List<T> findSync(String field, String value) {
        return Collections.singletonList(findSync(value));
    }

    @Override
    public List<T> findAllSync(Consumer<T> postLoadAction) {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void saveSync(T model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void deleteSync(T model) {
        cache.remove(model.getId());
    }

    @Override
    public T deleteSync(String id) {
        return cache.remove(id);
    }

    public static <T extends Model> LocalModelService<T> create() {
        return new LocalModelService<>();
    }
}
