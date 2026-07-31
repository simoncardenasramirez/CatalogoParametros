package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.exception;

public final class OrganizacionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrganizacionException(final String message) {
        super(message);
    }
}