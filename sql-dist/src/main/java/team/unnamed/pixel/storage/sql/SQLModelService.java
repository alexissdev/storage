package team.unnamed.pixel.storage.sql;

import team.unnamed.pixel.storage.dist.RemoteModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.sql.connection.SQLClient;
import team.unnamed.pixel.storage.sql.identity.MapSerializer;
import team.unnamed.pixel.storage.sql.identity.Table;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SQLModelService<T extends Model & MapSerializer>
        extends RemoteModelService<T> {

    private final Jdbi connection;
    private final RowMapper<T> rowMapper;
    private final Table table;

    protected SQLModelService(
            Executor executor,
            SQLClient sqlClient,
            RowMapper<T> rowMapper,
            Table table
    ) {
        super(executor);
        this.connection = sqlClient.getConnection();
        this.rowMapper = rowMapper;
        this.table = table;

        try (Handle handle = connection.open()) {
            handle.execute(
                    "CREATE TABLE IF NOT EXISTS "
                            + table.getName() +
                            " (" + table.getDeclaration() + ")"
            );
        }
    }

    public static <T extends Model & MapSerializer>
    SQLModelServiceBuilder<T> builder(Class<T> type) {
        return new SQLModelServiceBuilder<>(type);
    }

    @Override
    public @Nullable T findSync(String id) {
        List<T> models = findSync(table.getPrimaryColumn(), id);

        if (models.isEmpty()) {
            return null;
        }

        return models.get(0);
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

    @Override
    public List<T> findAllSync(Consumer<T> postLoadAction) {
        try (Handle handle = connection.open()) {
            List<T> models = new ArrayList<>();

            for (T model : handle.select("SELECT * FROM <TABLE>")
                    .define("TABLE", table.getName())
                    .map(rowMapper)
            ) {
                postLoadAction.accept(model);
                models.add(model);
            }

            return models;
        }
    }

    @Override
    public void saveSync(T model) {
        try (Handle handle = connection.open()) {
            handle.createUpdate("REPLACE INTO <TABLE> (<COLUMNS>) VALUES (<VALUES>)")
                    .define("TABLE", table.getName())
                    .define("COLUMNS", table.getColumns())
                    .define("VALUES", table.getParameters())
                    .bindMap(model.toMap())
                    .execute();
        }
    }

    @Override
    public void deleteSync(T model) {
        try (Handle handle = connection.open()) {
            handle.createUpdate("DELETE FROM <TABLE> WHERE <COLUMN> = :n")
                    .define("TABLE", table.getName())
                    .define("COLUMN", table.getPrimaryColumn())
                    .bind("n", model.getId())
                    .execute();
        }
    }
}
