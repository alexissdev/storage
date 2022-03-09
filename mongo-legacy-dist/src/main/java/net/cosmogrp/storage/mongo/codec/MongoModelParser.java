package net.cosmogrp.storage.mongo.codec;

import net.cosmogrp.storage.model.Model;

public interface MongoModelParser<T extends Model> {

    T parse(DocumentReader reader);

}
