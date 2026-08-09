package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.exception;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
public final class OrganizacionException extends ValidationException {
    private static final long serialVersionUID = 1L;

    public OrganizacionException(final String message) {
        super(message);
    }
}