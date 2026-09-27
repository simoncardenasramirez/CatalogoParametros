package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearEstadoAmbienteDomain extends Domain {
    private String nombre;

    private CrearEstadoAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static CrearEstadoAmbienteDomain create(final UUID id, final String nombre) {
        return new CrearEstadoAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
