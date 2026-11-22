# Vigencias y estados del catalogo

Solo se modifican estados existentes: aplicaciones, modulos, funcionalidades,
parametros y asociaciones parametro-ambiente. No existen estados para tokens
internos porque ese mecanismo fue retirado.

Desactivar una aplicacion desactiva su rama de modulos, funcionalidades,
parametros y asociaciones por ambiente. Otras aplicaciones y los ambientes
compartidos conservan sus datos. Reactivar un padre no reactiva automaticamente
sus hijos.

La consulta M2M resuelve la aplicacion y el ambiente desde la identidad de
servicio asociada al `client_id`. Luego excluye parametros y asociaciones
inactivos o fuera de vigencia, incluso si existia una respuesta cacheada.

Las fechas se almacenan y comparan como `Instant` UTC. El intervalo de vigencia
es `[inicio, fin)`: inicio inclusivo y fin exclusivo.

Tras una mutacion se invalidan caches y se publican eventos `UPDATED` en los
streams SSE existentes. Las operaciones de reconciliacion de vigencias se
ejecutan en un hilo dedicado y reintentan en el siguiente ciclo si fallan.
