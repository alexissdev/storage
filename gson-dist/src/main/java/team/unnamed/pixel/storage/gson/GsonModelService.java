package team.unnamed.pixel.storage.gson;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.dist.RemoteModelService;
import team.unnamed.pixel.storage.model.Model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class GsonModelService<T extends Model>
        extends RemoteModelService<T> {

    public static <T extends Model> GsonModelServiceBuilder<T> builder(Class<T> type) {
        return new GsonModelServiceBuilder<>(type);
    }

    private final Gson gson;
    private final Class<T> type;
    private final File folder;

    protected GsonModelService(
            Executor executor, Gson gson,
            Class<T> type, File folder
    ) {
        super(executor);
        this.gson = gson;
        this.type = type;
        this.folder = folder;
    }

    @Override
    public @Nullable T findSync(String id) {
        return internalFind(getFile(id));
    }

    @Override
    public List<T> findSync(String field, String value) {
        if (!field.equals(ModelService.ID_FIELD)) {
            throw new IllegalArgumentException(
                    "Only ID field is supported for sync find"
            );
        }

        return Collections.singletonList(findSync(value));
    }

    @Override
    public List<T> findAllSync() {
        File[] listFiles = folder.listFiles();

        if (listFiles == null) {
            return Collections.emptyList();
        }

        List<T> models = new ArrayList<>();

        for (File file : listFiles) {
            T model = internalFind(file);

            if (model == null) {
                continue;
            }

            models.add(model);
        }

        return models;
    }

    @Override
    public void saveSync(T model) {
        File file = getFile(model.getId());

        boolean write;

        if (!file.exists()) {
            try {
                write = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            write = true;
        }

        if (write) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(gson.toJson(model, type));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void deleteSync(T model) {
        getFile(model.getId()).delete();
    }

    private File getFile(String id) {
        return new File(folder, id + ".json");
    }

    private T internalFind(File file) {
        if (!file.exists()) {
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
