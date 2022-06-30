package team.unnamed.pixel.storage.mongo.codec;

import team.unnamed.pixel.storage.codec.ModelParser;
import team.unnamed.pixel.storage.model.Model;
import org.bson.Document;

public interface MongoModelParser<T extends Model>
        extends ModelParser<T, Document> {
}
