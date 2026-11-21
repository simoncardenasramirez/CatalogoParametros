package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public class ForbiddenException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(final String message) {
        super(message);
    }

    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ForbiddenException build(final String message) {
        return new ForbiddenException(message);
    }
}
