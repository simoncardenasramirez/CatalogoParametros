package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
public final class ModuloException extends ValidationException {

    public ModuloException(final String message) {
        super(message);
    }
}