---
name: architecture-checker
description: Verifica que el código de CatalogoParametrosUcoLab cumpla ESTRICTAMENTE la arquitectura hexagonal (puertos y adaptadores) y las convenciones del proyecto. Emite un informe pasó/falló por regla con archivos infractores y correcciones sugeridas. Úsala al crear o modificar cualquier clase Java (features crear/actualizar/consultar/eliminar, controladores, repositorios, dominios, reglas) o antes de dar por terminada una tarea.
---

# Architecture Checker Skill

Valida cumplimiento arquitectónico del backend `CatalogoParametrosUcoLab` contra su patrón real. Produce un informe accionable.

## 1. Mapa real de capas

Paquete raíz: `src/main/java/co/edu/uco/CatalogoParametrosUcoLab/`

| Capa | Contenido |
|---|---|
| `init/` | `CatalogoParametrosUcoLabApplication` (`@SpringBootApplication`, `scanBasePackages`), sin lógica de negocio |
| `application/` | `primaryports/` (`InteractorWithOutReturn<T>`, `InteractorWithReturn<T,R>`), `secondaryports/` (`entity`, `repository`, `publisher/Publisher<T>`, `message/ConsultarMensajePort`, `secret/SecretVaultPort`), `usecase/` (`UseCaseWithOutReturn`, `Domain`, `domain/rule/DomainRule`, `DomainRuleWithRepository`, `validator/RuleValidator`), `common/telemetry/TelemetryService`, `features/<entidad>/<operacion>/` |
| `infraestructure/` | `config/` (`WebConfig`, `LayerTracingAspect`), `primaryadapters/` (`controller/<entidad>/`, `response/Response`, `response/<entidad>/`, `exceptionhandler/GlobalExceptionHandler`), `secondaryadapters/` (`message/`, `publisher/<entidad>/<operacion>/`, `repository/<entidad>/SurrealDb<Entidad>Repository`, `secret/azure/`, `surrealdb/`) |
| `crosscutting/` | `constants/`, `exceptions/` (`BusinessException` abstracta → `ConflictException`, `NotFoundException`, `ValidationException`, `TechnicalException` — todas extienden `BusinessException`), `helpers/` (`PropertiesHelper`, `TextHelper`, `UUIDHelper`) |

Features: `aplicacion`, `funcionalidad`, `metadato`, `modulo`, `organizacion`, `parametro`, `tipoparametro`.

### Estado y deuda conocida (verificar contra el árbol real)

| Feature | Estado | Deuda |
|---|---|---|
| `aplicacion` | Completo | Typo `eliminaraplicacion/usecase/eliminaraimpl` (debe ser `eliminaraplicacionimpl`); `deleteById` sin `BEGIN/COMMIT TRANSACTION`; telemetría duplicada en `CrearAplicacionInteractorImpl`; mapper legacy clase en vez de enum |
| `funcionalidad` | Parcial | Mappers cruzados en `crearfuncionalidad/.../mapper/` (`CrearModuloDtoMapper`, `CrearParametroDtoMapper`) |
| `metadato` | Incompleto | Solo `CrearMetadatoDomain`; carpetas `primaryports/`, `secondaryports/`, `usecase/crearmetadato/` y `eliminarmetadato/` con solo `.gitkeep` |
| `modulo` | Parcial | Falta `eliminarmodulo`; mappers cruzados en `crearmodulo/.../mapper/` (`CrearFuncionalidadDtoMapper`, `CrearParametroDtoMapper`) |
| `organizacion` | Parcial | Typo `actualizarorganizidadimpl` (debe ser `actualizarorganizacionimpl`); `deleteById` sin `BEGIN/COMMIT`; mapper legacy clase |
| `parametro` | Completo | Mappers cruzados en `crearparametro/.../mapper/` (`CrearFuncionalidadDtoMapper`, `CrearModuloDtoMapper`) |
| `tipoparametro` | Solo consulta | Por diseño; `deleteById` sin `BEGIN/COMMIT` (sin use cases que lo usen) |

## 2. Reglas de verificación

