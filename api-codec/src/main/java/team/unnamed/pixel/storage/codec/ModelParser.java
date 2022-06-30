package team.unnamed.pixel.storage.codec;

import team.unnamed.pixel.storage.model.Model;

public interface ModelParser<T extends Model, R> {

    T parse(ModelReader<R> reader);

}
