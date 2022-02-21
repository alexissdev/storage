package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface ModelService<T extends Model> {

    /**
     * Find a sync by id
     *
     * @param id The id of the object to find.
     * @return The method returns a nullable object of type T.
     */
    @Nullable T findSync(String id);

    /**
     * Get the value of a sync variable
     *
     * @param id The id of the object to get.
     * @return The method returns a nullable object of type T.
     */
    @Nullable T getSync(String id);

    /**
     * Get the object with the given id from the cache,
     * or if it's not there, find it in the database
     * and put it in the cache if possible
     *
     * @param id The id of the object to get or find.
     * @return The object that was found or created.
     */
    @Nullable T getOrFindSync(String id);

    /**
     * Get all the items in the list
     *
     * @return A List of T
     */
    List<T> getAllSync();

    /**
     * Return a list of all the elements in the collection
     *
     * @return Nothing
     */
    List<T> findAllSync();

    /**
     * Save the model to the database and cache
     * if it's not already there.
     *
     * @param model The model that will be saved.
     */
    void saveSync(T model);

    /**
     * Uploads the model to the server
     *
     * @param model The model to be uploaded.
     */
    void uploadSync(T model);

    /**
     * Upload all the files in the current directory
     * to the remote server
     *
     * @param preUploadAction a function that takes a single
     *                        parameter, which is the file to be uploaded.
     */
    void uploadAllSync(Consumer<T> preUploadAction);

    /**
     * DeleteSync deletes the model from the database
     *
     * @param model The model to be deleted.
     */
    void deleteSync(T model);

    /**
     * DeleteSync deletes the object with the given id
     *
     * @param id The id of the sync to delete.
     * @return The return type is void.
     */
    T deleteSync(String id);

}
