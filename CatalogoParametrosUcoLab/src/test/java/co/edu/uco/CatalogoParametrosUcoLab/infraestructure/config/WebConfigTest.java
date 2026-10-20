package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

class WebConfigTest {
    @Test
    void debePermitirPreflightConCredencialesSinEjecutarNegocio() {
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.options("http://localhost:8080/catalogo-parametros/api/v1/parametros")
                .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type,X-XSRF-TOKEN"));
        var invoked = new AtomicBoolean();
        new WebConfig().corsWebFilter("http://localhost:4200").filter(exchange, ignored -> {
            invoked.set(true);
            return Mono.empty();
        }).block();
        assertFalse(invoked.get());
        assertEquals("http://localhost:4200", exchange.getResponse().getHeaders().getAccessControlAllowOrigin());
        assertTrue(exchange.getResponse().getHeaders().getAccessControlAllowCredentials());
    }

    @Test
    void debeConservarCorsCuandoPepRechazaLaSolicitud() {
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:8080/catalogo-parametros/api/v1/parametros")
                .header(HttpHeaders.ORIGIN, "http://localhost:4200"));
        new WebConfig().corsWebFilter(" http://localhost:4200, http://localhost:5173 ")
                .filter(exchange, denied -> {
                    denied.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return denied.getResponse().setComplete();
                }).block();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertEquals("http://localhost:4200", exchange.getResponse().getHeaders().getAccessControlAllowOrigin());
    }

    @Test
    void debeRechazarOrigenNoAutorizado() {
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:8080/catalogo-parametros/api/v1/parametros")
                .header(HttpHeaders.ORIGIN, "http://otro.example"));
        var invoked = new AtomicBoolean();
        new WebConfig().corsWebFilter("http://localhost:4200").filter(exchange, ignored -> {
            invoked.set(true);
            return Mono.empty();
        }).block();
        assertFalse(invoked.get());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    void debeRechazarComodinConCredenciales() {
        assertThrows(IllegalArgumentException.class, () -> new WebConfig().corsWebFilter("*"));
    }
}
