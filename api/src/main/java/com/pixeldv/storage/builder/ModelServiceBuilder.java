package com.pixeldv.storage.builder;

import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.resolve.ResolverRegistry;

import java.util.concurrent.Executor;

public interface ModelServiceBuilder<T extends Model> {

	ModelServiceBuilder<T> executor(Executor executor);

	ModelServiceBuilder<T> cachedService(ModelService<T> cachedService);

	ModelServiceBuilder<T> resolverRegistry(ResolverRegistry<T> resolverRegistry);

	ModelService<T> build();
}
