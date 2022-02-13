package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.model.Model;

public interface MongoModelParser<T extends Model> {

    T parse(DocumentReader reader);

}
