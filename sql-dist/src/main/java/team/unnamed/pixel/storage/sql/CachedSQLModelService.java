package team.unnamed.pixel.storage.sql;

import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.dist.CachedRemoteModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.resolve.ResolverRegistry;
import team.unnamed.pixel.storage.sql.connection.SQLClient;
import team.unnamed.pixel.storage.sql.identity.MapSerializer;
import team.unnamed.pixel.storage.sql.identity.Table;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

public class CachedSQLModelService<T extends Model & MapSerializer>
        extends CachedRemoteModelService<T> {

    private final SQLModelService<T> delegate;

    protected CachedSQLModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            SQLClient client,
            RowMapper<T> mapper,
            Table table
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.delegate = new SQLModelService<>(
                executor, client,
                mapper, table
        );
    }

    @Override
    protected void internalSave(T model) {
        delegate.saveSync(model);
    }

    @Override
    protected void internalDelete(T model) {
        delegate.deleteSync(model);
    }

    @Override
    protected @Nullable T internalFind(String id) {
        return delegate.findSync(id);
    }

    @Override
    protected List<T> internalFindAll() {
        return delegate.findAllSync();
    }

    @Override
    public List<T> findSync(String field, String value) {
        return delegate.findSync(field, value);
    }
}
