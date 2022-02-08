package net.cosmogrp.storage.model;

public abstract class AbstractModel
        implements Model {

    private final String _id;

    public AbstractModel(String id) {
        this._id = id;
    }

    @Override
    public String getId() {
        return _id;
    }
}
