---
name: hexagon-scaffolder
description: Genera features/CRUD completos (crear, actualizar, consultar, eliminar) para CatalogoParametrosUcoLab reproduciendo ESTRICTAMENTE el patrón hexagonal real del código (estructura de paquetes, clases, imports y estilo del feature `parametro`). Úsala cuando el usuario pida crear, consultar, actualizar o eliminar una entidad nueva o ampliar una existente.
---

# Hexagon Scaffolder Skill

Plantilla exacta para generar operaciones/features del backend `CatalogoParametrosUcoLab`. **Antes de generar, lee el código real del feature `parametro` y reprodúcelo con exactitud** (nombres de paquete, imports, orden de imports, estilo). Esta skill define el mapa de archivos y las reglas de contenido; los bloques exactos se copian del patrón existente.

## 1. Convenciones de nombres

| Concepto | Valor |
|---|---|
| Feature (paquete) | `parametro`, `funcionalidad`, `modulo`, `organizacion`, `aplicacion`, `tipoparametro`, `metadato` |
| Feature (clase) | `Parametro`, `Funcionalidad`, `Modulo`, `Organizacion`, `Aplicacion`, `TipoParametro`, `Metadato` |
| Operación (paquete) | `crear<feature>`, `actualizar<feature>`, `consultar<feature>`, `eliminar<feature>` |
| Operación (prefijo clase) | `CrearX`, `ActualizarX`, `ConsultarX`, `EliminarX` |
| Paquete usecase impl | `<operacion>impl` (p. ej. `crearparametroimpl`; **sin typos** — la deuda conocida `actualizarorganizidadimpl` es un error a no repetir) |
| Paquete base | `co.edu.uco.CatalogoParametrosUcoLab` |

Estado verificado de features (deuda): `metadato` incompleto (solo dominio); `modulo` sin eliminar; mappers cruzados en `crearfuncionalidad`, `crearmodulo` y `crearparametro` (no replicarlos).

## 2. Mapa de archivos por operación

### CREAR — `application/features/<feature>/crear<feature>/`
```
Crear<Feature>.java                      interface extends UseCaseWithOutReturn<Crear<Feature>Domain>
Crear<Feature>RuleValidator.java         interface extends RuleValidator<Crear<Feature>Domain>
primaryports/
  dto/Crear<Feature>DtoRequest.java       campos String, validación en setters
  dto/Crear<Feature>DtoInput.java         campos normalizados (String, UUID, boolean)
  interactor/Crear<Feature>Interactor.java        extends InteractorWithOutReturn<Crear<Feature>DtoRequest>
  interactor/impl/Crear<Feature>InteractorImpl.java  @Service, delega en Crear<Feature>, usa Crear<Feature>DtoMapper.INSTANCE
  interactor/mapper/Crear<Feature>DtoMapper.java    enum INSTANCE (toDtoInput, toDomain) — SOLO este mapper
secondaryports/
  event/Crear<Feature>Event.java          implements <Feature>Event, EventType.CREATED, factory created(entity)
  publisher/Crear<Feature>Publisher.java  extends Publisher<Crear<Feature>Event>
usecase/
  crear<feature>impl/Crear<Feature>Impl.java             @Service implements Crear<Feature>
  crear<feature>impl/Crear<Feature>RuleValidatorImpl.java  @Service implements Crear<Feature>RuleValidator
  domain/Crear<Feature>Domain.java         extends Domain, create(...), setters privados
  domain/rules/<Feature><Regla>Rule.java         (+ impl/<Feature><Regla>RuleImpl.java)
```

### ACTUALIZAR — `application/features/<feature>/actualizar<feature>/`
Igual que crear, PERO:
- Interactor firma `void execute(UUID id, Actualizar<Feature>DtoRequest data)` (NO extiende puerto genérico).
- `Actualizar<Feature>DtoMapper.toDomain(UUID id, ...)` — el domain lleva id.
- `Actualizar<Feature>Impl`: valida `UUIDHelper.getDefault().equals(data.getId())` (ValidationException) y `findById(...).isEmpty()` (NotFoundException) ANTES de las reglas; guarda con `repository.update`.

### CONSULTAR — `application/features/<feature>/consultar<feature>/`
```
primaryports/
  interactor/Consultar<Feature>Interactor.java
      extends InteractorWithReturn<UUID, List<<Feature>Entity>>  +  List<<Feature>Entity> execute()
  interactor/impl/Consultar<Feature>InteractorImpl.java  @Service, inyecta <Feature>Repository
      execute() -> repository.findAll()
      execute(UUID id) -> repository.findById(id).map(List::of).orElse(List.of())
```

