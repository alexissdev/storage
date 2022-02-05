package net.cosmogrp.storage.sql.identity;

import net.cosmogrp.storage.model.Model;

import java.util.Map;

public interface SQLMapSerializer<T extends Model> {

    Map<String, Object> serialize(T model);

}
