package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.model.Model;
import org.bson.Document;

public interface MongoModelParser<T extends Model> {

    T parse(Document document);

}
