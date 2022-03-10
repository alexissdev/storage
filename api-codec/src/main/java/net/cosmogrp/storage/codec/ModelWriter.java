package net.cosmogrp.storage.codec;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ModelWriter<R> {
    
    ModelWriter<R> write(String field, UUID uuid);
    
    ModelWriter<R> write(String field, String value);
    
    ModelWriter<R> write(String field, int value);
    
    ModelWriter<R> write(String field, long value);
    
    ModelWriter<R> write(String field, double value);
    
    ModelWriter<R> write(String field, boolean value);
    
    ModelWriter<R> write(
            String field, 
            ModelCodec<R> child
    );
    
    ModelWriter<R> write(
            String field, 
            Collection<? extends ModelCodec<R>> children
    );

    ModelWriter<R> write(
            String field,
            Map<?, ? extends ModelCodec<R>> children
    );

    R end();
    
}
