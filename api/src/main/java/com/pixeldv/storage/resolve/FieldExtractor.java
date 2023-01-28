package com.pixeldv.storage.resolve;

import com.pixeldv.storage.model.Model;

import java.util.function.Function;

public interface FieldExtractor<T extends Model>
	extends Function<T, String> {
}
