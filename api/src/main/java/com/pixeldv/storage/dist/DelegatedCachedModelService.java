package com.pixeldv.storage.dist;

import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.resolve.ResolverRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class DelegatedCachedModelService<T extends Model>
        extends CachedRemoteModelService<T> {

    protected final ModelService<T> delegate;

    public DelegatedCachedModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            ModelService<T> delegate
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.delegate = delegate;
    }

    @Override
    public List<T> findSync(String field, String value) {
        return delegate.findSync(field, value);
    }

    @Override
    protected void internalSave(T model) {
        delegate.saveSync(model);
    }

    @Override
    protected void internalDelete(T model) {
        delegate.deleteSync(model);
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return delegate.findSync(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return delegate.findAllSync();
    }
}
