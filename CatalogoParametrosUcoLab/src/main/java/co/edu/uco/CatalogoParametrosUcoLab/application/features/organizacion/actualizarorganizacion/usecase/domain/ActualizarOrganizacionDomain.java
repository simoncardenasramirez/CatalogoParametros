package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;

public final class ActualizarOrganizacionDomain extends Domain {

    private String nombre;

    private ActualizarOrganizacionDomain(final UUID id, final String nombre) {
        super(id);
        setNombre(nombre);
    }

    public static ActualizarOrganizacionDomain create(final UUID id, final String nombre) {
        return new ActualizarOrganizacionDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(final String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }
}
