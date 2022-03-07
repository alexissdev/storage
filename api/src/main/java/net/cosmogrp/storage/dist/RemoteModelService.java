package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public abstract class RemoteModelService<T extends Model>
        extends AsyncModelService<T> {
    public RemoteModelService(Executor executor) {
        super(executor);
    }

    @Override
    public @Nullable T findSync(String id) {
        return internalFind(id);
    }

    @Override
    public List<T> findAllSync() {
        return internalFindAll();
    }

    @Override
    public void saveSync(T model) {
        internalSave(model);
    }

    @Override
    public void deleteSync(T model) {
        internalDelete(model);
    }

    @Override
    public T deleteSync(String id) {
        T model = findSync(id);

        if (model != null) {
            deleteSync(model);
        }

        return model;
    }

    protected abstract void internalSave(T model);

    protected abstract void internalDelete(T model);

    protected abstract @Nullable T internalFind(String id);

    protected abstract List<T> internalFindAll();

}
