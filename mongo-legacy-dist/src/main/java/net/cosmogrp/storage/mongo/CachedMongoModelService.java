package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoCollection;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import net.cosmogrp.storage.mongo.codec.MongoModelParser;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class CachedMongoModelService<T extends DocumentCodec & Model>
        extends CachedRemoteModelService<T> {

    private final MongoModelService<T> delegate;

    public CachedMongoModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            MongoCollection<Document> mongoCollection,
            MongoModelParser<T> mongoModelParser
    ) {
        super(executor, cacheModelService, resolverRegistry);

        this.delegate = new MongoModelService<>(
                executor, mongoCollection,
                mongoModelParser
        );
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
