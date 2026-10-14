# Cache Redis del catalogo de parametros

Se replica el flujo de `proyecto mensajes/NosGraduamos-FC`: estrategia en el nucleo,
puerto independiente para cache y adaptador Redis en infraestructura. No se usan
anotaciones `@Cacheable` ni se acoplan los controllers a Redis.

```text
Interactor / caso de uso / regla
    -> ParametroRepository
        -> ParametroCatalogoStrategy
            -> CacheParametroRepository -> RedisParametroRepository -> Redis
            -> BaseDatosParametroRepository -> SurrealDbParametroRepository -> SurrealDB
```

El alcance incluye todas las entidades del catalogo: parametros, organizaciones,
aplicaciones, modulos, funcionalidades, metadatos, tipos de parametro, tipos de
metadato, ambientes, estados de ambiente, metadatos por ambiente y estados de
metadato por ambiente. Cada entidad tiene su propia estrategia, puerto de cache,
puerto de base de datos, adaptador Redis y mapper JSON explicito. Los
mensajes del backend siguen usando su adaptador existente; el fallback en memoria
de mensajes tecnicos del proyecto de referencia no corresponde a datos de parametros.

Una lectura consulta Redis primero. Si falta la clave, consulta SurrealDB y guarda
el resultado, incluyendo listas vacias y parametros inexistentes. Un acierto evita
otra consulta a SurrealDB. Se cubren las consultas por ID y todos los listados,
paginas y filtros que expone el puerto de cada entidad. La unicidad por nombre
y las comprobaciones de relaciones (`existsBy...`) se comprueban siempre en SurrealDB.
Los mappers conservan UUIDs, estados, fechas con zona horaria y valores JSON de metadatos.

Crear, actualizar, eliminar y cambiar estado confirman primero la escritura en
SurrealDB y luego cambian la version de cache de la entidad modificada. Esto invalida
todos sus IDs, paginas y filtros, en todas las instancias del backend, sin invalidar
las otras entidades. Tipo de metadato solo expone consultas; sus cambios externos
dependen del TTL. No se ejecutan
`KEYS`, `FLUSHDB` ni barridos; los datos de versiones anteriores expiran por TTL.
Las consultas en curso guardan bajo su version original, evitando repoblar una
version nueva con resultados anteriores a una mutacion.

Si Redis falla o el JSON no es valido, la lectura usa SurrealDB. Un fallo de cache
no convierte una escritura ya confirmada en un error HTTP. Si la invalidacion
falla y Redis recupera sus datos antiguos, estos pueden permanecer hasta su TTL
(5 minutos por defecto). Las escrituras externas que omitan la estrategia tambien
dependen del TTL. Esta cache ofrece consistencia eventual, no transacciones
distribuidas entre Redis y SurrealDB.

## Ejecucion

Desde la raiz del backend:

```powershell
docker compose up -d redis
.\mvnw.cmd spring-boot:run
```

Para ejecutar todo el stack: `docker compose up --build`.
Compose configura `REDIS_HOST=redis` en el backend; fuera de Docker el host por
defecto es `localhost`. Redis usa el volumen `redis-data`, AOF y un healthcheck.
El puerto publicado se limita a `127.0.0.1`.

| Variable | Valor por defecto | Proposito |
| --- | --- | --- |
| `REDIS_HOST` | `localhost` | Host fuera de Compose |
| `REDIS_PORT` | `6379` | Puerto local; Compose usa 6379 dentro de la red |
| `REDIS_PASSWORD` | vacio | Contrasena compartida por Redis y el backend |
| `CACHE_HABILITADA` | `true` | Permite deshabilitar la cache sin modificar codigo |
| `CACHE_TTL` | `5m` | Expiracion de los resultados; debe ser positiva |
| `CACHE_PREFIJO` | `catalogo-parametros` | Aisla aplicaciones/ambientes que comparten Redis |

Configurar valores privados en `.env` (ignorado por Git). Para ambientes que
comparten Redis usar prefijos diferentes. No se necesita un secreto nuevo en Azure
para ejecutar la cache local. Los timeouts de conexion y operaciones son de 1 segundo.

## Verificacion

```powershell
.\mvnw.cmd test '-Dtest=*CatalogoStrategyTest,Redis*RepositoryTest,Redis*WiringTest'
.\mvnw.cmd test
docker compose config --quiet
```

Las pruebas de cache usan mocks; verifican aciertos, fallos, TTL, serializacion,
invalidacion, concurrencia y resolucion de puertos sin servicios externos.

Revision de arquitectura: APROBADO para estos cambios. El nucleo solo conoce
puertos y entidades; Redis, Jackson y sus propiedades permanecen en infraestructura.
Los contratos HTTP, validadores, telemetria de los casos de uso y publishers SSE
siguen pasando por los puertos existentes.
