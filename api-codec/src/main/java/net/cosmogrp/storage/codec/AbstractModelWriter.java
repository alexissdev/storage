package net.cosmogrp.storage.codec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class AbstractModelWriter<R>
        implements ModelWriter<R> {

    @Override
    public ModelWriter<R> write(String field, UUID uuid) {
        return write(field, uuid.toString());
    }

    @Override
    public ModelWriter<R> write(String field, String value) {
        return write0(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, int value) {
        return write0(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, long value) {
        return write0(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, double value) {
        return write0(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, boolean value) {
        return write0(field, value);
    }

    @Override
    public ModelWriter<R> write(String field, ModelCodec<R> child) {
        if (child == null) {
            return write0(field, null);
        } else {
            return write0(field, child.serialize());
        }
    }

    @Override
    public ModelWriter<R> write(String field, Collection<? extends ModelCodec<R>> children) {
        List<R> documents = new ArrayList<>(children.size());
        for (ModelCodec<R> child : children) {
            documents.add(child.serialize());
        }

        return write0(field, documents);
    }

    protected abstract ModelWriter<R> write0(String field, Object value);
}
