package com.pixeldv.storage.codec;

import com.pixeldv.storage.model.Model;

public interface ModelParser<T extends Model, R> {

    T parse(ModelReader<R> reader);

}
