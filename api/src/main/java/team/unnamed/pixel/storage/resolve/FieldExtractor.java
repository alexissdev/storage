package team.unnamed.pixel.storage.resolve;

import team.unnamed.pixel.storage.model.Model;

import java.util.function.Function;

public interface FieldExtractor<T extends Model>
        extends Function<T, String> {
}
