package net.cosmogrp.storage.bukkit.codec;

import net.cosmogrp.storage.codec.ModelParser;
import net.cosmogrp.storage.model.Model;
import org.bukkit.configuration.ConfigurationSection;

public interface YamlModelParser<T extends Model>
        extends ModelParser<T, ConfigurationSection> {

}
