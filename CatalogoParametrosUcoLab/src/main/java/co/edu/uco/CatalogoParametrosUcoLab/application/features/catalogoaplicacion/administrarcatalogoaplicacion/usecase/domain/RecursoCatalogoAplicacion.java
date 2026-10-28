package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

public enum RecursoCatalogoAplicacion {
    MODULO("modulos"), FUNCIONALIDAD("funcionalidades"), PARAMETRO("parametros"),
    METADATO("metadatos"), METADATO_AMBIENTE("metadatos-ambiente");
    private final String ruta;
    RecursoCatalogoAplicacion(final String ruta) { this.ruta = ruta; }
    public static RecursoCatalogoAplicacion desde(final String ruta) {
        for (var recurso : values()) if (recurso.ruta.equals(ruta)) return recurso;
        throw ValidationException.build("El recurso no forma parte de la administracion embebida.");
    }
}
