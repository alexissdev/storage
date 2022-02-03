package net.cosmogrp.storage.model;

public abstract class AbstractModel
        implements Model {

    private final String _id;
    private boolean deleted;

    public AbstractModel(String id) {
        this._id = id;
    }

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void delete() {
        this.deleted = true;
    }
}