### ELIMINAR — `application/features/<feature>/eliminar<feature>/`
```
Eliminar<Feature>.java                    interface extends UseCaseWithOutReturn<UUID>
primaryports/
  interactor/Eliminar<Feature>Interactor.java         extends InteractorWithOutReturn<UUID>
  interactor/impl/Eliminar<Feature>InteractorImpl.java  @Service, delega en Eliminar<Feature>
secondaryports/
  event/Eliminar<Feature>Event.java        implements <Feature>Event, EventType.DELETED, factory deleted(entity)
  publisher/Eliminar<Feature>Publisher.java
usecase/
  eliminar<feature>impl/Eliminar<Feature>Impl.java
  domain/rules/Eliminar<Feature>IdExistsRule(+Impl)
  domain/rules/Eliminar<Feature>IsNotUsedBy<X>Rule(+Impl)   (si hay integridad referencial)
```

### POR FEATURE (si no existe) — núcleo y adaptadores
```
application/secondaryports/entity/<Feature>Entity.java
application/secondaryports/repository/<Feature>Repository.java
application/features/<feature>/secondaryports/event/<Feature>Event.java   (interfaz marcador)
infraestructure/secondaryadapters/repository/<feature>/SurrealDb<Feature>Repository.java
infraestructure/secondaryadapters/publisher/<feature>/<operacion>/<Operacion>PublisherImpl.java
infraestructure/primaryadapters/response/<feature>/<Feature>Response.java
infraestructure/primaryadapters/controller/<feature>/<Feature>Controller.java
```

**TRES PASOS OBLIGATORIOS adicionales cuando la feature es NUEVA (no basta con crear los archivos):**

1. **Registrar la tabla en SurrealDB.** Añadir en `infraestructure/secondaryadapters/surrealdb/SurrealDbClient.java` (método `withDatabaseContext`) la línea:
   ```
   DEFINE TABLE IF NOT EXISTS <tabla_plural> SCHEMALESS;
   ```
   junto a las 6 existentes (`parametros`, `funcionalidades`, `modulos`, `organizaciones`, `aplicaciones`, `tipos_parametro`). Sin esto, el repositorio falla en runtime ("table not found").
2. **Crear los mensajes MSG en `src/main/resources/message.properties`.** Las reglas/DTOs/use cases usan `consultarMensajePort.consultarMensaje("MSG-xxx")`; si el código no existe, `PropertiesHelper` lanza `NotFoundException`. Continuar la numeración desde **MSG-145** (la actual termina en MSG-144) con mensajes literales en español del feature (obligatorio, vacío, longitud, formato, ya-existe, referencia no existe, id obligatorio para actualizar/eliminar, no encontrado, etc.).
3. **Plural correcto en español SIN tildes** en `TABLE_NAME`, ruta del controller y body del SSE: `pais` → `paises`, `ciudad` → `ciudades`, `pais` singular en clase `Pais`/`PaisEntity`. En código nunca se usan acentos ni la "ñ" en identificadores.

## 3. Contenido exacto por tipo de clase

### RuleValidator (`Crear<Feature>RuleValidator`)
- `public interface Crear<Feature>RuleValidator extends RuleValidator<Crear<Feature>Domain> { }` (cuerpo vacío). Imports: `RuleValidator` de `application.usecase.validator` y el domain.

### RuleValidatorImpl (`Crear<Feature>RuleValidatorImpl`)
- `@Service public class Crear<Feature>RuleValidatorImpl implements Crear<Feature>RuleValidator`.
- Inyecta por **constructor** TODAS las reglas de la operación (`final`), en orden de ejecución.
- `validate(data)` ejecuta cada regla en orden: `regla1.execute(data); regla2.execute(data); ...` (orden del código real de `parametro`: isNotNull → isNotEmpty → length → format → isValid de referencias → exists de referencias → doesNotExist de nombre).

### Interactor (`Crear<Feature>Interactor`)
- `public interface Crear<Feature>Interactor extends InteractorWithOutReturn<Crear<Feature>DtoRequest> { }` (cuerpo vacío). Imports: `InteractorWithOutReturn` de `application.primaryports` y el `DtoRequest`.

### InteractorImpl (`Crear<Feature>InteractorImpl`)
- `@Service public final class Crear<Feature>InteractorImpl implements Crear<Feature>Interactor`.
- Inyecta por constructor el use case `Crear<Feature>`.
- `execute(Crear<Feature>DtoRequest data)`: `var dtoInput = Crear<Feature>DtoMapper.INSTANCE.toDtoInput(data); var domain = Crear<Feature>DtoMapper.INSTANCE.toDomain(dtoInput); crear<Feature>.execute(domain);` (usar tipos explícitos o `var` según el estilo real).

