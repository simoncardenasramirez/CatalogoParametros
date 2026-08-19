---
name: test-builder
description: Genera tests unitarios y de integración (JUnit 5 + Mockito + WebTestClient) para CatalogoParametrosUcoLab reproduciendo las convenciones reales del proyecto (nombres debe<Escenario>Cuando<Condición>, excepciones del proyecto, TelemetryService real con SimpleMeterRegistry). Úsala cuando el usuario pida crear tests, aumentar cobertura o verificar una operación (crear/actualizar/consultar/eliminar), una regla, un DTO, un use case, un interactor, un controlador o el GlobalExceptionHandler.
---

# Test Builder Skill

Plantillas exactas para escribir tests del backend `CatalogoParametrosUcoLab` (JUnit 5, Mockito, WebTestClient). **Antes de escribir, lee el código real bajo test y el patrón de referencia** (use cases/rules/DTOs de la feature `parametro`).

> Las plantillas usan la feature `parametro` como ejemplo pero son **100% genéricas**: aplican igual a features nuevas (`Pais`, `Ciudad`, ...). Al usarlas con otra entidad, cambia solo nombres/paquetes/imports (`Pais`, `pais`, `paises`, `crearpais`); la estructura y los escenarios se mantienen. Si la feature es nueva, los mensajes de las reglas usan los `MSG-xxx` creados por `feature-builder` en `message.properties`.

## 0. Infraestructura de test (verificado)

- Dependencia: `spring-boot-starter-test` (scope test) → incluye **JUnit 5** (`junit-jupiter`), **Mockito** (`mockito-core` + `mockito-junit-jupiter`), **AssertJ**, Hamcrest, **WebTestClient** (vía `spring-test` + `spring-webflux`). **No hay** Testcontainers ni `spring-boot-testcontainers` ni `application-test.properties`.
- Ubicación de tests: `src/test/java/co/edu/uco/CatalogoParametrosUcoLab/` (espejo del paquete de la clase bajo test).
- Tests existentes de referencia: `crosscutting/helpers/PropertiesHelperTest` (`assertEquals`/`assertThrows` sin Spring) y `CatalogoParametrosUcoLabApplicationTests` (`@SpringBootTest` carga el contexto sin infraestructura externa: `SecretClient` y `SurrealDbClient` no conectan en la construcción).
- **Nombres de método SIEMPRE**: `debe<Escenario>Cuando<Condición>()`.
- **Excepciones del proyecto** en `assertThrows`: `ValidationException`, `NotFoundException`, `ConflictException`, `TechnicalException` (todas extienden `BusinessException`).

### Reglas de oro para testear este proyecto

1. **`TelemetryService` NUNCA se mockea en use cases**: `recordBusinessOperation` ejecuta el `Runnable`/`Supplier`; si lo mockeas, el código bajo test NO se ejecuta. Usar el real: `new TelemetryService(new SimpleMeterRegistry())` (`io.micrometer.core.instrument.simple.SimpleMeterRegistry`).
2. **`ConsultarMensajePort` sí se mockea**: use cases y reglas lo inyectan por campo `@Autowired`; `@InjectMocks` de Mockito lo inyecta igualmente por reflexión. Usar `when(consultarMensajePort.consultarMensaje("MSG-xxx")).thenReturn("mensaje")`.
3. **Use cases con inyección por constructor** (repo + publisher + validator + telemetry) → `@InjectMocks` funciona; si hace falta pasar `TelemetryService` real, construirlo y asignarlo (Mockito no crea el real).
4. **Controllers reactivos** → `@WebFluxTest(<Controller>.class)` + `@MockitoBean` para interactores y publishers + `@Import(GlobalExceptionHandler.class)` + `WebTestClient` (`@Autowired`).
5. **`@MockitoBean`** (de `org.springframework.test.context.bean.override.mockito.MockitoBean`) — disponible en Spring Boot 4; `@MockBean` está deprecado.

## 1. DTO Request (`CrearParametroDtoRequest`)

Sin Spring. **Auto-validación en los setters** (patrón real): cada setter normaliza con `TextHelper.applyTrim` (helper de `crosscutting.helpers`) y llama a un `private validateXxx()` que lanza `ValidationException.build("mensaje literal en español")` — el DTO request NO usa `consultarMensajePort`. IDs: `UUID.fromString(...)` en try/catch; `activo`: `== null ? "true" : applyTrim(...).toLowerCase()` validado contra "true"/"false". Paquete espejo: `.../crearparametro/primaryports/dto/`.

