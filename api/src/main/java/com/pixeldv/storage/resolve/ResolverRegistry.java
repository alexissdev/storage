package com.pixeldv.storage.resolve;

import com.pixeldv.storage.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class ResolverRegistry<T extends Model> {

	private final Map<String, RelationalResolver<T>> resolvers;

	protected ResolverRegistry(@NotNull Map<String, RelationalResolver<T>> resolvers) {
		this.resolvers = resolvers;
	}

	protected ResolverRegistry() {
		this.resolvers = Collections.emptyMap();
	}

	public void bind(@NotNull T model) {
		for (RelationalResolver<T> resolver : resolvers.values()) {
			resolver.bind(model);
		}
	}

	public void unbind(@NotNull T model) {
		for (RelationalResolver<T> resolver : resolvers.values()) {
			resolver.unbind(model);
		}
	}

	public @Nullable RelationalResolver<T> getResolver(@NotNull String field) {
		return resolvers.get(field);
	}

	public @Nullable Iterable<String> resolve(@NotNull String field, @NotNull String value) {
		RelationalResolver<T> resolver = getResolver(field);
		if (resolver == null) {
			return null;
		}
		return resolver.resolve(value);
	}

	@Contract(pure = true, value = " -> new")
	public static <T extends Model> @NotNull ResolverRegistryBuilder<T> builder() {
		return new ResolverRegistryBuilder<>();
	}

	@Contract(pure = true, value = " -> new")
	public static <T extends Model> @NotNull ResolverRegistry<T> empty() {
		return new ResolverRegistry<>();
	}
}
