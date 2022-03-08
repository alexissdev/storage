package net.cosmogrp.storage.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.meta.ModelMeta;
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

    public <T extends Model> MongoModelService<T> create(
            Class<T> modelClass,
            String collectionName,
            ModelService<T> cacheModelService
    ) {
        return new MongoModelService<>(
                executor,
                new ModelMeta<>(modelClass),
                cacheModelService,
                JacksonMongoCollection.builder()
                        .withObjectMapper(mapper)
                        .build(
                                mongoClient, database,
                                collectionName,
                                modelClass, UuidRepresentation.JAVA_LEGACY
                        )
        );
    }
}
