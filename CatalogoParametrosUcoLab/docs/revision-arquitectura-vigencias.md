# Revision de arquitectura: vigencias y UTC

Resultado: **APROBADO** para los cambios de esta implementacion.

| Regla | Resultado y evidencia |
|---|---|
| Dependencias hacia adentro | application no importa infraestructure/init; crosscutting no importa capas superiores. |
| Adaptadores dependen de contratos | Scheduler recibe ControlEstadoCatalogo; estrategias usan puertos; persistencia/publicador reciben interfaces de repositorio y publicadores. No se importan implementaciones del nucleo desde infraestructura. |
| Entrada y negocio separados | ControlEstadoCatalogoInteractorImpl delega a GestionarEstadosCatalogo. EvaluadorVigencia contiene las reglas puras de estado, tiempo y jerarquia. |
| Persistencia y cache | EstadoCatalogoRepository separa proyeccion, normalizacion UTC y escritura. Los repositorios BaseDatos evitan recursion por las estrategias cacheadas. Token sigue la misma separacion. |
| Transacciones y escape | La cascada se confirma en una transaccion SurrealDB; conserva columnas ajenas al estado. La normalizacion compara valores anteriores y escapa cadenas. Las demas interpolaciones son UUID, Instant o literales internos. |
| Inmutabilidad e inyeccion | Nuevas proyecciones son records inmutables; campos de servicios/adaptadores son final con inyeccion por constructor. Mapper enum INSTANCE. |
| Excepciones y telemetria | Se usan ValidationException/TechnicalException del proyecto. El caso de uso registra sincronizar-vigencias y logs VIGENCIA-CATALOGO. El scheduler captura fallos y reintenta en el siguiente ciclo. |
| HTTP/SSE | No se agregan rutas ni acceso a repositorios en controllers. Se reutilizan los eventos UPDATED y streams SSE existentes. La consulta por token respeta disponibilidad aunque haya cache anterior. |
| Sin cambios de esquema de estados | OrganizacionEntity, AmbienteEntity y MetadatoEntity no reciben estados nuevos. Las organizaciones solo normalizan sus fechas por el contrato UTC. |
| UTC | Clock.systemUTC, comparacion con Instant, entidades/DTO internos normalizados y tokens OffsetDateTime. LocalDateTime solo se usa en el helper de lectura historica con UTC explicito. |
| Frontend | Reglas auxiliares en shared/utils; HTTP/SSE existentes; componentes conservan Reactive Forms. Selectores bloquean opciones no disponibles y tokens liberan la suscripcion periodica al destruirse. |
| Pruebas | Suite backend 1176/1176, Angular 207/207; reglas con reloj fijo y adaptadores simulados, sin servicios externos. Incluye filtrado de inactivos por token, consultas agrupadas y ejecutor dedicado. Prueba operativa adicional de SQL en SurrealDB temporal en memoria: cascada, UTC y aislamiento de otra aplicacion. |

La feature de vigencia no crea un CRUD ni un endpoint: sus datos de entrada son
proyecciones internas del catalogo. Por ello no introduce DTO HTTP, controller ni
dominios CRUD vacios. Los esquemas/contratos HTTP existentes se conservan y sus
reglas de disponibilidad/UTC quedan documentadas.
