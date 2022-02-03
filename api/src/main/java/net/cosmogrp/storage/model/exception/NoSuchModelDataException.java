package net.cosmogrp.storage.model.exception;

public class NoSuchModelDataException
        extends Exception {

    public NoSuchModelDataException(Class<?> modelClass, String dataKey) {
        super("Annotation '" + dataKey + "' " +
                "not found on model " + modelClass.getName());
    }

}
