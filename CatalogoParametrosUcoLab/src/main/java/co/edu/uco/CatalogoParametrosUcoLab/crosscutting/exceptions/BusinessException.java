package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public abstract class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected BusinessException(final String message) {
        super(message);
    }

    protected BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
