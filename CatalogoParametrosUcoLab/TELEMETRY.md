# Telemetría y Observabilidad

Este proyecto incluye telemetría completa con **Prometheus** + **Grafana** y **OpenTelemetry** para tracing distribuido.

## Arquitectura

```
┌─────────────┐     ┌──────────────┐     ┌───────────┐
│   App       │────▶│  Prometheus  │────▶│  Grafana  │
│ (Spring     │     │  (métricas)  │     │ (dashboards)│
│  Boot 4.0)  │     └──────────────┘     └───────────┘
│             │
│ Actuator    │     ┌──────────────┐
│ + Micrometer│────▶│  OpenTelemetry│
│ + OTel      │     │  (tracing)   │
└─────────────┘     └──────────────┘
```

## Componentes

### 1. Métricas (Prometheus + Micrometer)

**Dependencias en `pom.xml`:**
- `spring-boot-starter-actuator` — Endpoints de salud y métricas
- `micrometer-registry-prometheus` — Exportación de métricas a Prometheus

**Configuración en `application.properties`:**
```properties
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

**Endpoints disponibles:**
- `GET /actuator/health` — Estado de salud
- `GET /actuator/info` — Información de la app
- `GET /actuator/prometheus` — Métricas en formato Prometheus

### 2. Tracing (OpenTelemetry)

**Dependencias en `pom.xml`:**
- `spring-boot-starter-opentelemetry` — Bridge Micrometer/OpenTelemetry y exportador OTLP
- `aspectjweaver` — Instrumentación transversal de las capas internas

**Configuración en `application.properties`:**
```properties
management.tracing.sampling.probability=1.0
management.opentelemetry.tracing.export.otlp.endpoint=http://otel-collector:4317
management.opentelemetry.tracing.export.otlp.transport=grpc
management.opentelemetry.resource-attributes.service.name=catalogo-parametros
spring.reactor.context-propagation=auto
```

### Spans y `traceId` en WebFlux

Cada petición HTTP crea el span raíz automático de WebFlux. `LayerTracingAspect`
crea spans hijos para `interactor`, `usecase` y `repository`, por lo que Jaeger
muestra la duración propia de cada llamada y su relación padre-hijo. El span HTTP
incluye el tiempo total de controlador/petición.

La aplicación usa `Schedulers.boundedElastic()` en los controladores. Por eso
`spring.reactor.context-propagation=auto` es necesario: conserva el contexto de
OpenTelemetry y los valores MDC `traceId`/`spanId` al cambiar de hilo. Los logs de
arranque no pertenecen a una petición y es normal que allí ambos campos estén vacíos.

En Jaeger abre el servicio `catalogo-parametros`, ejecuta una petición a la API y
selecciona la traza: verás un span HTTP raíz y sus hijos con nombres como
`interactor.ActualizarAplicacionInteractorImpl.execute`,
`usecase.ActualizarAplicacionImpl.execute` y
`repository.SurrealDbAplicacionRepository.findById`.

### 3. Grafana

**Provisioning automático:**
- `grafana/provisioning/datasources/prometheus.yml` — Datasource de Prometheus
- `grafana/provisioning/dashboards/dashboards.yml` — Carga automática de dashboards
- `grafana/dashboards/spring-boot-metrics.json` — Dashboard pre-construido

**Paneles incluidos:**
- JVM Memory Used (Heap / Non-Heap)
- HTTP Server Requests (rate por método y status)
- JVM CPU / Peak Threads
- Process Uptime

### 4. Métricas de Negocio Personalizadas

El proyecto incluye un `TelemetryService` que registra:
- `business.operation.count` — Contador de operaciones por tipo
- `business.operation.duration` — Duración de operaciones
- `business.operation.errors` — Errores por tipo de operación

## Cómo ejecutar

### Opción A: Docker Compose (recomendado)

```bash
# 1. Compilar la aplicación
./mvnw clean package -DskipTests

# 2. Levantar stack completo
docker-compose up --build

# 3. Acceder a:
#    - App:       http://localhost:8080
#    - Prometheus: http://localhost:9090
#    - Grafana:    http://localhost:3000 (admin/admin)
```

### Opción B: Solo aplicación

```bash
# Ejecutar la app
./mvnw spring-boot:run

# Ver métricas
curl http://localhost:8080/actuator/prometheus
```

### Opción C: Con OpenTelemetry Collector

```bash
# Levantar collector para tracing
docker run -p 4317:4317 -p 4318:4318 \
  otel/opentelemetry-collector:latest \
  --config /etc/otel-collector-config.yml
```

## Uso en código

```java
// Inyectar el servicio de telemetría
@Autowired
private TelemetryService telemetry;

// Registrar una operación de negocio
telemetry.recordBusinessOperation("crear_aplicacion", durationNanos);

// Registrar un error
telemetry.recordBusinessError("crear_aplicacion", exception);
```

## Métricas personalizadas disponibles

| Métrica | Tipo | Descripción |
|---------|------|-------------|
| `business.operation.count` | Counter | Total de operaciones por tipo |
| `business.operation.duration` | Timer | Duración de operaciones en nanosegundos |
| `business.operation.errors` | Counter | Errores por tipo de operación |

## Alertas sugeridas en Grafana

1. **Alta tasa de errores** — `rate(business_operation_errors_total[5m]) > 0.1`
2. **Alta latencia** — `histogram_quantile(0.95, rate(business_operation_duration_seconds_bucket[5m])) > 1`
3. **Servicio caído** — `up{job="catalogo-parametros"} == 0`
