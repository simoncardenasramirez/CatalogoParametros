package co.edu.uco.CatalogoParametrosUcoLab.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public abstract class Domain {

    private UUID id;

    protected Domain(final UUID id) {
        setId(id);
    }

    public final UUID getId() {
        return id;
    }

    protected final void setId(final UUID id) {
        this.id = UUIDHelper.getDefault(id);
    }

    public final void generateId() {
        this.id = UUIDHelper.generate();
    }
}
