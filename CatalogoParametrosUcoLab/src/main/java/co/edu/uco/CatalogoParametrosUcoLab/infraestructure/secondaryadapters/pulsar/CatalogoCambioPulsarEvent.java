package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import java.util.UUID;

/** Contrato externo de invalidacion: no contiene valores de parametros ni secretos. */
public record CatalogoCambioPulsarEvent(
        String source,
        String eventType,
        String resource,
        String operation,
        UUID resourceId,
        UUID parentId,
        UUID environmentId,
        UUID eventId) {
}
