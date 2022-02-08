package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.model.AbstractModel;
import org.bson.Document;

public abstract class MongoModel extends AbstractModel {

    public MongoModel(String id) {
        super(id);
    }

    protected abstract Document toDocument();

}