```java
package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearParametroDtoRequestTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearParametroDtoRequest();
        assertEquals("", dto.getNombre());
        assertEquals("", dto.getIdFuncionalidad());
        assertEquals("", dto.getIdTipoParametro());
        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> new CrearParametroDtoRequest(" ", "uuid", "uuid", "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> new CrearParametroDtoRequest("ab", "uuid", "uuid", "true"));
        assertThrows(ValidationException.class,
                () -> new CrearParametroDtoRequest("a".repeat(51), "uuid", "uuid", "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> new CrearParametroDtoRequest("nombre", "no-es-uuid", "uuid", "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> new CrearParametroDtoRequest("nombre", UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(), "si"));
    }
}
```

## 1.5 Mapper (`CrearParametroDtoMapper`)

`enum INSTANCE`, sin Spring. Probar el flujo completo Request → Input → Domain: parseo de `UUID.fromString`/`Boolean.parseBoolean`, null-safety de `toDtoInput(null)` y que `toDomain(request)` sea equivalente a `toDomain(toDtoInput(request))`.

```java
package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearParametroDtoMapperTest {

    @Test
    void debeConvertirRequestTodoStringEnInputConDatosReales() {
        var idFuncionalidad = UUID.randomUUID().toString();
        var idTipoParametro = UUID.randomUUID().toString();
        var request = CrearParametroDtoRequest.create("parametro", idFuncionalidad, idTipoParametro, "true");

        CrearParametroDtoInput input = CrearParametroDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("parametro", input.getNombre());
        assertEquals(UUID.fromString(idFuncionalidad), input.getIdFuncionalidad());
        assertEquals(UUID.fromString(idTipoParametro), input.getIdTipoParametro());
        assertTrue(input.isActivo());
    }

    @Test
    void debeUsarValoresPorDefectoCuandoElRequestEsNulo() {
        var input = CrearParametroDtoMapper.INSTANCE.toDtoInput(null);
        assertEquals(UUIDHelper.getDefault(), input.getIdFuncionalidad());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYTdeInput() {
        var request = CrearParametroDtoRequest.create("parametro", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "false");
        var viaRequest = CrearParametroDtoMapper.INSTANCE.toDomain(request);
        var viaInput = CrearParametroDtoMapper.INSTANCE.toDomain(CrearParametroDtoMapper.INSTANCE.toDtoInput(request));
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
    }
}
```

## 2. Domain y Entity

Setters privados (domain) / públicos (entity) con `TextHelper.applyTrim` y `UUIDHelper.getDefault`. Probar normalización:

```java
@Test
void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
    var domain = CrearParametroDomain.create(null, "  nombre  ", UUID.randomUUID(), UUID.randomUUID(), true);
    assertEquals(UUIDHelper.getDefault(), domain.getId());
    assertEquals("nombre", domain.getNombre());
}
```

## 3. Regla de dominio (`ParametroNameIsNotEmptyRuleImpl`)

`@ExtendWith(MockitoExtension.class)`, `@Mock ConsultarMensajePort`, `@InjectMocks` la regla. Probar éxito y fracaso (con la excepción correcta).

```java
package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.ConsultarMensajePort;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class ParametroNameIsNotEmptyRuleImplTest {

    @Mock
    private ConsultarMensajePort consultarMensajePort;

    @InjectMocks
    private ParametroNameIsNotEmptyRuleImpl rule;

    private CrearParametroDomain domainConNombre(final String nombre) {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), nombre,
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debePasarCuandoElNombreNoEstaVacio() {
        assertDoesNotThrow(() -> rule.execute(domainConNombre("parametro valido")));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        when(consultarMensajePort.consultarMensaje("MSG-134")).thenReturn("El nombre del parametro no puede estar vacio.");
        assertThrows(ValidationException.class, () -> rule.execute(domainConNombre("  ")));
    }
}
```

Reglas con repositorio (p. ej. `ParametroFuncionalidadExistsRuleImpl`): `@Mock FuncionalidadRepository`, `@Mock ConsultarMensajePort`, `@InjectMocks`; simular `findById(...)` con `Optional.of(...)`/`Optional.empty()` y verificar `ValidationException` (referencia nula/default) o `NotFoundException` (no existe).

