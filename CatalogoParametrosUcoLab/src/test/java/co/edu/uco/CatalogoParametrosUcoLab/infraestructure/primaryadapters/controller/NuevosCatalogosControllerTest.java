package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class NuevosCatalogosControllerTest {
    private static final String APP = "co.edu.uco.CatalogoParametrosUcoLab.application.features.";
    private static final String INFRA =
            "co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.";
    private static final String ENTITY =
            "co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.";

    static Stream<String[]> features() {
        return Stream.of(new String[] {"ambiente", "Ambiente"},
                new String[] {"estadoambiente", "EstadoAmbiente"},
                new String[] {"estadometadatoambiente", "EstadoMetadatoAmbiente"},
                new String[] {"metadatoambiente", "MetadatoAmbiente"});
    }

    @ParameterizedTest
    @MethodSource("features")
    void debeEjecutarTodosLosEndpoints(final String feature, final String name) throws Exception {
        final var id = UUID.randomUUID();
        final var entity = createEntity(name, id);
        final var controllerClass = Class.forName(INFRA + feature + "." + name + "Controller");
        final var constructor = controllerClass.getConstructors()[0];
        final var dependencies = Stream.of(constructor.getParameterTypes())
                .map(type -> mock(type, invocation -> {
                    if ("getStream".equals(invocation.getMethod().getName())) return Flux.empty();
                    if ("execute".equals(invocation.getMethod().getName())) {
                        if (invocation.getMethod().getReturnType() == List.class) return List.of(entity);
                        if (invocation.getMethod().getReturnType() == void.class) return null;
                        return entity;
                    }
                    return Answers.RETURNS_DEFAULTS.answer(invocation);
                })).toArray();
        final var controller = constructor.newInstance(dependencies);

        final var event = (Flux<?>) controllerClass.getMethod("publicarEventos").invoke(controller);
        assertNotNull(event.blockFirst());

        final var createRequest = createRequest(feature, name, "Crear");
        final var updateRequest = createRequest(feature, name, "Actualizar");
        assertOk((Mono<?>) controllerClass.getMethod("crear", createRequest.getClass())
                .invoke(controller, createRequest));
        assertOk((Mono<?>) controllerClass.getMethod("actualizar", UUID.class, updateRequest.getClass())
                .invoke(controller, id, updateRequest));
        assertOk((Mono<?>) controllerClass.getMethod("eliminar", UUID.class).invoke(controller, id));
        assertOk((Mono<?>) controllerClass.getMethod("consultar", int.class, int.class)
                .invoke(controller, 1, 10));
        assertOk((Mono<?>) controllerClass.getMethod("consultarPorId", UUID.class)
                .invoke(controller, id));
    }

    private void assertOk(final Mono<?> response) {
        final var value = response.block();
        assertNotNull(value);
        assertNotNull(((ResponseEntity<?>) value).getBody());
    }

    private Object createRequest(final String feature, final String name,
            final String operation) throws Exception {
        final var folder = operation.toLowerCase() + name.toLowerCase();
        final var type = Class.forName(APP + feature + "." + folder + ".primaryports.dto."
                + operation + name + "DtoRequest");
        if ("MetadatoAmbiente".equals(name)) {
            final var value = UUID.randomUUID().toString();
            return type.getConstructor(String.class, String.class, String.class)
                    .newInstance(value, value, value);
        }
        return type.getConstructor(String.class).newInstance("Nombre valido");
    }

    private Object createEntity(final String name, final UUID id) throws Exception {
        final var type = Class.forName(ENTITY + name + "Entity");
        if ("MetadatoAmbiente".equals(name)) {
            final var value = UUID.randomUUID();
            return type.getMethod("create", UUID.class, UUID.class, UUID.class, UUID.class)
                    .invoke(null, id, value, value, value);
        }
        return type.getMethod("create", UUID.class, String.class)
                .invoke(null, id, "Nombre valido");
    }
}
