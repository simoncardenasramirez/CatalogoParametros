package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import java.util.UUID;

public final class UUIDHelper {

    private static final UUID DEFAULT = new UUID(0L, 0L);

    private UUIDHelper() {
    }

    public static UUID generate() {
        return UUID.randomUUID();
    }

    public static UUID getDefault() {
        return DEFAULT;
    }

    public static UUID getDefault(final UUID value) {
        return value == null ? DEFAULT : value;
    }
}
