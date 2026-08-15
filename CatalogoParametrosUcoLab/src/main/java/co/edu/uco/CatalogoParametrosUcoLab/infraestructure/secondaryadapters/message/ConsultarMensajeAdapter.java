package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.message;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper;

@Component
public class ConsultarMensajeAdapter implements ConsultarMensajePort {

    @Override
    public String consultarMensaje(final String codigo) {
        return PropertiesHelper.getValue(Constants.MESSAGE_PROPERTIES_FILE, codigo);
    }
}
