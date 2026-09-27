package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class CrearEstadoMetadatoAmbienteDomain extends Domain {
    private String nombre;

    private CrearEstadoMetadatoAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static CrearEstadoMetadatoAmbienteDomain create(final UUID id, final String nombre) {
        return new CrearEstadoMetadatoAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