## 4. RuleValidator (`CrearParametroRuleValidatorImpl`)

`@Mock` todas las reglas, `@InjectMocks` el validator; verificar que se ejecutan en orden con `InOrder`.

```java
@ExtendWith(MockitoExtension.class)
class CrearParametroRuleValidatorImplTest {

    @Mock private ParametroNameIsNotNullRule r1;
    @Mock private ParametroNameIsNotEmptyRule r2;
    // ... las 8 reglas
    @InjectMocks private CrearParametroRuleValidatorImpl validator;

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = ...;
        validator.validate(domain);
        InOrder inOrder = inOrder(r1, r2, r3, r4, r5, r6, r7, r8);
        inOrder.verify(r1).execute(domain);
        inOrder.verify(r2).execute(domain);
        // ... resto
    }
}
```

## 5. Use case (`CrearParametroImpl`, `ActualizarParametroImpl`, `EliminarParametroImpl`)

**Con `TelemetryService` real** (`new TelemetryService(new SimpleMeterRegistry())`), `@Mock` repo, publisher, validator (y `@Mock ConsultarMensajePort` para actualizar/eliminar).

```java
package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametroRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@ExtendWith(MockitoExtension.class)
class CrearParametroImplTest {

    @Mock private ParametroRepository parametroRepository;
    @Mock private CrearParametroPublisher crearParametroPublisher;
    @Mock private CrearParametroRuleValidator crearParametroRuleValidator;

    @InjectMocks
    private CrearParametroImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearParametroImpl(parametroRepository, crearParametroPublisher,
                crearParametroRuleValidator, new TelemetryService(new SimpleMeterRegistry()));
    }

    private CrearParametroDomain domainValido() {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeCrearParametroExitosamenteCuandoLosDatosSonValidos() {
        var domain = domainValido();
        when(parametroRepository.save(any(ParametroEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(domain);

        verify(crearParametroRuleValidator).validate(domain);
        verify(parametroRepository).save(any(ParametroEntity.class));
        verify(crearParametroPublisher).sendEvent(any(CrearParametroEvent.class));
    }

    @Test
    void debeFallarCuandoLaValidacionFalla() {
        var domain = domainValido();
        org.mockito.Mockito.doThrow(ValidationException.build("error"))
                .when(crearParametroRuleValidator).validate(any());

        assertThrows(ValidationException.class, () -> useCase.execute(domain));
        verify(parametroRepository, never()).save(any());
        verify(crearParametroPublisher, never()).sendEvent(any());
    }
}
```

Para `ActualizarParametroImpl`/`EliminarParametroImpl` además: `@Mock ConsultarMensajePort`, simular `consultarMensaje("MSG-115"/"MSG-114"/...)` y `findById` (vacío → `NotFoundException`). Recordar que estos usan `@Autowired ConsultarMensajePort` por campo: con `@InjectMocks` Mockito lo inyecta por reflexión (el `setUp` con constructor no lo cubre → asignar vía `Field`/reflexión o usar `@InjectMocks` sin `setUp`).

## 6. Interactor (`CrearParametroInteractorImpl`, `ConsultarParametroInteractorImpl`)

`@Mock` el use case (interfaz) o el repositorio; `@InjectMocks` el interactor. Verificar delegación y mapeo.

```java
@ExtendWith(MockitoExtension.class)
class ConsultarParametroInteractorImplTest {

    @Mock private ParametroRepository parametroRepository;
    @InjectMocks private ConsultarParametroInteractorImpl interactor;

    @Test
    void debeDevolverTodasLasEntidadesCuandoSeConsultaSinId() {
        var entity = ParametroEntity.create(UUID.randomUUID(), "p", UUID.randomUUID(), UUID.randomUUID(), true);
        when(parametroRepository.findAll()).thenReturn(List.of(entity));
        assertEquals(List.of(entity), interactor.execute());
    }

    @Test
    void debeDevolverListaVaciaCuandoNoExisteElId() {
        when(parametroRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertTrue(interactor.execute(UUID.randomUUID()).isEmpty());
    }
}
```

## 7. Controlador (`ParametroController`) — WebTestClient

