package net.cosmogrp.storage.mongo.meta;

import net.cosmogrp.storage.model.AnnotationHelper;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.exception.NoSuchModelDataException;
import net.cosmogrp.storage.model.meta.ModelMeta;

public class MongoModelMeta<T extends Model>
        extends ModelMeta<T> {

    private final String collectionName;

    public MongoModelMeta(Class<T> type) throws NoSuchModelDataException {
        super(type);

        this.collectionName = AnnotationHelper.getAnnotation(
                type, Collection.class, true
        ).value();
    }

    public String getCollectionName() {
        return collectionName;
    }
}
