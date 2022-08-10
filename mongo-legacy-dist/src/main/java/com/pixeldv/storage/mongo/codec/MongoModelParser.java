package com.pixeldv.storage.mongo.codec;

import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.codec.ModelParser;
import org.bson.Document;

public interface MongoModelParser<T extends Model>
        extends ModelParser<T, Document> {
}
