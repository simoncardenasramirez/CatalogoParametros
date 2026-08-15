package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.secret.azure;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureKeyVaultConfig {

    @Bean
    public SecretClient secretClient(final AzureKeyVaultProperties properties) {
        return new SecretClientBuilder()
                .vaultUrl(properties.getEndpoint())
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }
}
