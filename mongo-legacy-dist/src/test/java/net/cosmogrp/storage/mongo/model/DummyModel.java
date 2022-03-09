package net.cosmogrp.storage.mongo.model;

import net.cosmogrp.storage.model.AbstractModel;
import net.cosmogrp.storage.mongo.codec.DocumentBuilder;
import net.cosmogrp.storage.mongo.codec.DocumentCodec;
import net.cosmogrp.storage.mongo.codec.DocumentReader;
import org.bson.Document;

public class DummyModel extends AbstractModel
        implements DocumentCodec {

    private final String someValue;

    public DummyModel(String id, String someValue) {
        super(id);
        this.someValue = someValue;
    }

    public static DummyModel create(String id, String someValue) {
        return new DummyModel(id, someValue);
    }

    public static DummyModel fromDocument(DocumentReader reader) {
        return new DummyModel(
                reader.readString("_id"),
                reader.readString("someValue")
        );
    }

    @Override
    public String toString() {
        return "DummyModel{" +
                "someValue='" + someValue + '\'' +
                '}';
    }

    public String getSomeValue() {
        return someValue;
    }

    @Override
    public Document toDocument() {
        return DocumentBuilder.create(this)
                .write("someValue", someValue)
                .build();
    }
}