### UseCase impl (`Crear<Feature>Impl`)
- `@Service`, logger `LoggerFactory.getLogger(X.class)`, `private static final String OPERATION_NAME = "crear-<feature>";`
- Inyecta por constructor: `<Feature>Repository`, `<Operacion>Publisher`, `<Operacion>RuleValidator`, `TelemetryService`.
- `execute(domain)` envuelto en `telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {...})` con logs `[CREAR-FEATURE]`.
- Flujo: validator.validate → domain.generateId() → `Entity.create(id, ...)` → repository.save → publisher.sendEvent(`Event.created(entity)`).

### DtoRequest (`Crear<Feature>DtoRequest`)
- `public final class`, campos `String` privados; constructor por defecto que delega en el completo (`TextHelper.EMPTY`, `"true"`); constructor completo que llama a los setters; `static create(...)`; getters y setters públicos.
- **Auto-validación en setters (patrón real de `CrearParametroDtoRequest`)**:
  - Cada setter normaliza primero con el helper de `crosscutting.helpers` y luego valida: `this.nombre = TextHelper.applyTrim(nombre); validateNombre();`
  - `private void validateNombre()`: `TextHelper.isBlank` → "El nombre del parametro es obligatorio."; longitud → "El nombre debe tener entre 3 y 50 caracteres."
  - IDs (string que representa UUID): try/catch `UUID.fromString(idRef)` → "El identificador de la <ref> no es valido. Valor recibido: " + valor.
  - `activo`: `activo == null ? "true" : TextHelper.applyTrim(activo).toLowerCase()` y validar contra `"true"`/`"false"`.
- Mensajes SIEMPRE literales en español (el DTO request NO usa `consultarMensajePort` ni `MSG-xxx`).

### DtoInput (`Crear<Feature>DtoInput`)
- `public final class`, campos tipados (`String`, `UUID`, `boolean`), constructor por defecto que delega en el completo con valores por defecto (`""`, `UUIDHelper.getDefault()`, `false`), constructor completo que llama a los setters, `static create(...)`, getters (boolean → `isActivo()`), setters con `UUIDHelper.getDefault(id)` para los UUID.

### DtoMapper (`enum Crear<Feature>DtoMapper { INSTANCE; }`)
- `toDomain(Crear<Feature>DtoRequest)` → `final var dtoInput = toDtoInput(dto); return toDomain(dtoInput);`.
- `toDtoInput(request)` → null-safety real: `var dtoToMap = dto == null ? new Crear<Feature>DtoRequest() : dto;` luego `UUID.fromString(dtoToMap.getIdX())`, `Boolean.parseBoolean(dtoToMap.getActivo())`, devuelve `Crear<Feature>DtoInput.create(nombre, idX, ..., activo)`.
- `toDomain(DtoInput)` → `Domain.create(UUIDHelper.getDefault(), ...)`.
- **No generar nunca** `Crear<OtraEntidad>DtoMapper.java` dentro de esta carpeta.

### Domain (`Crear<Feature>Domain extends Domain`)
- `final`, campos, constructor privado con setters privados, `create(...)`, getters públicos. Setter de texto: `TextHelper.applyTrim`; setter de UUID: `UUIDHelper.getDefault(value)`.

### Event (`Crear<Feature>Event implements <Feature>Event`)
- `private Entity entidad; private EventType event;`, `enum EventType { CREATED }`, constructor con setters, `static <Operacion>Event created(Entity)`.

### Publisher (`Crear<Feature>Publisher extends Publisher<Crear<Feature>Event>`)
- Interfaz vacía que extiende `Publisher<XxxEvent>`.

### PublisherImpl (`infraestructure/.../publisher/<feature>/<operacion>/<Operacion>PublisherImpl.java`)
- `@Component final`, `Sinks.Many<XxxEvent> sink = Sinks.many().replay().limit(100);`
- `sendEvent` → `sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST)`; `getStream` → `sink.asFlux()`.

### Regla (`<Feature><Regla>Rule`)
- `interface XxxRule extends DomainRule<XxxDomain>` (o `DomainRuleWithRepository<XxxDomain, R>` si usa repositorio).

### Regla impl (`<Feature><Regla>RuleImpl`)
- `@Service final`, `@Autowired ConsultarMensajePort` (patrón real) y/o repositorio por constructor.
- Lanza `ValidationException.build(...)` / `ConflictException.build(consultarMensajePort.consultarMensaje("MSG-xxx"))`.

### Repository port (`<Feature>Repository`)
- `save`, `update`, `deleteById(UUID)`, `existsByNombre(String)`, `findById(UUID)`, `findAll()`, `findBy<Referencia>(UUID)`.

