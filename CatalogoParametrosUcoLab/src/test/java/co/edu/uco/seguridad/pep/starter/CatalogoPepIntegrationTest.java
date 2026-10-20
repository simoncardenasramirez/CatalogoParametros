package co.edu.uco.seguridad.pep.starter;

import static org.junit.jupiter.api.Assertions.*;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.WebConfig;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

// Verifica el contrato del starter instalado sin depender de cuentas o servicios reales.
class CatalogoPepIntegrationTest {
    private static final String PATH = "/catalogo-parametros/api/v1/parametros";

    private List<String> publicPaths() {
        var config = new java.util.Properties();
        try (var stream = getClass().getResourceAsStream("/application.properties")) {
            config.load(stream);
        } catch (java.io.IOException error) {
            throw new java.io.UncheckedIOException(error);
        }
        return List.of(config.getProperty("security.pep.enforcement.public-paths").split(","));
    }

    @Test void consultaPorTokenUsaSuAutenticacionPropiaSinConsultarPep() {
        verify("/catalogo-parametros/api/v1/tokens/65208975-5350-45ce-81e4-2e54abfff853/parametros", false,
                request -> { fail("El Bearer del catalogo no debe enviarse a PEP"); return Mono.empty(); },
                HttpStatus.OK, true);
    }

    @Test void administracionDeTokensSigueProtegida() {
        verify("/catalogo-parametros/api/v1/tokens", false,
                request -> { fail("Sin sesion no debe consultar PEP"); return Mono.empty(); },
                HttpStatus.UNAUTHORIZED, false);
    }

    private void verify(String path, boolean session, ExchangeFunction decision,
                        HttpStatus expected, boolean expectedBusiness) {
        var properties = new PepEnforcementProperties(true, URI.create("http://pep.example"),
                "Catalogo", "local", "test-only-credential", publicPaths(), true);
        var pep = new PepEnforcementWebFilter(properties, WebClient.builder()
                .baseUrl("http://pep.example").exchangeFunction(decision).build());
        var request = MockServerHttpRequest.get("http://localhost:8080" + path)
                .header(HttpHeaders.ORIGIN, "http://localhost:4200");
        if (session) request.header(HttpHeaders.COOKIE, "SECURITY_BASELINE_SESSION=test-only-session");
        var exchange = MockServerWebExchange.from(request);
        var invoked = new AtomicBoolean();
        new WebConfig().corsWebFilter("http://localhost:4200").filter(exchange,
                corsExchange -> pep.filter(corsExchange, allowed -> {
                    invoked.set(true);
                    allowed.getResponse().setStatusCode(HttpStatus.OK);
                    return allowed.getResponse().setComplete();
                })).block(Duration.ofSeconds(5));
        assertEquals(expected, exchange.getResponse().getStatusCode());
        assertEquals(expectedBusiness, invoked.get());
        assertEquals("http://localhost:4200", exchange.getResponse().getHeaders().getAccessControlAllowOrigin());
    }

    @Test void sinSesionNoConsultaPepNiEjecutaNegocio() {
        verify(PATH, false, request -> { fail("No debe consultar PEP sin sesión"); return Mono.empty(); },
                HttpStatus.UNAUTHORIZED, false);
    }

    @Test void denegacionNoEjecutaNegocio() {
        verify(PATH, true, request -> Mono.just(ClientResponse.create(HttpStatus.FORBIDDEN).build()),
                HttpStatus.FORBIDDEN, false);
    }

    @Test void permisoPermiteNegocioYReenviaSesion() {
        verify(PATH, true, request -> {
            assertEquals("/internal/v1/embedded-access-decisions", request.url().getPath());
            assertEquals("SECURITY_BASELINE_SESSION=test-only-session", request.headers().getFirst(HttpHeaders.COOKIE));
            assertEquals("test-only-credential", request.headers().getFirst("X-Application-Credential"));
            return Mono.just(ClientResponse.create(HttpStatus.OK).build());
        }, HttpStatus.OK, true);
    }

    @Test void falloDePepNoEjecutaNegocio() {
        verify(PATH, true, request -> Mono.error(new IllegalStateException("PEP unavailable")),
                HttpStatus.SERVICE_UNAVAILABLE, false);
    }

    @Test void saludEsPublica() {
        verify("/actuator/health", false, request -> { fail("Salud no debe consultar PEP"); return Mono.empty(); },
                HttpStatus.OK, true);
    }
}
