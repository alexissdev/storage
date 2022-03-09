package net.cosmogrp.storage.mongo.codec;

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

/**
 * It reads a document and converts it into a Java object
 */
public class DocumentReader {

    private final Document document;

    private DocumentReader(Document document) {
        this.document = document;
    }

    /**
     * Reads a String from the current row
     * and converts it to a UUID
     *
     * @param field The name of the field to read.
     * @return Nothing.
     */
    public UUID readUuid(String field) {
        return UUID.fromString(readString(field));
    }

    /**
     * It reads a list of objects from the
     * document and returns them as a set
     *
     * @param field The field name in the document.
     * @param clazz The class of the type of the
     *              elements of the set.
     * @return A Set of the given class.
     */
    public <T> Set<T> readSet(String field, Class<T> clazz) {
        return new HashSet<>(document.getList(field, clazz));
    }

    /**
     * Reads a date from the document
     *
     * @param field The field name to read.
     * @return Nothing.
     */
    public Date readDate(String field) {
        return document.getDate(field);
    }

    /**
     * Reads a string from the document
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    public String readString(String field) {
        return document.getString(field);
    }

    /**
     * Reads a double from the document
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    public double readDouble(String field) {
        return document.getDouble(field);
    }

    /**
     * Reads a field from the document and
     * returns it as a float
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    public float readFloat(String field) {
        Double value = document.getDouble(field);

        if (value == null) {
            return 0;
        }

        return (float) ((double) value);
    }

    /**
     * Reads a long from the document
     *
     * @param field The field to read from.
     * @return The long value of the field.
     */
    public long readLong(String field) {
        return document.getLong(field);
    }

    /**
     * Reads the value of the field from the
     * document and returns it as an integer
     *
     * @param field The field to read.
     * @return The integer value of the field.
     */
    public int readInt(String field) {
        return document.getInteger(field);
    }

    /**
     * Reads a boolean value from the document
     *
     * @param field The field to read.
     * @return The boolean value of the field.
     */
    public boolean readBoolean(String field) {
        return document.getBoolean(field);
    }

    /**
     * Read a list of objects from a field in the document
     *
     * @param field The field name to read from the document.
     * @param clazz The class of the type you want to read.
     * @return A List of the specified type.
     */
    public <T> List<T> readList(String field, Class<T> clazz) {
        return document.getList(field, clazz);
    }

    /**
     * If the document has a field with the given name,
     * parse it using the given parser
     *
     * @param field  The name of the field to read.
     * @param parser A function that takes a DocumentReader
     *               and returns a DocumentCodec.
     * @return The result of applying the parser to the child document.
     */
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

    /**
     * It reads a list of documents from the given field, and
     * parses each document into a key-value pair using the given
     * key and value parsers
     *
     * @param field       The name of the field to read.
     * @param keyParser   A function that takes a DocumentCodec
     *                    and returns a key.
     * @param valueParser A function that takes a DocumentReader
     *                    and returns a value of type V.
     * @return A map of key-value pairs.
     */
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

    /**
     * Reads a list of documents from a field in the current document
     *
     * @param field  The name of the field in the document
     *               that contains the list of children.
     * @param parser A function that takes a DocumentReader
     *               and returns a DocumentCodec.
     * @return A set of DocumentCodec objects.
     */
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
