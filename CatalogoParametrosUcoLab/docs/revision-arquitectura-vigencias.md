# Revision de arquitectura: vigencias y UTC

Resultado: **APROBADO** para la reconciliacion de vigencias y la consulta M2M.

| Regla | Resultado |
| --- | --- |
| Dependencias | `application` depende de puertos; los adaptadores implementan esos contratos. |
| Persistencia | La proyeccion de estados y la cascada de desactivacion se ejecutan mediante repositorios y transacciones SurrealDB. |
| UTC | Las fechas se comparan con `Clock.systemUTC()` e `Instant`; el intervalo es `[inicio, fin)`. |
| HTTP/SSE | Los controllers no acceden a repositorios. Se reutilizan eventos `UPDATED` y streams SSE. |
| Consulta M2M | La identidad de servicio se resuelve por `client_id`; la asociacion determina aplicacion y ambiente antes de filtrar vigencia y estado. |
| Frontend | El microfrontend conserva formularios reactivos, exige ambiente cuando corresponde y libera SSE al destruirse. |

La vigencia no crea endpoints CRUD adicionales: alimenta las reglas existentes
de administracion y la consulta de parametros para consumidores autorizados.
