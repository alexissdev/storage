package com.pixeldv.storage.bukkit.codec;

import com.pixeldv.storage.codec.ModelParser;
import com.pixeldv.storage.model.Model;

import java.util.Map;

public interface YamlModelParser<T extends Model>
		extends ModelParser<T, Map<String, Object>> {

}
