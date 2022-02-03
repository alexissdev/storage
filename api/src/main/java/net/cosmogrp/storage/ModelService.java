package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface ModelService<T extends Model> {

    @Nullable T findSync(String id);

    @Nullable T getSync(String id);

    @Nullable T getOrFindSync(String id);

    List<T> getAllSync();

    List<T> findAllSync();

    void saveSync(T model);

    void uploadSync(T model);

    void uploadAllSync(Consumer<T> preUploadAction);

    void deleteSync(T model);

    T deleteSync(String id);

}
