package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;

class NuevasFeaturesUseCaseTest {
    private static final String APP = "co.edu.uco.CatalogoParametrosUcoLab.application.features.";
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
    void debeEjecutarCasosDeUso(final String feature, final String name) throws Exception {
        final var id = UUID.randomUUID();
        final var entityClass = Class.forName(ENTITY + name + "Entity");
        final var entity = createEntity(entityClass, name, id);

        executeMutation(feature, name, "Crear", entity, id);
        executeMutation(feature, name, "Actualizar", entity, id);
        executeDelete(feature, name, entity, id);
        executeQueries(feature, name, entity, id);
        executeApplicationFlow(feature, name, entity, id);
    }

    private void executeApplicationFlow(final String feature, final String name,
            final Object entity, final UUID id) throws Exception {
        for (final var operation : List.of("Crear", "Actualizar")) {
            final var folder = operation.toLowerCase() + name.toLowerCase();
            final var requestClass = Class.forName(APP + feature + "." + folder
                    + ".primaryports.dto." + operation + name + "DtoRequest");
            final Object request;
            if ("MetadatoAmbiente".equals(name)) {
                final var value = UUID.randomUUID().toString();
                request = requestClass.getConstructor(String.class, String.class, String.class)
                        .newInstance(value, value, value);
            } else {
                request = requestClass.getConstructor(String.class).newInstance(" Nombre valido ");
            }

            final var mapperClass = Class.forName(APP + feature + "." + folder
                    + ".primaryports.interactor.mapper." + operation + name + "DtoMapper");
            final var mapper = mapperClass.getField("INSTANCE").get(null);
            final var input = mapperClass.getMethod("toDtoInput", requestClass).invoke(mapper, request);
            assertNotNull(input);

            final var interactorClass = Class.forName(APP + feature + "." + folder
                    + ".primaryports.interactor.impl." + operation + name + "InteractorImpl");
            final var useCaseType = interactorClass.getConstructors()[0].getParameterTypes()[0];
            final var useCase = mock(useCaseType, invocation -> entity);
            final var interactor = interactorClass.getConstructors()[0].newInstance(useCase);
            final Object result = "Crear".equals(operation)
                    ? interactorClass.getMethod("execute", requestClass).invoke(interactor, request)
                    : interactorClass.getMethod("execute", UUID.class, requestClass)
                            .invoke(interactor, id, request);
            assertNotNull(result);
        }

        final var deleteFolder = "eliminar" + name.toLowerCase();
        final var deleteInteractorClass = Class.forName(APP + feature + "." + deleteFolder
                + ".primaryports.interactor.impl.Eliminar" + name + "InteractorImpl");
        final var deleteUseCaseType = deleteInteractorClass.getConstructors()[0].getParameterTypes()[0];
        final var deleteInteractor = deleteInteractorClass.getConstructors()[0]
                .newInstance(mock(deleteUseCaseType));
        deleteInteractorClass.getMethod("execute", UUID.class).invoke(deleteInteractor, id);
    }

    private void executeMutation(final String feature, final String name, final String operation,
            final Object entity, final UUID id) throws Exception {
        final var folder = operation.toLowerCase() + name.toLowerCase();
        final var implClass = Class.forName(APP + feature + "." + folder + ".usecase."
                + folder + "impl." + operation + name + "Impl");
        final var instance = instantiate(implClass, entity);
        final var domainClass = Class.forName(APP + feature + "." + folder
                + ".usecase.domain." + operation + name + "Domain");
        final var domain = createDomain(domainClass, name, "Crear".equals(operation) ? new UUID(0, 0) : id);
        final Method execute = implClass.getMethod("execute", domainClass);
        assertNotNull(execute.invoke(instance, domain));
    }

    private void executeDelete(final String feature, final String name,
            final Object entity, final UUID id) throws Exception {
        final var folder = "eliminar" + name.toLowerCase();
        final var implClass = Class.forName(APP + feature + "." + folder + ".usecase."
                + folder + "impl.Eliminar" + name + "Impl");
        final var instance = instantiate(implClass, entity);
        implClass.getMethod("execute", UUID.class).invoke(instance, id);
    }

    private void executeQueries(final String feature, final String name,
            final Object entity, final UUID id) throws Exception {
        final var folder = "consultar" + name.toLowerCase();
        final var implClass = Class.forName(APP + feature + "." + folder
                + ".primaryports.interactor.impl.Consultar" + name + "InteractorImpl");
        final var instance = instantiate(implClass, entity);
        assertNotNull(implClass.getMethod("execute", UUID.class).invoke(instance, id));
        assertNotNull(implClass.getMethod("execute", int.class, int.class).invoke(instance, 0, 0));
    }

    private Object instantiate(final Class<?> type, final Object entity) throws Exception {
        final Constructor<?> constructor = type.getConstructors()[0];
        final var arguments = Stream.of(constructor.getParameterTypes())
                .map(parameter -> mock(parameter, invocation -> {
                    final var method = invocation.getMethod().getName();
                    if ("findById".equals(method)) return Optional.of(entity);
                    if ("findAllPaginado".equals(method)) return List.of(entity);
                    if ("save".equals(method) || "update".equals(method)) return invocation.getArgument(0);
                    if (invocation.getMethod().getReturnType() == boolean.class) return false;
                    return Answers.RETURNS_DEFAULTS.answer(invocation);
                })).toArray();
        return constructor.newInstance(arguments);
    }

    private Object createEntity(final Class<?> type, final String name, final UUID id) throws Exception {
        if ("MetadatoAmbiente".equals(name)) {
            final var reference = UUID.randomUUID();
            return type.getMethod("create", UUID.class, UUID.class, UUID.class, UUID.class)
                    .invoke(null, id, reference, reference, reference);
        }
        return type.getMethod("create", UUID.class, String.class).invoke(null, id, "Nombre valido");
    }

    private Object createDomain(final Class<?> type, final String name, final UUID id) throws Exception {
        if ("MetadatoAmbiente".equals(name)) {
            final var reference = UUID.randomUUID();
            return type.getMethod("create", UUID.class, UUID.class, UUID.class, UUID.class)
                    .invoke(null, id, reference, reference, reference);
        }
        return type.getMethod("create", UUID.class, String.class).invoke(null, id, "Nombre valido");
    }
}
