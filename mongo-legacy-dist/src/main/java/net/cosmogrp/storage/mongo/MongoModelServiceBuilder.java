package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.builder.LayoutModelServiceBuilder;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import net.cosmogrp.storage.mongo.codec.MongoModelParser;
import org.bson.Document;

public class MongoModelServiceBuilder<T extends Model & DocumentCodec>
        extends LayoutModelServiceBuilder<T, MongoModelServiceBuilder<T>> {

    private MongoDatabase database;
    private String collectionName;
    private MongoModelParser<T> modelParser;

    protected MongoModelServiceBuilder() {

    }

    public MongoModelServiceBuilder<T> database(MongoDatabase database) {
        this.database = database;
        return this;
    }

    public MongoModelServiceBuilder<T> modelParser(MongoModelParser<T> modelParser) {
        this.modelParser = modelParser;
        return this;
    }

    public MongoModelServiceBuilder<T> collection(String collection) {
        this.collectionName = collection;
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();

        MongoCollection<Document> collection =
                database.getCollection(collectionName);

        if (cacheModelService == null) {
            return new MongoModelService<>(executor, collection, modelParser);
        } else {
            return new CachedMongoModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, collection,
                    modelParser
            );
        }
    }

    @Override
    protected MongoModelServiceBuilder<T> back() {
        return this;
    }
}
