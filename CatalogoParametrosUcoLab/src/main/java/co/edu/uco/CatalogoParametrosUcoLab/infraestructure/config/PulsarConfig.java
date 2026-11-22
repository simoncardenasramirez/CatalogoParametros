package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** Configura el cliente Pulsar sin exponer detalles del broker al núcleo. */
@Configuration
public class PulsarConfig {

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "pulsar", name = "enabled", havingValue = "true")
    @Primary
    PulsarClient pulsarClient(@Value("${pulsar.service-url}") final String serviceUrl)
            throws PulsarClientException {
        return PulsarClient.builder().serviceUrl(serviceUrl).build();
    }

    /**
     * Cliente del broker propio de Catalogo de Parametros. Se mantiene separado del cliente usado
     * para consumir los avisos de MessageUcoLab, porque cada aplicacion puede tener su propio broker.
     */
    @Bean(name = "catalogoParametrosPulsarPublisherClient", destroyMethod = "close")
    @ConditionalOnProperty(prefix = "pulsar.publisher", name = "enabled", havingValue = "true")
    PulsarClient catalogoParametrosPulsarPublisherClient(
            @Value("${pulsar.publisher.service-url}") final String serviceUrl)
            throws PulsarClientException {
        return PulsarClient.builder().serviceUrl(serviceUrl).build();
    }
}
