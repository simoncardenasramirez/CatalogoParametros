package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public class ConflictException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ConflictException(final String message) {
        super(message);
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ConflictException build(final String message) {
        return new ConflictException(message);
    }
}
