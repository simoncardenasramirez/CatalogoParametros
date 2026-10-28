# Vigencias y estados del catalogo

Solo se modifican estados que ya existen: activa en aplicaciones, activo en
modulos, funcionalidades y parametros, estadoToken en tokens e
idEstadoMetadatoAmbiente en las asociaciones parametro-ambiente.
No se agregan estados a organizaciones, ambientes compartidos ni metadatos.

## Comportamiento

- Un vencimiento desactiva el registro y los descendientes que tengan estado.
- Desactivar una aplicacion desactiva sus modulos, funcionalidades, parametros,
  asociaciones por ambiente y tokens. Otras aplicaciones y los ambientes
  compartidos conservan sus datos.
- Desactivar un modulo o funcionalidad afecta solo su rama. Desactivar un
  parametro desactiva sus asociaciones por ambiente.
- Reactivar un padre no reactiva automaticamente sus hijos.
- Se pueden consultar los inactivos y corregir su informacion/vigencia para
  reactivarlos individualmente. Para activar se necesitan padres disponibles.
  No se pueden crear nuevas relaciones hacia padres inactivos o fuera de vigencia.
- La consulta por token rechaza tokens expirados o de aplicaciones no disponibles
  y excluye parametros y asociaciones inactivos, incluso ante una cache anterior.

La vigencia es un intervalo [inicio, fin). Inicio es inclusivo y fin exclusivo.
Los limites nulos son abiertos en las entidades que ya los admitian. Un inicio
futuro bloquea el uso, pero no elimina una activacion programada. Los parametros
no adquieren fechas nuevas: heredan la disponibilidad de sus padres.

## UTC

Almacenamiento, cache, respuestas y comparaciones utilizan UTC. El reloj de
negocio es Clock.systemUTC() y las reglas comparan Instant. Los DTO reciben
fechas ISO-8601 con zona (Z u otro offset); se convierten al mismo instante UTC.
Los tokens dejan de usar LocalDateTime y exigen zona en las entradas nuevas.
El navegador convierte datetime-local a UTC y muestra fechas en la zona local,
conservando precision e instante si solo se edita otro campo.

La reconciliacion tambien canonicaliza las fechas persistidas anteriormente con
otros offsets, sin cambiar sus instantes. Los valores historicos sin zona se
interpretan como UTC (formato anterior de tokens del contenedor). La normalizacion
actualiza solo campos de fecha existentes, con comparacion del valor anterior para
respetar correcciones concurrentes. No altera estados de entidades que no los tienen.

## Ejecucion y arquitectura

El scheduler es un adaptador de entrada y llama a ControlEstadoCatalogo. El
trabajo corre en un hilo dedicado con capacidad de una ejecucion, separado de
las peticiones HTTP. fixedDelay espera un minuto tras finalizar el ciclo anterior:
no acumula ejecuciones si una revision tarda. La proyeccion de estados se lee
en una sola peticion a SurrealDB, sin cargar valores, nombres ni hashes de tokens.
Las notificaciones se agrupan por tipo para evitar una lectura por cada hijo.
Una edicion normal solo valida; la reconciliacion inmediata se dispara cuando
un padre cambia de activo a inactivo.

El interactor delega al caso de uso GestionarEstadosCatalogo; EvaluadorVigencia
contiene las reglas puras. La persistencia de desactivaciones usa un puerto
especializado y una transaccion SurrealDB para toda la cascada calculada.
Las estrategias de repositorio de aplicacion validan cada mutacion, serializan
el cambio con la reconciliacion dentro de la instancia y notifican tras persistir.

Tras los cambios se invalidan caches y se publican eventos UPDATED en los streams
SSE existentes. La pantalla de tokens actualiza su lista cada 30 segundos.
Las comprobaciones de uso consultan persistencia, sin depender del siguiente
ciclo ni de la disponibilidad de Redis. Si falla la reconciliacion se registra
el error y se reintenta en el siguiente ciclo.

Configuracion, en milisegundos:

| Propiedad | Valor predeterminado |
|---|---|
| catalogo.vigencia.habilitada | true |
| catalogo.vigencia.intervalo | 60000 |
| catalogo.vigencia.demora-inicial | 10000 |

El perfil test deshabilita el scheduler y el filtro PEP externo; las pruebas de
su contrato son aisladas. Las reglas se prueban con reloj fijo, sin SurrealDB,
Azure ni Redis reales.