### SurrealDbRepository (`SurrealDb<Feature>Repository`)
- `@Repository`, `private static final String TABLE_NAME = "<features>";` (plural), inyecta `SurrealDbClient`.
- `save`/`update`/`deleteById` con `BEGIN TRANSACTION; ... COMMIT TRANSACTION;` y `.formatted(...)` con `escape()` para strings.
- Helpers privados: `firstStatementResult(JsonNode)`, `toEntity(JsonNode)`, `extractUuid(JsonNode)`, `escape(String)`; si la entidad tiene fechas añadir `extractDateTime(JsonNode)` (quita `d'...'`, `Z` y añade `:00` si falta) y `formatDateTime(LocalDateTime)` (`null` o `'yyyy-MM-ddTHH:mm:ss'`).
- Tipo JSON: `tools.jackson.databind.JsonNode`.

### Controller (`<Feature>Controller`)
- `@RestController final`, `@RequestMapping("/catalogo-parametros/api/v1/<features>")`.
- Inyecta por constructor: interactores (crear, actualizar, eliminar, consultar) y publishers (crear, actualizar, eliminar). Nunca repositorios.
- `GET /events` → `Flux<ServerSentEvent<XxxEvent>>` con `Flux.merge(...getStream().cast(...))` + comentario `connected`.
- `POST` → `Mono<ResponseEntity<XxxResponse>>` con `Mono.fromCallable(() -> {...}).subscribeOn(Schedulers.boundedElastic())`, catch `BusinessException` re-lanzada y `Exception` → 500.
- `PUT /{id}`, `DELETE /{id}`, `GET`, `GET /{id}` igual patrón; `GET /{id}` devuelve 404 con mensaje si la lista queda vacía.
- Mensajes literales en español: "X creado exitosamente.", "Ocurrio un error creando el x.", "No se encontro el x con el id especificado."

### Response (`<Feature>Response extends Response`)
- `private final List<<Feature>Entity> <features> = new ArrayList<>();` + getter. `Response` base tiene `private final List<String> mensajes = new ArrayList<>()` (sin setter).

### Entity (`<Feature>Entity`)
- `final`, constructor privado + privado con args, `create(...)`, getters/setters públicos con `TextHelper.applyTrim` / `UUIDHelper.getDefault`.

### GlobalExceptionHandler (referencia)
- No se genera por feature. `@RestControllerAdvice` con `DecodingException` (raíz → `ValidationException`/`InvalidFormatException`, `MSG-143`/`MSG-144`), `ValidationException` → 400, `NotFoundException` → 404, `ConflictException` → 409, `TechnicalException` → 500, `BusinessException` → 400. Logs `[EXCEPTION-HANDLER]` y `recordError`.

## 4. Reglas de generación (no negociables)

- Respetar capas y dependencias: núcleo (`application`) no importa `infraestructure`/SurrealDB/Azure; adaptadores solo implementan puertos.
- `Application`/núcleo: nombres y mensajes en español; DTOs con `create(...)` y **auto-validación en setters** (ver `DtoRequest`); dominios inmutables; mappers `enum INSTANCE`; reglas con excepciones del proyecto.
- Telemetría obligatoria en cada use case de mutación (crear/actualizar/eliminar): `recordBusinessOperation(OPERATION_NAME, () -> {...})` y logs `[OPERACION-ENTIDAD]`. **Solo en use cases; los interactores nunca registran telemetría**.
- `deleteById` SIEMPRE con `BEGIN TRANSACTION; ... COMMIT TRANSACTION;`; **nunca** `@Transactional` de `spring-tx` (no hay TransactionManager; SurrealDB por RestClient).
- Cada feature genera SU propio `<Feature>Response` (nunca reutilizar el de otra feature).
- Consultar: el interactor inyecta el repositorio directamente (patrón real); no generar `Consultar*UseCase` ni `UseCaseWithReturn` (no existen).
- Si la feature es solo-consulta (p. ej. `tipoparametro`), solo generar interactor+impl (consultar) y su controller/response.
- **Nunca** generar mappers cruzados ni carpetas vacías (`.gitkeep`).

## 5. Verificación post-generación

1. `Glob` para confirmar que existen TODOS los archivos del mapa (sin `.gitkeep` solos).
2. Revisar imports: `application` jamás importa `infraestructure`; `infraestructure` solo puertos.
3. Comprobar que en `crear<feature>/.../mapper/` solo existe `Crear<Feature>DtoMapper.java` (los mappers cruzados preexistentes son deuda; no generarlos).
4. Compilar: `.\mvnw.cmd compile`.
5. Avisar al orquestador para correr `architecture-checker` y `test-runner`.
6. Si se piden tests para la feature generada, el orquestador delega en `sub-agents/test-writer` (skill `test-builder`), que usa las mismas convenciones `debe<Escenario>Cuando<Condición>` y nunca mockea `TelemetryService` en use cases.