`@WebFluxTest(ParametroController.class)` + `@MockitoBean` de interactores/publishers + `@Import(GlobalExceptionHandler.class)`.

```java
package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.parametro;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametro.primaryports.interactor.ConsultarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.publisher.CrearParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.exceptionhandler.GlobalExceptionHandler;

@WebFluxTest(ParametroController.class)
@Import(GlobalExceptionHandler.class)
class ParametroControllerTest {

    @Autowired private WebTestClient webTestClient;

    @MockitoBean private CrearParametroInteractor crearParametroInteractor;
    @MockitoBean private ActualizarParametroInteractor actualizarParametroInteractor;
    @MockitoBean private EliminarParametroInteractor eliminarParametroInteractor;
    @MockitoBean private ConsultarParametroInteractor consultarParametroInteractor;
    @MockitoBean private CrearParametroPublisher crearParametroPublisher;
    @MockitoBean private ActualizarParametroPublisher actualizarParametroPublisher;
    @MockitoBean private EliminarParametroPublisher eliminarParametroPublisher;

    @Test
    void debeCrearParametroYDevolver201() {
        var idFuncionalidad = UUID.randomUUID().toString();
        var idTipoParametro = UUID.randomUUID().toString();
        webTestClient.post().uri("/catalogo-parametros/api/v1/parametros")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"parametro\",\"idFuncionalidad\":\"" + idFuncionalidad
                        + "\",\"idTipoParametro\":\"" + idTipoParametro + "\",\"activo\":\"true\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.mensajes[0]").isEqualTo("Parametro creado exitosamente.");
    }

    @Test
    void debeDevolver200YLasEntidadesAlConsultarTodos() {
        var entity = ParametroEntity.create(UUID.randomUUID(), "p", UUID.randomUUID(), UUID.randomUUID(), true);
        when(consultarParametroInteractor.execute()).thenReturn(List.of(entity));

        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.parametros[0].nombre").isEqualTo("p");
    }

    @Test
    void debeDevolver404CuandoNoExisteElId() {
        when(consultarParametroInteractor.execute(any(UUID.class))).thenReturn(List.of());
        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void debeDevolver404CuandoLaConsultaNoEncuentra() {
        org.mockito.Mockito.doThrow(NotFoundException.build("no existe"))
                .when(consultarParametroInteractor).execute(any(UUID.class));
        webTestClient.get().uri("/catalogo-parametros/api/v1/parametros/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }
}
```

Notas: `bodyValue` con JSON (no inventar UUIDs — usar `UUID.randomUUID().toString()` en el JSON con concatenación o `String.format`). Para el SSE `/events` usar `webTestClient.get().uri(".../events").exchange().expectStatus().isOk()` (publishers mockeados emiten vacío; el `comment("connected")` se envía siempre).

## 8. GlobalExceptionHandler (unitario con WebTestClient)

`@WebFluxTest(GlobalExceptionHandler.class)` + `@Import` de un controlador de prueba (o `@MockitoBean` para `TelemetryService` y `ConsultarMensajePort`, que el handler inyecta por constructor). Verificar el mapeo de estados:

| Excepción | HTTP |
|---|---|
| `ValidationException` | 400 |
| `NotFoundException` | 404 |
| `ConflictException` | 409 |
| `TechnicalException` | 500 |
| `DecodingException` / `InvalidFormatException` | 400 (con `MSG-143`/`MSG-144`) |

## 9. Helpers / crosscutting

Patrón de `PropertiesHelperTest`: `assertEquals`/`assertThrows` sin Spring. Para `TextHelper`: `applyTrim` (null → `""`), `isBlank`. Para `UUIDHelper`: `generate` (no nulo), `getDefault` (nulo → `00000000-...`, no nulo → mismo valor).

## 10. Verificación post-generación

1. `Glob` para confirmar que el test existe en el espejo del paquete.
2. Compilar tests: `.\mvnw.cmd test-compile` o directamente `.\mvnw.cmd test -Dtest=<ClaseTest>`.
3. Corre los tests vía `test-runner`/`unit-test-runner`; ningún test puede depender de infraestructura externa (SurrealDB/Azure) ni de red.
4. Informar al orquestador qué cubrió y qué dejó sin cubrir (p. ej. feature `metadato` no tiene código que testear).