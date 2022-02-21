package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.model.Model;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It's a builder for documents
 */
public class DocumentBuilder {

    private final Document document;

    private DocumentBuilder() {
        document = new Document();
    }

    /**
     * Write a field with a UUID value
     *
     * @param field The field name to write to.
     * @param uuid The UUID to write to the document.
     * @return Nothing.
     */
    public DocumentBuilder write(String field, UUID uuid) {
        document.append(field, uuid.toString());
        return this;
    }

    /**
     * It adds a field to the document.
     *
     * @param field The name of the field to be added to the document.
     * @param value The value to be written.
     * @return Nothing.
     */
    public DocumentBuilder write(String field, Object value) {
        document.append(field, value);
        return this;
    }

    /**
     * Write a field to the document
     *
     * @param field The name of the field to write to.
     * @param child The child document.
     * @return Nothing.
     */
    public DocumentBuilder write(
            String field,
            @Nullable DocumentCodec child
    ) {
        if (child == null) {
            document.append(field, null);
        } else {
            document.append(field, child.toDocument());
        }

        return this;
    }

    /**
     * Write a field to the document with a list of children
     *
     * @param field The name of the field to write to.
     * @param children The collection of child documents.
     * @return Nothing.
     */
    public DocumentBuilder write(
            String field,
            Collection<? extends DocumentCodec> children
    ) {
        List<Document> documents = new ArrayList<>(children.size());
        for (DocumentCodec child : children) {
            documents.add(child.toDocument());
        }

        document.append(field, documents);
        return this;
    }

    /**
     * Write a list of documents to a field
     *
     * @param field The name of the field to write to.
     * @param children A map of child documents.
     * @return Nothing.
     */
    public DocumentBuilder write(
            String field,
            Map<?, ? extends DocumentCodec> children
    ) {
        List<Document> documents = new ArrayList<>(children.size());
        for (DocumentCodec child : children.values()) {
            documents.add(child.toDocument());
        }

        document.append(field, documents);
        return this;
    }

    public Document build() {
        return document;
    }

    public static DocumentBuilder create() {
        return new DocumentBuilder();
    }

    public static DocumentBuilder create(Model model) {
        return new DocumentBuilder()
                .write("_id", model.getId());
    }

}
