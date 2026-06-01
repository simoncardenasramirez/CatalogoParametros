package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class SurrealDbClient {

    private static final MediaType SURREALQL = MediaType.valueOf("application/surrealql");

    private final RestClient restClient;
    private final SurrealDbProperties properties;
    private final ObjectMapper objectMapper;

    public SurrealDbClient(final SurrealDbProperties properties, final ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().baseUrl(properties.getUrl()).build();
    }

    public JsonNode execute(final String query) {
        try {
            var body = restClient.post()
                    .uri("/sql")
                    .headers(headers -> {
                        headers.setBasicAuth(properties.getUsername(), properties.getPassword());
                        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
                        headers.setContentType(SURREALQL);
                    })
                    .body(withDatabaseContext(query))
                    .retrieve()
                    .body(String.class);

            var response = objectMapper.readTree(body);
            validateSuccessfulResponse(response);
            return response;
        } catch (final ParametroException exception) {
            throw exception;
        } catch (final RestClientResponseException exception) {
            throw new ParametroException("SurrealDB rechazo la operacion: " + exception.getResponseBodyAsString());
        } catch (final Exception exception) {
            throw new ParametroException("No fue posible ejecutar la operacion en SurrealDB: " + exception.getMessage());
        }
    }

    private void validateSuccessfulResponse(final JsonNode response) {
        if (!response.isArray()) {
            throw new ParametroException("SurrealDB retorno una respuesta inesperada.");
        }

        for (var statement : response) {
            var status = statement.path("status").asText();
            if (!"OK".equalsIgnoreCase(status)) {
                var detail = statement.path("detail").asText(statement.path("result").toString());
                throw new ParametroException("SurrealDB rechazo la operacion: " + detail);
            }
        }
    }

    private String withDatabaseContext(final String query) {
        return """
                DEFINE NAMESPACE IF NOT EXISTS %s;
                USE NS %s;
                DEFINE DATABASE IF NOT EXISTS %s;
                USE DB %s;
                DEFINE TABLE IF NOT EXISTS parametros SCHEMALESS;
                DEFINE INDEX IF NOT EXISTS idx_parametros_nombre ON TABLE parametros FIELDS nombre UNIQUE;
                %s
                """.formatted(properties.getNamespace(), properties.getNamespace(), properties.getDatabase(),
                properties.getDatabase(), query);
    }
}