1. **Dirección de dependencias**: `init → infraestructure → application → crosscutting`. `crosscutting` no importa a nadie. Buscar con `rg` imports prohibidos (p. ej. `application` importando `infraestructure`, `surrealdb` o `azure`).
2. **`infraestructure` solo importa interfaces de `application`**: nunca `...Impl` del núcleo.
3. **Controladores**: solo inyectan interactores y publishers; nunca repositorios ni `SurrealDbClient`. Devuelven `Mono<ResponseEntity<XxxResponse>>` con `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())`. `BusinessException` se relanza; `Exception` genérica → 500. SSE en `/events` con `Flux<ServerSentEvent<XxxEvent>>` y comentario `connected`.
4. **Interactores**: `XxxInteractorImpl` (`@Service`, `final`) que delega en el use case y usa `XxxDtoMapper.INSTANCE`.
5. **Use cases**: las mutaciones implementan `UseCaseWithOutReturn` (no existe `UseCaseWithReturn`), dependen de puertos (`XxxRepository`, `XxxPublisher`), telemetría con `recordBusinessOperation` y logs `[OPERACION-ENTIDAD]`. Las consultas NO tienen use case: el `Consultar<X>InteractorImpl` inyecta el `XxxRepository` directamente (patrón real).
6. **Mappers**: `enum XxxDtoMapper { INSTANCE; }`; métodos `toDtoInput`/`toDomain`. **Solo el mapper de la operación**; detectar mappers cruzados (`Crear<OtraEntidad>DtoMapper.java` dentro de `crear<entidad>/.../mapper/`).
7. **Dominios**: `XxxDomain extends Domain`, `create(...)`, setters privados, helpers (`TextHelper.applyTrim`, `UUIDHelper.getDefault`).
8. **Reglas**: `Xxx<Regla>Rule` + `Xxx<Regla>RuleImpl` (`@Service`); las que usan repositorio extienden `DomainRuleWithRepository`.
9. **Repositorios**: implementan el port, usan `SurrealDbClient` con `BEGIN TRANSACTION; ... COMMIT TRANSACTION;`, `escape()`, `firstStatementResult`, `toEntity`, `extractUuid` (y `extractDateTime`/`formatDateTime` si hay fechas). `TABLE_NAME` en plural. JSON: `tools.jackson.databind.JsonNode`.
10. **Publishers**: `@Component final`, `Sinks.Many<XxxEvent> sink = Sinks.many().replay().limit(100)`, `sendEvent` → `sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST)`, `getStream` → `sink.asFlux()`.
11. **GlobalExceptionHandler**: `@RestControllerAdvice`, inyección por constructor de `TelemetryService` + `ConsultarMensajePort`. Handlers: `DecodingException` (causa raíz → `ValidationException`/`InvalidFormatException` con `MSG-143`/`MSG-144`), `ValidationException` → 400, `NotFoundException` → 404, `ConflictException` → 409, `TechnicalException` → 500, `BusinessException` → 400. Logs `[EXCEPTION-HANDLER]` y `recordError`.
12. **Response base**: `Response` con `private final List<String> mensajes = new ArrayList<>()` (sin setter); `<Feature>Response extends Response` con lista de entidades.
13. **Excepciones**: solo las del proyecto (`ValidationException.build`, `NotFoundException.build`, `ConflictException.build`, `TechnicalException.build`). Mapa: 400/404/409/500.
14. **Entidades**: `application/secondaryports/entity/`; inmutables de estilo, `create(...)`, helpers.
15. **Reactividad y publishers**: `Publisher<T>` con `sendEvent` + `Flux<T> getStream()`; eventos `XxxEvent` con `EventType` y factories (`created`, `updated`, `deleted`).
16. **Sin dependencias circulares** entre features.
17. **Sin carpetas de operación vacías** (solo `.gitkeep`) cuando la operación está declarada en el controller/feature.
18. **Tabla definida en SurrealDB**: toda `TABLE_NAME` usada por un repositorio (`SurrealDb<Entidad>Repository`) debe estar definida en `SurrealDbClient.withDatabaseContext` como `DEFINE TABLE IF NOT EXISTS <tabla> SCHEMALESS;`. Si falta, REPROBAR (fallo en runtime "table not found").
19. **Mensajes MSG existentes**: todo `consultarMensajePort.consultarMensaje("MSG-xxx")` del código debe tener su entrada `MSG-xxx=...` en `src/main/resources/message.properties`. Si falta, REPROBAR (runtime `NotFoundException`). Buscar con `rg` todos los `"MSG-(\d+)"` en Java y contrastar con el `.properties`.

## 3. Procedimiento

1. Determina los archivos afectados (nuevos/modificados) de la tarea.
2. Verifica la estructura de paquetes con `Glob` (que existan TODOS los archivos del patrón de la operación; que no queden carpetas con solo `.gitkeep`).
3. Con `rg` busca imports/violaciones por regla. Para mappers cruzados, lista el contenido de cada `crear<entidad>/.../mapper/`.
4. Emite el informe.

## 4. Plantilla de informe

```
INFORME DE CUMPLIMIENTO ARQUITECTÓNICO
Ruta revisada: <lista de archivos>

Regla 1 [PASÓ]  Dirección de dependencias correcta
Regla 3 [REPROBÓ]  src/.../XxxController.java:42  -> inyecta XxxRepository; debe inyectar interactor
Regla 6 [REPROBÓ]  src/.../crearparametro/mapper/CrearFuncionalidadDtoMapper.java  -> mapper cruzado
Regla 11 [PASÓ]  GlobalExceptionHandler cumple patrón @RestControllerAdvice
...

DEUDA CONOCIDA (si aplica, no bloquea la tarea actual):
- src/.../actualizarorganizacion/usecase/actualizarorganizidadimpl/  -> typo
- src/.../eliminaraplicacion/usecase/eliminaraimpl/  -> typo
- application/features/metadato/  -> operación incompleta (solo CrearMetadatoDomain)
- application/features/modulo/  -> falta eliminarmodulo

CONCLUSIÓN: APROBADO | RECHAZADO (n violaciones)
CORRECCIONES: <por cada violación, archivo + fix concreto>
```

No modifiques código: reporta. El orquestador asigna las correcciones.