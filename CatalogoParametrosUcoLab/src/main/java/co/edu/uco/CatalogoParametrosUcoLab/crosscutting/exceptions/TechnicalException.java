package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

public class TechnicalException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public TechnicalException(final String message) {
        super(message);
    }

    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static TechnicalException build(final String message) {
        return new TechnicalException(message);
    }
}
