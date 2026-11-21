package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configura el cliente Pulsar sin exponer detalles del broker al núcleo. */
@Configuration
@ConditionalOnProperty(prefix = "pulsar", name = "enabled", havingValue = "true")
public class PulsarConfig {

    @Bean(destroyMethod = "close")
    PulsarClient pulsarClient(@Value("${pulsar.service-url}") final String serviceUrl)
            throws PulsarClientException {
        return PulsarClient.builder().serviceUrl(serviceUrl).build();
    }
}
