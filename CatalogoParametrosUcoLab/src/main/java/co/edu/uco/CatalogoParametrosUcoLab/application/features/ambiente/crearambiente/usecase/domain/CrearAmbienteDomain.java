package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearAmbienteDomain extends Domain {
    private String nombre;

    private CrearAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static CrearAmbienteDomain create(final UUID id, final String nombre) {
        return new CrearAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
