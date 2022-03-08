package net.cosmogrp.storage.resolve;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ResolverRegistry<T extends Model> {

    private final Map<String, RelationalResolver<T>> resolvers;

    public ResolverRegistry(
            Map<String, RelationalResolver<T>> resolvers
    ) {
        this.resolvers = resolvers;
    }

    public void bind(T model) {
        for (RelationalResolver<T> resolver : resolvers.values()) {
            resolver.bind(model);
        }
    }

    public void unbind(T model) {
        for (RelationalResolver<T> resolver : resolvers.values()) {
            resolver.unbind(model);
        }
    }

    public @Nullable RelationalResolver<T> getResolver(String field) {
        return resolvers.get(field);
    }

    public Iterable<String> resolve(String field, String value) {
        RelationalResolver<T> resolver = getResolver(field);
        if (resolver == null) {
            return null;
        }
        return resolver.resolve(value);
    }

    public static <T extends Model> ResolverRegistryBuilder<T> builder() {
        return new ResolverRegistryBuilder<>();
    }
}
