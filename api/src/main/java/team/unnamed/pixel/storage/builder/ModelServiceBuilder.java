package team.unnamed.pixel.storage.builder;

import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.resolve.ResolverRegistry;

import java.util.concurrent.Executor;

public interface ModelServiceBuilder<T extends Model> {

    ModelServiceBuilder<T> executor(Executor executor);

    ModelServiceBuilder<T> cachedService(ModelService<T> cachedService);

    ModelServiceBuilder<T> resolverRegistry(ResolverRegistry<T> resolverRegistry);

    ModelService<T> build();

}
