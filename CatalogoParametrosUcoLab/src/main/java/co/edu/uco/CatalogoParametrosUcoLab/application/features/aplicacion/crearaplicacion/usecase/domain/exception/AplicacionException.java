package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
public final class AplicacionException extends ValidationException {

    public AplicacionException(final String message) {
        super(message);
    }
}
