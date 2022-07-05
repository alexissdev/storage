package team.unnamed.pixel.storage.bukkit;

import team.unnamed.pixel.storage.bukkit.codec.YamlCodec;
import team.unnamed.pixel.storage.bukkit.codec.YamlModelParser;
import team.unnamed.pixel.storage.bukkit.codec.YamlReader;
import team.unnamed.pixel.storage.dist.RemoteModelService;
import team.unnamed.pixel.storage.model.Model;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import team.unnamed.pixel.storage.ModelService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class YamlModelService<T extends Model & YamlCodec>
        extends RemoteModelService<T> {

    private final File folder;
    private final YamlModelParser<T> modelParser;

    protected YamlModelService(
            Executor executor, File folder,
            YamlModelParser<T> modelParser
    ) {
        super(executor);
        this.folder = folder;
        this.modelParser = modelParser;
    }

    public static <T extends Model & YamlCodec>
    YamlModelServiceBuilder<T> builder(Class<T> type) {
        return new YamlModelServiceBuilder<>(type);
    }

    @Override
    public @Nullable T findSync(String id) {
        File file = createFile(id, false);

        if (!file.exists()) {
            return null;
        }

        return parse(file);
    }

    @Override
    public List<T> findSync(String field, String value) {
        if (field.equals(ModelService.ID_FIELD)) {
            return Collections.singletonList(findSync(value));
        }

        return Collections.emptyList();
    }

    @Override
    public List<T> findAllSync(Consumer<T> postLoadAction) {
        File[] files = folder.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        List<T> models = new ArrayList<>(files.length);

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            T model = parse(file);

            postLoadAction.accept(model);
            models.add(model);
        }

        return models;
    }

    private File createFile(String id, boolean create) {
        File file = new File(folder, id + ".yml");

        if (create && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    private T parse(File file) {
        YamlConfiguration configuration = YamlConfiguration
                .loadConfiguration(file);

        return modelParser.parse(YamlReader.create(configuration));
    }

    @Override
    public void saveSync(T model) {
        File file = createFile(model.getId(), true);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.addDefaults(model.serialize());
        configuration.options().copyDefaults(true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not save model", e);
        }
    }

    @Override
    public void deleteSync(T model) {
        File file = createFile(model.getId(), false);

        if (file.exists()) {
            file.delete();
        }
    }
}
