package net.cosmogrp.storage.codec;

import java.util.UUID;

public abstract class PrimitiveModelWriter<R>
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

    protected abstract ModelWriter<R> write0(String field, Object value);
}
