package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.exception;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
public final class OrganizacionException extends ValidationException {

    public OrganizacionException(final String message) {
        super(message);
    }
}
