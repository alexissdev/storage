package net.cosmogrp.storage.mongo.codec;

import net.cosmogrp.storage.codec.ModelCodec;
import net.cosmogrp.storage.codec.ModelWriter;
import net.cosmogrp.storage.codec.PrimitiveModelWriter;
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
public class DocumentWriter extends PrimitiveModelWriter<Document> {

    private final Document document;

    private DocumentWriter() {
        document = new Document();
    }

    public static DocumentWriter create() {
        return new DocumentWriter();
    }

    public static ModelWriter<Document> create(Model model) {
        return new DocumentWriter()
                .write("_id", model.getId());
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
    @Override
    protected DocumentWriter write0(String field, Object value) {
        document.append(field, value);
        return this;
    }

    @Override
    public Document end() {
        return document;
    }

}
