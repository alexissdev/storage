package team.unnamed.pixel.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.builder.LayoutModelServiceBuilder;
import team.unnamed.pixel.storage.dist.DelegatedCachedModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.mongo.codec.DocumentCodec;
import team.unnamed.pixel.storage.mongo.codec.MongoModelParser;
import org.bson.Document;
import team.unnamed.pixel.storage.util.Validate;

import static team.unnamed.pixel.storage.util.Validate.notNull;

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
