package net.cosmogrp.storage.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.LocalModelService;
import net.cosmogrp.storage.mongo.model.DummyModel;
import net.cosmogrp.storage.resolve.RelationalResolver;
import net.cosmogrp.storage.resolve.ResolverRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public final class TestHelper {

    private TestHelper() {
        throw new UnsupportedOperationException();
    }

    public static CachedRemoteModelService<DummyModel> create() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("admin");

        Map<String, RelationalResolver<DummyModel>> resolvers =
                new HashMap<>();

        resolvers.put("someValue", new RelationalResolver<>(DummyModel::getSomeValue));

        ResolverRegistry<DummyModel> resolverRegistry =
                new ResolverRegistry<>(resolvers);

        return new MongoModelService<>(
                Executors.newSingleThreadExecutor(),
                new LocalModelService<>(),
                resolverRegistry,
                database.getCollection("test"),
                DummyModel::fromDocument
        );
    }

}
