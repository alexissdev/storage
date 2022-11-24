package com.pixeldv.storage.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.pixeldv.storage.dist.CachedRemoteModelService;
import com.pixeldv.storage.dist.LocalModelService;
import com.pixeldv.storage.mongo.model.DummyModel;
import com.pixeldv.storage.resolve.ResolverRegistry;

public final class TestHelper {

	private TestHelper() {
		throw new UnsupportedOperationException();
	}

	public static CachedRemoteModelService<DummyModel> create() {
		MongoClient client = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = client.getDatabase("admin");

		return (CachedRemoteModelService<DummyModel>)
				       MongoModelService.builder(DummyModel.class)
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
