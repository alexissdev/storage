package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.cosmogrp.storage.dist.CachedModelService;
import net.cosmogrp.storage.dist.LocalModelService;
import net.cosmogrp.storage.mongo.model.DummyModel;
import net.cosmogrp.storage.resolve.ResolverRegistry;

public final class TestHelper {

    private TestHelper() {
        throw new UnsupportedOperationException();
    }

    public static CachedModelService<DummyModel> create() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("admin");

        return (CachedModelService<DummyModel>)
                MongoModelService.<DummyModel>builder()
                        .modelParser(DummyModel::fromDocument)
                        .cachedService(LocalModelService.create())
                        .resolverRegistry(ResolverRegistry.<DummyModel>builder()
                                .register("someValue", DummyModel::getSomeValue)
                                .build())
                        .collection("test")
                        .database(database)
                        .build();
    }

}
