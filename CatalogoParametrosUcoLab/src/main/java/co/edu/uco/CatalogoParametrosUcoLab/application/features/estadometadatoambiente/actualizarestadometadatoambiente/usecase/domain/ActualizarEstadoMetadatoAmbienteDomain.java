package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ActualizarEstadoMetadatoAmbienteDomain extends Domain {
    private String nombre;

    private ActualizarEstadoMetadatoAmbienteDomain(final UUID id, final String nombre) {
        super(id);
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public static ActualizarEstadoMetadatoAmbienteDomain create(final UUID id, final String nombre) {
        return new ActualizarEstadoMetadatoAmbienteDomain(id, nombre);
    }

    public String getNombre() {
        return nombre;
    }
}
