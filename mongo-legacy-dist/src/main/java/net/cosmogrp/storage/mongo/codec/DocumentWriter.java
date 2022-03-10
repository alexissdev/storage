package net.cosmogrp.storage.mongo.codec;

import net.cosmogrp.storage.codec.ModelCodec;
import net.cosmogrp.storage.codec.ModelWriter;
import net.cosmogrp.storage.model.Model;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It's a builder for documents
 */
public class DocumentWriter implements ModelWriter<Document> {

    private final Document document;

    private DocumentWriter() {
        document = new Document();
    }

    public static DocumentWriter create() {
        return new DocumentWriter();
    }

    public static DocumentWriter create(Model model) {
        return new DocumentWriter()
                .write("_id", model.getId());
    }

    /**
     * Write a field with a UUID value
     *
     * @param field The field name to write to.
     * @param uuid  The UUID to write to the document.
     * @return Nothing.
     */
    @Override
    public DocumentWriter write(String field, UUID uuid) {
        document.append(field, uuid.toString());
        return this;
    }

    @Override
    public DocumentWriter write(String field, String value) {
        return write0(field, value);
    }

    @Override
    public DocumentWriter write(String field, int value) {
        return write0(field, value);
    }

    @Override
    public DocumentWriter write(String field, long value) {
        return write0(field, value);
    }

    @Override
    public DocumentWriter write(String field, double value) {
        return write0(field, value);
    }

    @Override
    public DocumentWriter write(String field, boolean value) {
        return write0(field, value);
    }

    @Override
    public DocumentWriter write(String field, ModelCodec<Document> child) {
        if (child == null) {
            document.append(field, null);
        } else {
            document.append(field, child.serialize());
        }

        return this;
    }

    @Override
    public DocumentWriter write(
            String field,
            Collection<? extends ModelCodec<Document>> children
    ) {
        List<Document> documents = new ArrayList<>(children.size());
        for (ModelCodec<Document> child : children) {
            documents.add(child.serialize());
        }

        document.append(field, documents);
        return this;
    }

    @Override
    public DocumentWriter write(
            String field,
            Map<?, ? extends ModelCodec<Document>> children
    ) {
        List<Document> documents = new ArrayList<>(children.size());
        for (ModelCodec<Document> child : children.values()) {
            documents.add(child.serialize());
        }

        document.append(field, documents);
        return this;
    }

    /**
     * It adds a field to the document.
     *
     * @param field The name of the field to be added to the document.
     * @param value The value to be written.
     * @return Nothing.
     */
    private DocumentWriter write0(String field, Object value) {
        document.append(field, value);
        return this;
    }

    @Override
    public Document end() {
        return document;
    }

}
