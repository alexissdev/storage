package net.cosmogrp.storage.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import net.cosmogrp.storage.mongo.meta.MongoModelMeta;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.exception.NoSuchModelDataException;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import java.util.concurrent.Executor;

public class MongoModelServiceProvider {

    private final MongoClient mongoClient;
    private final Executor executor;
    private final ObjectMapper mapper;
    private final String database;

    public MongoModelServiceProvider(
            MongoClient mongoClient,
            Executor executor,
            ObjectMapper mapper,
            String database
    ) {
        this.mongoClient = mongoClient;
        this.executor = executor;
        this.mapper = mapper;
        this.database = database;
    }

    public <T extends Model> MongoModelService<T> create(Class<T> modelClass)
            throws NoSuchModelDataException {
        MongoModelMeta<T> modelMeta = new MongoModelMeta<>(modelClass);

        return new MongoModelService<>(
                executor,
                modelMeta,
                JacksonMongoCollection.builder()
                        .withObjectMapper(mapper)
                        .build(
                                mongoClient, database,
                                modelMeta.getCollectionName(),
                                modelClass, UuidRepresentation.JAVA_LEGACY
                        )
        );
    }
}
