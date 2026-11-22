package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.consumer;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Lee el client_id del JWT de una identidad de servicio. La firma y los permisos del token los valida el
 * PEP antes de que la petición alcance el controlador; aquí solo se obtiene el claim para resolver el
 * ambiente asociado en el catálogo.
 */
@Component
public final class ServiceIdentityClientIdReader {
    private static final String BEARER = "Bearer ";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String read(final ServerWebExchange exchange) {
        final String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.regionMatches(true, 0, BEARER, 0, BEARER.length())) {
            return null;
        }
        final String[] parts = authorization.substring(BEARER.length()).trim().split("\\.");
        if (parts.length != 3) {
            return null;
        }
        try {
            final JsonNode claims = MAPPER.readTree(Base64.getUrlDecoder().decode(parts[1]));
            final String authorizedParty = text(claims, "azp");
            return authorizedParty == null ? text(claims, "client_id") : authorizedParty;
        } catch (final Exception exception) {
            return null;
        }
    }

    private static String text(final JsonNode claims, final String name) {
        final JsonNode value = claims == null ? null : claims.get(name);
        return value != null && value.isTextual() && !value.asText().isBlank() ? value.asText() : null;
    }
}
