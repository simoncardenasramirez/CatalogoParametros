package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.message;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.uco.messageucolab.client.api.MessageReader;
import co.edu.uco.messageucolab.client.api.model.MessageDetails;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajeDetallePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeDetalle;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.PropertiesHelper;

@Component
public class ConsultarMensajeAdapter implements ConsultarMensajePort, ConsultarMensajeDetallePort {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    private final MessageReader messageReader;

    @Autowired
    public ConsultarMensajeAdapter(final MessageReader messageReader) {
        this.messageReader = messageReader;
    }

    /**
     * Constructor conservado para las pruebas unitarias del respaldo local.
     */
    ConsultarMensajeAdapter() {
        this.messageReader = null;
    }

    @Override
    public String consultarMensaje(final String codigo) {
        return consultarDetalle(codigo).content();
    }

    @Override
    public MensajeDetalle consultarDetalle(final String codigo) {
        if (messageReader != null) {
            try {
                final String codigoMessageUcoLab = codigo.replace('-', '_');
                final MessageDetails detalle = messageReader.getDetails(codigoMessageUcoLab)
                        .block(TIMEOUT);
                if (detalle != null && detalle.content() != null) {
                    return new MensajeDetalle(
                            detalle.code(),
                            detalle.title(),
                            detalle.content(),
                            detalle.type(),
                            detalle.category(),
                            detalle.functionality());
                }
            } catch (final RuntimeException ignored) {
                // MessageUcoLab no debe impedir que el catalogo responda sus validaciones.
            }
        }

        return new MensajeDetalle(
                codigo,
                codigo,
                consultarRespaldoLocal(codigo),
                null,
                null,
                null);
    }

    private String consultarRespaldoLocal(final String codigo) {
        return PropertiesHelper.getValue(Constants.MESSAGE_PROPERTIES_FILE, codigo);
    }
}
