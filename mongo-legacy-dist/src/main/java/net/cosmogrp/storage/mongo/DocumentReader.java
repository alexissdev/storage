package net.cosmogrp.storage.mongo;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public double readDouble(String field) {
        return document.getDouble(field);
    }

    public float readFloat(String field) {
        Double value = document.getDouble(field);

        if (value == null) {
            return 0;
        }

        return (float) ((double) value);
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

    public <T extends DocumentCodec> @Nullable T readChild(
            String field,
            Function<DocumentReader, T> parser
    ) {
        Document child = document.get(field, Document.class);

        if (child == null) {
            return null;
        }

        return parser.apply(DocumentReader.create(child));
    }

    public <K, V extends DocumentCodec> Map<K, V> readMap(
            String field,
            Function<V, K> keyParser,
            Function<DocumentReader, V> valueParser
    ) {
        List<Document> documents = document.getList(field, Document.class);
        Map<K, V> map = new HashMap<>(documents.size());

        for (Document document : documents) {
            V value = valueParser.apply(DocumentReader.create(document));

            map.put(keyParser.apply(value), value);
        }

        return map;
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
