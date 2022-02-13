package net.cosmogrp.storage.mongo;

import net.cosmogrp.storage.model.Model;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DocumentBuilder {

    private final Document document;

    private DocumentBuilder() {
        document = new Document();
    }

    public DocumentBuilder write(String field, UUID uuid) {
        document.append(field, uuid.toString());
        return this;
    }

    public DocumentBuilder write(String field, Object value) {
        document.append(field, value);
        return this;
    }

    public DocumentBuilder write(String field, DocumentCodec child) {
        document.append(field, child.toDocument());
        return this;
    }

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
