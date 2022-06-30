package team.unnamed.pixel.storage.model;

public abstract class AbstractModel
        implements Model {

    private final String id;

    public AbstractModel(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
