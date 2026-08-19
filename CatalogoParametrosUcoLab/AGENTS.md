# AGENTS.md — CatalogoParametrosUcoLab

Guía de arquitectura y convenciones para cualquier agente (humano o IA) que trabaje en este repositorio.

## 1. Resumen del proyecto

Backend REST reactivo construido con **Java 21**, **Spring Boot 4.0.6 (WebFlux)** y **Maven**. Base de datos **SurrealDB**, secretos desde **Azure Key Vault** y observabilidad completa con **Prometheus / Grafana / OpenTelemetry / Loki**. Incluye un frontend Angular en `../catalogo-parametros-front`.

Paquete raíz: `co.edu.uco.CatalogoParametrosUcoLab`.

## 2. Arquitectura hexagonal (puertos y adaptadores)

El proyecto está organizado en **cuatro capas** bajo `src/main/java/co/edu/uco/CatalogoParametrosUcoLab/`. La regla de dependencia es **unidireccional y hacia adentro**: `init → infraestructure → application → crosscutting`. `crosscutting` no depende de nada.

```
┌───────────────────────────────────────────────────────────────┐
│  init/                      Bootstrap de la aplicación        │
├───────────────────────────────────────────────────────────────┤
│  infraestructure/           Adaptadores (driving + driven)    │
│    config/                  Configuración Spring              │
│    primaryadapters/         Controladores REST, responses,    │
│                             exception handler                 │
│    secondaryadapters/       Repositorios SurrealDB,           │
│                             publishers, mensajes, secretos,   │
│                             cliente SurrealDB                 │
├───────────────────────────────────────────────────────────────┤
│  application/               Núcleo (dominio + casos de uso)   │
│    primaryports/            Puertos de entrada (interactores) │
│    secondaryports/          Puertos de salida (contratos)     │
│    usecase/                 Base de casos de uso y reglas     │
│    common/telemetry/        TelemetryService                  │
│    features/<feature>/      Módulos por feature y operación   │
├───────────────────────────────────────────────────────────────┤
│  crosscutting/              constants, exceptions, helpers    │
└───────────────────────────────────────────────────────────────┘
```

### 2.1 `init/`

- `CatalogoParametrosUcoLabApplication.java` — única clase `@SpringBootApplication` con `scanBasePackages = "co.edu.uco.CatalogoParametrosUcoLab"`. No contiene lógica de negocio.

### 2.2 `application/` (el núcleo)

Contiene la lógica de negocio y los **puertos** (interfaces). No depende de Spring (excepto anotaciones `@Service` por convención) ni de SurrealDB/Azure.

**Puertos genéricos:**
- `primaryports/InteractorWithOutReturn<T>.java` → `void execute(T data)`
- `primaryports/InteractorWithReturn<T,R>.java` → `R execute(T data)`
- `secondaryports/repository/*.java` → contratos por entidad (`AplicacionRepository`, `ParametroRepository`, ...)
- `secondaryports/publisher/Publisher<T>.java` → `sendEvent(T)` + `Flux<T> getStream()`
- `secondaryports/message/ConsultarMensajePort.java` → mensajes por código (MSG-xxx)
- `secondaryports/secret/SecretVaultPort.java` → secretos de Azure
- `secondaryports/entity/*.java` → entidades de persistencia (vivir aquí, en el núcleo)
- `usecase/UseCaseWithOutReturn<T>.java`, `usecase/UseCaseWithReturn<T,R>.java`
- `usecase/domain/Domain.java`, `usecase/domain/rule/DomainRule.java`, `usecase/domain/rule/DomainRuleWithRepository.java`
- `usecase/validator/RuleValidator<T>.java` → `void validate(T data)`

**Features:** `application/features/<feature>/` con features `aplicacion`, `funcionalidad`, `metadato`, `modulo`, `organizacion`, `parametro`, `tipoparametro`. Cada feature se subdivide en **operaciones** (`crear<feature>`, `actualizar<feature>`, `consultar<feature>`, `eliminar<feature>`), salvo `tipoparametro` y `metadato` que hoy solo tienen consulta/creación.

Estructura por operación (patrón fijo — ver skill `hexagon-scaffolder`):

```
<operacion>/                            ej: crearparametro/
├── Crear<Feature>.java                 Interfaz del caso de uso (extends UseCaseWithOutReturn<XxxDomain>)
├── Crear<Feature>RuleValidator.java    Interfaz del validador (extends RuleValidator<XxxDomain>)
├── primaryports/
│   ├── dto/Crear<Feature>DtoRequest.java   DTO que llega de la capa de infraestructura
│   ├── dto/Crear<Feature>DtoInput.java     DTO interno normalizado
│   ├── interactor/Crear<Feature>Interactor.java       Puerta de entrada
│   ├── interactor/impl/Crear<Feature>InteractorImpl.java  (@Service)
│   └── interactor/mapper/Crear<Feature>DtoMapper.java   (enum INSTANCE, MapStruct manual)
├── secondaryports/
│   ├── event/Crear<Feature>Event.java      Evento de dominio
│   └── publisher/Crear<Feature>Publisher.java  (extends Publisher<XxxEvent>)
└── usecase/
    ├── crear<feature>impl/Crear<Feature>Impl.java            (@Service)
    ├── crear<feature>impl/Crear<Feature>RuleValidatorImpl.java
    ├── domain/Crear<Feature>Domain.java                       (extends Domain)
    └── domain/rules/<XxxRule>.java (+ impl/)
```

