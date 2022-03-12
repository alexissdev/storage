package net.cosmogrp.storage.bukkit.codec;

import net.cosmogrp.storage.codec.ModelWriter;
import net.cosmogrp.storage.codec.AbstractModelWriter;
import net.cosmogrp.storage.model.Model;

import java.util.HashMap;
import java.util.Map;

public class YamlWriter extends AbstractModelWriter<Map<String, Object>> {

    private final Map<String, Object> values;

    private YamlWriter() {
        this.values = new HashMap<>();
    }

    public static ModelWriter<Map<String, Object>> create() {
        return new YamlWriter();
    }

    public static ModelWriter<Map<String, Object>> create(Model model) {
        return new YamlWriter()
                .write("id", model.getId());
    }

    @Override
    protected YamlWriter write0(String field, Object value) {
        values.put(field, value);
        return this;
    }

    @Override
    public Map<String, Object> end() {
        return values;
    }
}
