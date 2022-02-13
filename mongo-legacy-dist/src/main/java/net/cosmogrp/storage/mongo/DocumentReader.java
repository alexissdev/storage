package net.cosmogrp.storage.mongo;

import org.bson.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class DocumentReader {

    private final Document document;

    private DocumentReader(Document document) {
        this.document = document;
    }

    public UUID readUuid(String field) {
        return UUID.fromString(readString(field));
    }

    public <T> Set<T> readSet(String field, Class<T> clazz) {
        return new HashSet<>(document.getList(field, clazz));
    }

    public Date readDate(String field) {
        return document.getDate(field);
    }

    public String readString(String field) {
        return document.getString(field);
    }

    public long readLong(String field) {
        return document.getLong(field);
    }

    public int readInt(String field) {
        return document.getInteger(field);
    }

    public boolean readBoolean(String field) {
        return document.getBoolean(field);
    }

    public <T> List<T> readByte(String field, Class<T> clazz) {
        return document.getList(field, clazz);
    }

    public Document readChild(String field) {
        return document.get(field, Document.class);
    }

    public <T extends DocumentCodec> Set<T> readChildren(
            String field,
            Function<DocumentReader, T> parser
    ) {
        Set<T> children = new HashSet<>();
        List<Document> documents = document.getList(field, Document.class);

        for (Document document : documents) {
            children.add(parser.apply(
                    DocumentReader.create(document)
            ));
        }

        return children;
    }

    public static DocumentReader create(Document document) {
        return new DocumentReader(document);
    }
}
