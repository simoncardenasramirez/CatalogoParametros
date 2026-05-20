package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

public final class TextHelper {

    public static final String EMPTY = "";

    private TextHelper() {
    }

    public static String applyTrim(final String value) {
        return value == null ? EMPTY : value.trim();
    }

    public static boolean isBlank(final String value) {
        return applyTrim(value).isEmpty();
    }
}
