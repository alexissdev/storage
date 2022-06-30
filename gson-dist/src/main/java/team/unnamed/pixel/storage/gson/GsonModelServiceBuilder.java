package team.unnamed.pixel.storage.gson;

import com.google.gson.Gson;
import team.unnamed.pixel.storage.ModelService;
import team.unnamed.pixel.storage.builder.LayoutModelServiceBuilder;
import team.unnamed.pixel.storage.dist.DelegatedCachedModelService;
import team.unnamed.pixel.storage.model.Model;
import team.unnamed.pixel.storage.util.Validate;

import java.io.File;

public class GsonModelServiceBuilder<T extends Model>
        extends LayoutModelServiceBuilder<T, GsonModelServiceBuilder<T>> {

    private Gson gson;
    private File folder;

    protected GsonModelServiceBuilder(Class<T> type) {
        super(type);
    }

    public GsonModelServiceBuilder<T> gson(Gson gson) {
        this.gson = gson;
        return back();
    }

    public GsonModelServiceBuilder<T> folder(File folder) {
        this.folder = folder;
        return back();
    }

    @Override
    protected GsonModelServiceBuilder<T> back() {
        return this;
    }

    @Override
    public ModelService<T> build() {
        check();
        Validate.notNull(gson, "gson");
        Validate.notNull(folder, "folder");

        ModelService<T> modelService = new GsonModelService<>(
                executor, gson,
                type, folder
        );

        if (cacheModelService == null) {
            return modelService;
        } else {
            return new DelegatedCachedModelService<>(
                    executor, cacheModelService,
                    resolverRegistry, modelService
            );
        }
    }
}
