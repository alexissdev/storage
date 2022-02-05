package net.cosmogrp.storage.redis.meta;

import net.cosmogrp.storage.model.AnnotationHelper;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.exception.NoSuchModelDataException;
import net.cosmogrp.storage.model.meta.ModelMeta;

public class RedisModelMeta<T extends Model>
        extends ModelMeta<T> {

    private final String tableName;

    public RedisModelMeta(
            Class<T> type
    ) throws NoSuchModelDataException {
        super(type);

        this.tableName = AnnotationHelper.getAnnotation(
                type, RedisTable.class,
                true
        ).value();
    }

    public String getTableName() {
        return tableName;
    }
}
