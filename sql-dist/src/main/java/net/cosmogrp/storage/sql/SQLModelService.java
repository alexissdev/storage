package net.cosmogrp.storage.sql;

import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.model.Model;
import net.cosmogrp.storage.resolve.ResolverRegistry;
import net.cosmogrp.storage.sql.connection.SQLClient;
import net.cosmogrp.storage.sql.identity.SQLMapSerializer;
import net.cosmogrp.storage.sql.identity.Table;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class SQLModelService<T extends Model>
        extends CachedRemoteModelService<T> {

    private final Jdbi connection;
    private final RowMapper<T> rowMapper;
    private final SQLMapSerializer<T> mapSerializer;
    private final Table table;

    public SQLModelService(
            Executor executor,
            ModelService<T> cacheModelService,
            ResolverRegistry<T> resolverRegistry,
            SQLClient sqlClient,
            RowMapper<T> rowMapper,
            SQLMapSerializer<T> mapSerializer,
            Table table
    ) {
        super(executor, cacheModelService, resolverRegistry);
        this.connection = sqlClient.getConnection();
        this.rowMapper = rowMapper;
        this.mapSerializer = mapSerializer;
        this.table = table;

        try (Handle handle = connection.open()) {
            handle.execute(
                    "CREATE TABLE IF NOT EXISTS "
                            + table.getName() +
                            " (" + table.getDeclaration() + ")"
            );
        }
    }

    @Override
    protected void internalSave(T model) {
        try (Handle handle = connection.open()) {
            handle.createUpdate("REPLACE INTO <TABLE> (<COLUMNS>) VALUES (<VALUES>)")
                    .define("TABLE", table.getName())
                    .define("COLUMNS", table.getColumns())
                    .define("VALUES", table.getParameters())
                    .bindMap(mapSerializer.serialize(model))
                    .execute();
        }
    }

    @Override
    protected void internalDelete(T model) {
        try (Handle handle = connection.open()) {
            handle.createUpdate("DELETE FROM <TABLE> WHERE <COLUMN> = :n")
                    .define("TABLE", table.getName())
                    .define("COLUMN", table.getPrimaryColumn())
                    .bind("n", model.getId())
                    .execute();
        }
    }

    @Override
    protected @Nullable T internalFind(String id) {
        List<T> models = findSync(table.getPrimaryColumn(), id);

        if (models.isEmpty()) {
            return null;
        }

        return models.get(0);
    }

    @Override
    protected List<T> internalFindAll() {
        try (Handle handle = connection.open()) {
            List<T> models = new ArrayList<>();

            for (T model : handle.select("SELECT * FROM <TABLE>")
                    .define("TABLE", table.getName())
                    .map(rowMapper)
            ) {
                models.add(model);
            }

            return models;
        }
    }

    @Override
    public List<T> findSync(String field, String value) {
        try (Handle handle = connection.open()) {
            return handle.select("SELECT * FROM <TABLE> WHERE <COLUMN> = :n")
                    .define("TABLE", table.getName())
                    .define("COLUMN", field)
                    .bind("n", value)
                    .map(rowMapper)
                    .collect(Collectors.toList());
        }
    }
}
