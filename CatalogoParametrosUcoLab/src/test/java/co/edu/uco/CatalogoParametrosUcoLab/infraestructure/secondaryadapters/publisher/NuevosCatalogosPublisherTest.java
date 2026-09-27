package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import reactor.core.publisher.Flux;

class NuevosCatalogosPublisherTest {
    private static final String APP = "co.edu.uco.CatalogoParametrosUcoLab.application.features.";
    private static final String INFRA =
            "co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.";

    static Stream<String[]> casos() {
        return Stream.of("ambiente:Ambiente", "estadoambiente:EstadoAmbiente",
                        "estadometadatoambiente:EstadoMetadatoAmbiente",
                        "metadatoambiente:MetadatoAmbiente")
                .flatMap(definition -> {
                    final var parts = definition.split(":");
                    return Stream.of(new String[] { parts[0], parts[1], "Crear", "created", "CREATED" },
                            new String[] { parts[0], parts[1], "Actualizar", "updated", "UPDATED" },
                            new String[] { parts[0], parts[1], "Eliminar", "deleted", "DELETED" });
                });
    }

    @ParameterizedTest
    @MethodSource("casos")
    void debePublicarEventos(final String feature, final String entityName, final String operation,
            final String factoryMethod, final String eventType) throws Exception {
        final var operationPackage = operation.toLowerCase() + entityName.toLowerCase();
        final var eventClassName = APP + feature + "." + operationPackage
                + ".secondaryports.event." + operation + entityName + "Event";
        final var publisherClassName = INFRA + feature + "." + operationPackage
                + "." + operation + entityName + "PublisherImpl";

        final var eventClass = Class.forName(eventClassName);
        final var entityClass = Class.forName(
                "co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity."
                        + entityName + "Entity");
        final Method factory = eventClass.getMethod(factoryMethod, entityClass);
        final var event = factory.invoke(null, new Object[] { null });

        assertNotNull(event);
        assertEquals(eventType, eventClass.getMethod("getEvent").invoke(event).toString());
        assertEquals(null, eventClass.getMethod("get" + entityName).invoke(event));

        final var publisher = Class.forName(publisherClassName).getConstructor().newInstance();
        final Method getStream = publisher.getClass().getMethod("getStream");
        final Method sendEvent = publisher.getClass().getMethod("sendEvent", eventClass);
        @SuppressWarnings("unchecked")
        final Flux<Object> stream = (Flux<Object>) getStream.invoke(publisher);

        sendEvent.invoke(publisher, event);
        assertEquals(event, stream.blockFirst());
    }
}
