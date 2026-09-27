package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarEstadoAmbienteDomain extends Domain {
    private String nombre;

    private ActualizarEstadoAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static ActualizarEstadoAmbienteDomain create(final UUID id, final String nombre) {
        return new ActualizarEstadoAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
