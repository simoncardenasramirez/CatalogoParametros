package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import co.edu.uco.seguridad.pep.starter.m2m.ServiceAccessTokenProvider;

/** Agrega el token tecnico solo a las llamadas dirigidas a MessageUcoLab. */
@Configuration
@ConditionalOnProperty(prefix = "security.pep.service-identity", name = "enabled", havingValue = "true")
@ConditionalOnBean(ServiceAccessTokenProvider.class)
public class MessageUcoLabSecurityConfiguration {

    @Bean
    @Primary
    WebClient.Builder messageUcoLabWebClientBuilder(
            final ServiceAccessTokenProvider tokenProvider,
            @Value("${messageucolab.url}") final String messageUcoLabUrl) {
        final URI messageUcoLabOrigin = URI.create(messageUcoLabUrl);

        return WebClient.builder().filter((request, next) -> {
            if (!sameOrigin(request.url(), messageUcoLabOrigin)) {
                return next.exchange(request);
            }

            return tokenProvider.token()
                    .map(token -> ClientRequest.from(request)
                            .headers(headers -> headers.setBearerAuth(token))
                            .build())
                    .flatMap(next::exchange);
        });
    }

    private boolean sameOrigin(final URI requestUri, final URI configuredUri) {
        return requestUri.getScheme().equalsIgnoreCase(configuredUri.getScheme())
                && requestUri.getHost().equalsIgnoreCase(configuredUri.getHost())
                && effectivePort(requestUri) == effectivePort(configuredUri);
    }

    private int effectivePort(final URI uri) {
        if (uri.getPort() > 0) {
            return uri.getPort();
        }
        return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
    }
}
