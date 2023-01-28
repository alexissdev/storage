package com.pixeldv.storage.bukkit;

import com.pixeldv.storage.ModelService;
import com.pixeldv.storage.builder.LayoutModelServiceBuilder;
import com.pixeldv.storage.bukkit.codec.YamlCodec;
import com.pixeldv.storage.bukkit.codec.YamlModelParser;
import com.pixeldv.storage.dist.DelegatedCachedModelService;
import com.pixeldv.storage.model.Model;
import com.pixeldv.storage.util.Validate;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class YamlModelServiceBuilder<T extends Model & YamlCodec>
	extends LayoutModelServiceBuilder<T, YamlModelServiceBuilder<T>> {

	private YamlModelParser<T> modelParser;
	private File folder;

	protected YamlModelServiceBuilder(Class<T> type) {
		super(type);
	}

	public YamlModelServiceBuilder<T> folder(File folder) {
		this.folder = folder;
		return this;
	}

	public YamlModelServiceBuilder<T> dataFolder(Plugin plugin, String child) {
		return folder(new File(plugin.getDataFolder(), child));
	}

	public YamlModelServiceBuilder<T> dataFolder(Plugin plugin) {
		return folder(plugin.getDataFolder());
	}

	public YamlModelServiceBuilder<T> modelParser(YamlModelParser<T> modelParser) {
		this.modelParser = modelParser;
		return this;
	}

	@Override
	protected YamlModelServiceBuilder<T> back() {
		return this;
	}

	@Override
	public ModelService<T> build() {
		check();
		Validate.notNull(modelParser, "modelParser");
		Validate.notNull(folder, "folder");

		if (!folder.exists()) {
			Validate.state(
				folder.mkdirs(),
				"Failed to create folder: "
				+ folder.getName());
		}

		YamlModelService<T> modelService =
			new YamlModelService<>(executor, folder, modelParser);

		if (cacheModelService == null) {
			return modelService;
		} else {
			return new DelegatedCachedModelService<>(
				executor, cacheModelService,
				resolverRegistry, modelService
			);
		}
	}
}
