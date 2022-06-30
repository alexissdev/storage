package team.unnamed.pixel.storage.bukkit.codec;

import team.unnamed.pixel.storage.codec.ModelParser;
import team.unnamed.pixel.storage.model.Model;

import java.util.Map;

public interface YamlModelParser<T extends Model>
        extends ModelParser<T, Map<String, Object>> {

}
