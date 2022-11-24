package com.pixeldv.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.builder.LayoutModelServiceBuilder;
import com.pixeldv.storage.dist.DelegatedCachedModelService;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.mongo.codec.DocumentCodec;
import com.pixeldv.storage.mongo.codec.MongoModelParser;
import com.pixeldv.storage.util.Validate;
import org.bson.Document;

public class MongoModelServiceBuilder<T extends Model & DocumentCodec>
		extends LayoutModelServiceBuilder<T, MongoModelServiceBuilder<T>> {

	private MongoDatabase database;
	private String collectionName;
	private MongoModelParser<T> modelParser;

	protected MongoModelServiceBuilder(Class<T> type) {
		super(type);
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
		Validate.notNull(modelParser, "modelParser");
		Validate.notNull(database, "database");
		Validate.notNull(collectionName, "collectionName");

		MongoCollection<Document> collection =
				database.getCollection(collectionName);

		MongoModelService<T> modelService =
				new MongoModelService<>(executor, collection, modelParser);

		if (cacheModelService == null) {
			return modelService;
		} else {
			return new DelegatedCachedModelService<>(
					executor, cacheModelService,
					resolverRegistry, modelService
			);
		}
	}

	@Override
	protected MongoModelServiceBuilder<T> back() {
		return this;
	}
}
