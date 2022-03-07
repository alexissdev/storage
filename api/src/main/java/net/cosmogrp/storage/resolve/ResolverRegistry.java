package net.cosmogrp.storage.resolve;

import net.cosmogrp.storage.model.Model;

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

    public Iterable<String> resolve(String field, String value) {
        RelationalResolver<T> resolver = resolvers.get(field);
        if (resolver == null) {
            return null;
        }
        return resolver.resolve(value);
    }

}
