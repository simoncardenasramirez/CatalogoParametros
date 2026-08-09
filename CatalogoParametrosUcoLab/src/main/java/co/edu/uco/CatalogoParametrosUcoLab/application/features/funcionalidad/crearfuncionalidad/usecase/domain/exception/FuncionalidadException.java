package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.exception;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
public final class FuncionalidadException extends ValidationException {

    public FuncionalidadException(final String message) {
        super(message);
    }
}