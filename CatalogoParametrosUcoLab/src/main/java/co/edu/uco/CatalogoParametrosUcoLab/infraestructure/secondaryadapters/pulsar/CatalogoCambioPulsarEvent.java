package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import java.util.UUID;

import tools.jackson.databind.JsonNode;

/** Contrato externo de invalidacion: no contiene valores de parametros ni secretos. */
public record CatalogoCambioPulsarEvent(
        String source,
        String eventType,
        String resource,
        String operation,
        String resourceName,
        JsonNode resourceValue,
        UUID resourceId,
        UUID parentId,
        UUID applicationId,
        UUID environmentId,
        UUID eventId,
        int schemaVersion,
        String occurredAt) {

    public static final int CURRENT_SCHEMA_VERSION = 3;
}
