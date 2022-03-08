package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MongoModelService<T extends DocumentCodec & Model>
        extends CachedRemoteModelService<T> {

    private final MongoCollection<Document> mongoCollection;
    private final MongoModelParser<T> mongoModelParser;

    public MongoModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            MongoCollection<Document> mongoCollection,
            MongoModelParser<T> mongoModelParser
    ) {
        super(executor, cacheModelService, resolverRegistry);

        this.mongoCollection = mongoCollection;
        this.mongoModelParser = mongoModelParser;
    }

    @Override
    protected void internalSave(T model) {
        mongoCollection.replaceOne(
                Filters.eq("_id", model.getId()),
                model.toDocument(),
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    protected void internalDelete(T model) {
        mongoCollection.deleteOne(Filters.eq("_id", model.getId()));
    }

    @Override
    protected @Nullable T internalFind(String id) {
        Document document = mongoCollection
                .find(Filters.eq("_id", id))
                .first();

        if (document == null) {
            return null;
        }

        return mongoModelParser.parse(DocumentReader.create(document));
    }

    @Override
    protected List<T> internalFindAll() {
        List<Document> documents = mongoCollection.find()
                .into(new ArrayList<>());

        List<T> models = new ArrayList<>();

        for (Document document : documents) {
            models.add(mongoModelParser.parse(DocumentReader.create(document)));
        }

        return models;
    }

    @Override
    public List<T> findSync(String field, String value) {
        List<T> models = new ArrayList<>();

        for (Document document : mongoCollection
                .find(Filters.eq(field, value))) {
            models.add(mongoModelParser
                    .parse(DocumentReader
                            .create(document)));
        }

        return models;
    }
}
