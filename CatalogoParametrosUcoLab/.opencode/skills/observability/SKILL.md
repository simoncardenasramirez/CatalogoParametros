---
name: observability
description: Guía de telemetría y observabilidad de CatalogoParametrosUcoLab: contadores/timers de negocio con TelemetryService, exportación Prometheus y OTLP, dashboards de Grafana, tracing con OpenTelemetry/Jaeger y logs con Loki/promtail. Úsala para añadir métricas de negocio, modificar el stack de monitoreo (docker-compose, prometheus/, grafana/, otel-collector-config.yaml, promtail-config.yaml) o resolver problemas de observabilidad.
---

# Observability Skill

Telemetría y monitoreo del backend `CatalogoParametrosUcoLab`. La doc oficial vive en `TELEMETRY.md`.

## 1. Stack real

- **Métricas**: Micrometer → Prometheus (`/actuator/prometheus`) y OTLP → OpenTelemetry Collector.
- **Tracing**: OpenTelemetry → `otel-collector` → Jaeger (`:16686`).
- **Logs**: promtail → Loki (`:3100`), consultables en Grafana (`:3000` admin/admin).
- **Dashboards**: `grafana/provisioning/` (datasources + dashboards).

| Servicio | Puerto |
|---|---|
| App Spring Boot | 8080 |
| Prometheus | 9090 |
| Grafana | 3000 |
| SurrealDB | 8000 |
| OTEL Collector (gRPC/HTTP) | 4317 / 4318 |
| Jaeger | 16686 |
| Loki | 3100 |
| promtail | agente |

## 2. Métricas de negocio (TelemetryService real)

| Método | Métrica |
|---|---|
| `recordBusinessOperation("op", runnable/supplier)` | `business.operation.count{operation="op"}` |
| `recordBusinessError("op", throwable)` | `business.operation.errors{operation="op", error="SimpleName"}` |
| `recordError("tipo", "mensaje")` | `application.error.count{errorType="tipo"}` |
| `startOperationTimer()` / `stopOperationTimer(sample,"op")` | `business.operation.duration{operation="op"}` |

- Todo use case de mutación envuelve su lógica en `recordBusinessOperation(OPERATION_NAME, () -> {...})` con `OPERATION_NAME = "<operacion>-<entidad>"` y logs `[OPERACION-ENTIDAD]`.
- `GlobalExceptionHandler` registra `recordError("tipo-excepcion", msg)` con tipos `validation-exception`, `not-found-exception`, `conflict-exception`, `technical-exception`, `decoding-exception`; logs `[EXCEPTION-HANDLER]`.

Reglas al añadir métricas nuevas:
- Usar `MeterRegistry` (inyectado por constructor en un `@Service`).
- `Counter.builder(...)`/`Timer.builder(...)` con `.tag("operation", nombre)`.
- No romper la regla: `application` no depende de infraestructura (Micrometer es dependencia de Spring Boot, permitida).

## 3. Endpoints Actuator

- `GET /actuator/health`, `GET /actuator/info`, `GET /actuator/prometheus`.
- Config mínima que debe mantenerse: `management.endpoints.web.exposure.include=health,info,prometheus`, `management.metrics.export.prometheus.enabled=true`.
- Nombres en Prometheus: `camelCase` → `snake_case` + `_count` (p. ej. `business.operation.count` → `business_operation_count`).

## 4. Cambios en el stack

- `docker-compose.yml` — servicios `app`, `prometheus`, `grafana`, `surrealdb`, `otel-collector`, `jaeger`, `loki`, `promtail`; red `monitoring`; variables `MANAGEMENT_*`, `OTEL_*`.
- `prometheus/prometheus.yml` — jobs de scrape (app en `:8080/actuator/prometheus`).
- `otel-collector-config.yaml` — receivers/exporters OTLP y Prometheus.
- `promtail-config.yaml` — pipeline de logs Docker → Loki.

## 5. Verificación

- `curl http://localhost:8080/actuator/prometheus | Select-String "business_operation_count"` (comprobar contadores).
- `docker compose ps` para estados de servicios.
- Grafana → Explore (Loki) / dashboards (Prometheus).
- No exponer secretos en dashboards ni configs versionadas.