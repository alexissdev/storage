package net.cosmogrp.storage.sql.meta;

import net.cosmogrp.storage.model.AnnotationHelper;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.model.exception.NoSuchModelDataException;
import net.cosmogrp.storage.model.meta.ModelMeta;
import net.cosmogrp.storage.sql.identity.SQLElement;
import net.cosmogrp.storage.sql.identity.Table;
import net.cosmogrp.storage.sql.mysql.MySQLElement;
import net.cosmogrp.storage.sql.mysql.MySQLTable;

import java.util.ArrayList;
import java.util.List;

public class SQLModelMeta<T extends Model> extends ModelMeta<T> {

    private final Table table;

    public SQLModelMeta(Class<T> modelClass) throws NoSuchModelDataException {
        super(modelClass);

        SQLTable sqlTable = AnnotationHelper.getAnnotation(
                modelClass, SQLTable.class,
                true
        );

        List<SQLElement> elements = new ArrayList<>();

        for (SQLTable.Element element : sqlTable.elements()) {
            elements.add(new MySQLElement(
                    element.column(),
                    element.type(),
                    element.constraints()
            ));
        }

        table = new MySQLTable(sqlTable.name(), elements);
    }

    public Table getTable() {
        return table;
    }
}
