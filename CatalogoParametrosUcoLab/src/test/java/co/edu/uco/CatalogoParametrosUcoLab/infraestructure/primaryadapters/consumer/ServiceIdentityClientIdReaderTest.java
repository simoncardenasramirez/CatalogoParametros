package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

class ServiceIdentityClientIdReaderTest {
    private final ServiceIdentityClientIdReader reader = new ServiceIdentityClientIdReader();

    @Test
    void debeRetornarNuloCuandoNoHayBearer() {
        final var exchange = exchangeWithAuthorization(null);

        assertNull(reader.read(exchange));
    }

    @Test
    void debeRetornarElClientIdDelClaimAzp() {
        final var exchange = exchangeWithAuthorization(token("{\"azp\":\"client-azp\",\"client_id\":\"otro\"}"));

        assertEquals("client-azp", reader.read(exchange));
    }

    @Test
    void debeUsarClientIdCuandoNoExisteAzp() {
        final var exchange = exchangeWithAuthorization(token("{\"client_id\":\"client-fallback\"}"));

        assertEquals("client-fallback", reader.read(exchange));
    }

    @Test
    void debeRetornarNuloCuandoElTokenNoEsValido() {
        assertNull(reader.read(exchangeWithAuthorization("Bearer no-es-jwt")));
        assertNull(reader.read(exchangeWithAuthorization(token("no-json"))));
    }

    private ServerWebExchange exchangeWithAuthorization(final String value) {
        final var exchange = mock(ServerWebExchange.class);
        final var request = mock(ServerHttpRequest.class);
        final var headers = new HttpHeaders();
        if (value != null) headers.set(HttpHeaders.AUTHORIZATION, value);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);
        return exchange;
    }

    private String token(final String claims) {
        final var encoder = Base64.getUrlEncoder().withoutPadding();
        return "Bearer eyJhbGciOiJub25lIn0." + encoder.encodeToString(claims.getBytes(StandardCharsets.UTF_8))
                + ".firma";
    }
}
