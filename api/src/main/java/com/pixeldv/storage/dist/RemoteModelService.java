package com.pixeldv.storage.dist;

import com.pixeldv.storage.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public abstract class RemoteModelService<T extends Model>
	extends AsyncModelService<T> {

	public RemoteModelService(Executor executor) {
		super(executor);
	}

	@Override
	public T deleteSync(@NotNull String id) {
		T model = findSync(id);

		if (model != null) {
			deleteSync(model);
		}

		return model;
	}
}