### 2.3 `infraestructure/` (los adaptadores)

- `config/WebConfig.java`
- `primaryadapters/controller/<feature>/<Feature>Controller.java` — controladores REST, solo mapeo HTTP → interactor
- `primaryadapters/response/<feature>/<Feature>Response.java` + `response/Response.java` (base con `mensajes`)
- `primaryadapters/exceptionhandler/GlobalExceptionHandler.java` — `@RestControllerAdvice`
- `secondaryadapters/repository/<feature>/SurrealDb<Feature>Repository.java` — implementa el port, usa `SurrealDbClient`
- `secondaryadapters/publisher/<feature>/<operacion>/<Operacion>PublisherImpl.java`
- `secondaryadapters/message/ConsultarMensajeAdapter.java`
- `secondaryadapters/secret/azure/` — AzureKeyVaultConfig, AzureKeyVaultProperties, AzureKeyVaultSecretAdapter
- `secondaryadapters/surrealdb/` — SurrealDbClient, SurrealDbProperties

### 2.4 `crosscutting/`

- `constants/Constants.java`
- `exceptions/` — jerarquía: `BusinessException` (abstracta) → `ConflictException`, `NotFoundException`, `ValidationException`; más `TechnicalException`
- `helpers/` — `PropertiesHelper`, `TextHelper` (p.ej. `applyTrim`), `UUIDHelper` (`getDefault()`)

## 3. Reglas de dependencia (no negociables)

1. `infraestructure` depende de **interfaces** de `application` (puertos), nunca de clases concretas del núcleo.
2. `application` **no importa** nada de `infraestructure` ni de SurrealDB/Azure.
3. Los controladores **no acceden a repositorios ni a la base de datos**; solo inyectan interactores y publishers.
4. Los casos de uso dependen de puertos (`XxxRepository`, `XxxPublisher`), no de adaptadores.
5. `crosscutting` es la única capa que puede ser importada por todas; nunca importa a nadie.
6. No hay dependencias circulares entre features.

## 4. Convenciones de código

- **Idioma**: nombres de clases, métodos, dominios y mensajes en **español**. Tests: `debe<Escenario>Cuando<Condición>()`.
- **Naming**: `CrearX`, `ActualizarX`, `ConsultarX`, `EliminarX`; implementaciones `XImpl`; reglas `X<Regla>Rule` + `X<Regla>RuleImpl`; mappers enum con `INSTANCE`.
- **Inmutabilidad**: campos `final`, inyección por **constructor** (sin Lombok). Los dominios usan `create(...)` con setters privados y helpers de validación (`TextHelper.applyTrim`, `UUIDHelper.getDefault`).
- **Mappers**: `enum XxxDtoMapper { INSTANCE; }` con `toDtoInput`, `toDomain`, etc.
- **Reactividad**: WebFlux; controladores devuelven `Mono<ResponseEntity<XxxResponse>>` con `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())`. SSE en `GET .../events` con `Flux<ServerSentEvent<XxxEvent>>`.
- **Excepciones**: lanzar siempre excepciones del proyecto (`ValidationException`, `NotFoundException`, `ConflictException`, `TechnicalException`). Nunca `Exception` genérica para negocio.
- **Telemetría**: todo caso de uso registra la operación con `TelemetryService.recordBusinessOperation("operacion", () -> {...})` y logs con un prefijo `[CREAR-PARAMETRO]`-style.
- **Repositorios**: extienden la lógica de `SurrealDbClient` con transacciones `BEGIN TRANSACTION; ... COMMIT TRANSACTION;`, escapan strings con `escape()` y convierten `JsonNode` → entidad con `toEntity()`.

## 5. Comandos

Desde `CatalogoParametrosUcoLab/` (raíz del backend):

| Comando | Propósito |
|---|---|
| `.\mvnw.cmd compile` | Compilar |
| `.\mvnw.cmd test` | Ejecutar tests |
| `.\mvnw.cmd test -Dtest=<ClaseTest>` | Test específico |
| `.\mvnw.cmd clean package -DskipTests` | Empaquetar JAR |
| `.\mvnw.cmd spring-boot:run` | Ejecutar app |
| `docker compose up --build` | Levantar stack completo |

Stack de contenedores (docker-compose.yml): `app`, `prometheus` (:9090), `grafana` (:3000 admin/admin), `surrealdb` (:8000 root/root), `otel-collector`, `jaeger` (:16686), `loki` (:3100), `promtail`.

Frontend (Angular 17): `cd ../catalogo-parametros-front && npm install && npm start`.

## 6. Estructura de tests

Tests en `src/test/java/co/edu/uco/CatalogoParametrosUcoLab/` (JUnit 5, `spring-boot-starter-test`). Hoy existen:
- `CatalogoParametrosUcoLabApplicationTests` — carga el contexto (`@SpringBootTest`).
- `crosscutting/helpers/PropertiesHelperTest` — helpers con `assertEquals`/`assertThrows`.

## 7. Agentes y skills (.opencode)

Ver `.opencode/` para el orquestador, sub-agentes y skills disponibles. El orquestador es el agente por defecto (`default_agent`).