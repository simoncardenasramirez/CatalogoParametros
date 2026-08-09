package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public class NotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static NotFoundException build(final String message) {
        return new NotFoundException(message);
    }
}
