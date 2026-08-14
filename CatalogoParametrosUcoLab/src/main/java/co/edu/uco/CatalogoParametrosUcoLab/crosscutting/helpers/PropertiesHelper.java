package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

public final class PropertiesHelper {

    private PropertiesHelper() {
    }

    public static String getValue(final String propertiesFileName, final String key) {
        try (InputStream inputStream = PropertiesHelper.class.getClassLoader()
                .getResourceAsStream(propertiesFileName)) {

            if (inputStream == null) {
                throw TechnicalException.build("No fue posible encontrar el archivo de propiedades: "
                        + propertiesFileName);
            }

            var properties = new Properties();
            properties.load(inputStream);
            var value = properties.getProperty(key);

            if (TextHelper.isBlank(value)) {
                throw NotFoundException.build("No existe la llave '" + key + "' en el archivo "
                        + propertiesFileName);
            }

            return value;
        } catch (final NotFoundException | TechnicalException exception) {
            throw exception;
        } catch (final IOException exception) {
            throw new TechnicalException("No fue posible leer el archivo de propiedades: "
                    + propertiesFileName, exception);
        }
    }
}
