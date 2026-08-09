package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public class ValidationException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ValidationException(final String message) {
        super(message);
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ValidationException build(final String message) {
        return new ValidationException(message);
    }
}
