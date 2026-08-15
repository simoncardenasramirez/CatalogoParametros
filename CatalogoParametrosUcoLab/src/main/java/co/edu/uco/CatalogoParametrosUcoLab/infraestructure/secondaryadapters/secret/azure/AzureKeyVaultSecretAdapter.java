package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.secret.azure;

import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.security.keyvault.secrets.SecretClient;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.secret.SecretVaultPort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import org.springframework.stereotype.Component;

@Component
public class AzureKeyVaultSecretAdapter implements SecretVaultPort {

    private final SecretClient secretClient;
    private final ConsultarMensajePort consultarMensajePort;

    public AzureKeyVaultSecretAdapter(final SecretClient secretClient,
            final ConsultarMensajePort consultarMensajePort) {
        this.secretClient = secretClient;
        this.consultarMensajePort = consultarMensajePort;
    }

    @Override
    public void crearSecreto(final String nombre, final String valor) {
        try {
            secretClient.setSecret(nombre, valor);
        } catch (final HttpResponseException exception) {
            throw new TechnicalException(consultarMensajePort.consultarMensaje("MSG-140"), exception);
        }
    }

    @Override
    public String consultarSecreto(final String nombre) {
        try {
            return secretClient.getSecret(nombre).getValue();
        } catch (final ResourceNotFoundException exception) {
            throw new NotFoundException(consultarMensajePort.consultarMensaje("MSG-141"), exception);
        } catch (final HttpResponseException exception) {
            throw new TechnicalException(consultarMensajePort.consultarMensaje("MSG-142"), exception);
        }
    }
}
