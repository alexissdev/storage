package net.cosmogrp.storage.dist;

import net.cosmogrp.storage.model.Model;
import java.util.concurrent.Executor;

public abstract class AbstractModelService<T extends Model>
        extends AsyncModelService<T> {
    public AbstractModelService(Executor executor) {
        super(executor);
    }

    @Override
    public T deleteSync(String id) {
        T model = findSync(id);

        if (model != null) {
            deleteSync(model);
        }

        return model;
    }

}
