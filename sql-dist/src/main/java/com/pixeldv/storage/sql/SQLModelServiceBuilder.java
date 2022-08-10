package com.pixeldv.storage.sql;

import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.builder.LayoutModelServiceBuilder;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.sql.identity.MapSerializer;
import com.pixeldv.storage.sql.connection.SQLClient;
import com.pixeldv.storage.sql.identity.Table;
import org.jdbi.v3.core.mapper.RowMapper;
import com.pixeldv.storage.util.Validate;

import static com.pixeldv.storage.util.Validate.notNull;

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
