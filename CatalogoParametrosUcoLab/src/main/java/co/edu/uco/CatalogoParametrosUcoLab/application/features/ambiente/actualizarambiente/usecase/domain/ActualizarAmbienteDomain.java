package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarAmbienteDomain extends Domain {
    private String nombre;

    private ActualizarAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static ActualizarAmbienteDomain create(final UUID id, final String nombre) {
        return new ActualizarAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
