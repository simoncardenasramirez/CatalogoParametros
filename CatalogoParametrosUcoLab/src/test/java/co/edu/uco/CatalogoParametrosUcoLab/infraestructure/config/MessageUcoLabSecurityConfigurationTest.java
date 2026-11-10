package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.sun.net.httpserver.HttpServer;

import co.edu.uco.seguridad.pep.starter.m2m.ServiceAccessTokenProvider;
import reactor.core.publisher.Mono;

class MessageUcoLabSecurityConfigurationTest {

    private HttpServer server;
    private final AtomicReference<String> authorization = new AtomicReference<>();

    @BeforeEach
    void iniciarServidor() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
            final byte[] body = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
    }

    @AfterEach
    void detenerServidor() {
        server.stop(0);
    }

    @Test
    void debeEnviarBearerTecnicoCuandoConsultaMessageUcoLab() {
        final ServiceAccessTokenProvider provider = mock(ServiceAccessTokenProvider.class);
        when(provider.token()).thenReturn(Mono.just("technical-token"));

        final WebClient.Builder builder = new MessageUcoLabSecurityConfiguration()
                .messageUcoLabWebClientBuilder(provider,
                        "http://127.0.0.1:" + server.getAddress().getPort());

        final String response = builder.build().get()
                .uri("http://127.0.0.1:" + server.getAddress().getPort() + "/messageucolab/v1/public/messages/MSG_100")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertEquals("ok", response);
        assertEquals("Bearer technical-token", authorization.get());
    }
}
