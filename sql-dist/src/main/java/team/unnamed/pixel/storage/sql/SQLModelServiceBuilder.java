package team.unnamed.pixel.storage.sql;

import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.builder.LayoutModelServiceBuilder;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.sql.connection.SQLClient;
import team.unnamed.pixel.storage.sql.identity.MapSerializer;
import team.unnamed.pixel.storage.sql.identity.Table;
import org.jdbi.v3.core.mapper.RowMapper;
import team.unnamed.pixel.storage.util.Validate;

import static team.unnamed.pixel.storage.util.Validate.notNull;

public class SQLModelServiceBuilder<T extends Model & MapSerializer>
        extends LayoutModelServiceBuilder<T, SQLModelServiceBuilder<T>> {

    private SQLClient sqlClient;
    private RowMapper<T> rowMapper;
    private Table table;

    protected SQLModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public SQLModelServiceBuilder<T> client(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
        return this;
    }

    public SQLModelServiceBuilder<T> rowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    public SQLModelServiceBuilder<T> table(Table table) {
        this.table = table;
        return this;
    }

    @Override
    protected SQLModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        Validate.notNull(sqlClient, "sqlClient");
        Validate.notNull(rowMapper, "rowMapper");
        Validate.notNull(table, "table");

        if (cacheModelService == null) {
            return new SQLModelService<>(
                    executor, sqlClient,
                    rowMapper, table
            );
        } else {
            return new CachedSQLModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, sqlClient,
                    rowMapper, table
            );
        }
    }
}
