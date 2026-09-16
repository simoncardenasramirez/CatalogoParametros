# Consumo del Catálogo de Parámetros

El contrato público de este módulo es [`openapi/catalogo-parametros-v1.yaml`](openapi/catalogo-parametros-v1.yaml). Los equipos consumidores deben diseñar y generar sus clientes desde ese archivo, no desde clases Java internas ni desde la base de datos.

## Dirección y recursos

La URL base depende del ambiente y se antepone a `/catalogo-parametros/api/v1`. En local es `http://localhost:8080`.

| Recurso | Consulta | Escritura | Eventos SSE |
| --- | --- | --- | --- |
| Organizaciones | `GET /organizaciones`, `GET /organizaciones/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /organizaciones/events` |
| Aplicaciones | `GET /aplicaciones`, `GET /aplicaciones/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /aplicaciones/events` |
| Módulos | `GET /modulos`, `GET /modulos/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /modulos/events` |
| Funcionalidades | `GET /funcionalidades`, `GET /funcionalidades/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /funcionalidades/events` |
| Parámetros | `GET /parametros`, `GET /parametros/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /parametros/events` |
| Metadatos | `GET /metadatos`, `GET /metadatos/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` | `GET /metadatos/events` |
| Tipos de parámetro | `GET /tipos-parametro`, `GET /tipos-parametro/{id}` | Solo lectura | No |
| Tipos de metadato | `GET /tipos-metadato`, `GET /tipos-metadato/{id}` | Solo lectura | No |

Las colecciones paginadas aceptan `page` (desde 1) y `pageSize`; sus valores predeterminados son 1 y 10. La API responde siempre un objeto con `mensajes`. Las consultas agregan la colección correspondiente, por ejemplo `parametros`.

## Ejemplos

Consulta:

```sh
curl -sS 'http://localhost:8080/catalogo-parametros/api/v1/parametros?page=1&pageSize=10' \
  -H 'Accept: application/json'
```

Creación (los booleanos de entrada actualmente son cadenas por compatibilidad con los DTO existentes):

```sh
curl -sS -X POST 'http://localhost:8080/catalogo-parametros/api/v1/parametros' \
  -H 'Content-Type: application/json' \
  -d '{
    "nombre": "idioma",
    "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174001",
    "idTipoParametro": "123e4567-e89b-12d3-a456-426614174002",
    "activo": "true"
  }'
```

Eventos:

```sh
curl -N 'http://localhost:8080/catalogo-parametros/api/v1/parametros/events' \
  -H 'Accept: text/event-stream'
```

El nombre SSE (`event:`) identifica el recurso y el JSON de `data:` incluye el recurso y `event`, cuyos valores son `CREATED`, `UPDATED` o `DELETED`. El flujo es en vivo: no garantiza replay, persistencia ni reanudación de eventos perdidos. Para sincronización inicial, primero consulte el recurso REST y después mantenga el flujo SSE.

## Errores

| Código | Significado | Acción del consumidor |
| --- | --- | --- |
| 400 | Formato inválido o regla de negocio | Corregir la petición; revisar `mensajes` |
| 404 | Identificador inexistente | No reintentar sin cambiar el identificador |
| 409 | Conflicto o recurso en uso | Resolver el estado indicado en `mensajes` |
| 500 | Fallo técnico | Registrar y reintentar con backoff limitado |

## Flujo contract-first

1. Productor y consumidores acuerdan primero el cambio en `catalogo-parametros-v1.yaml`.
2. Se valida el YAML y se revisa su compatibilidad antes de modificar controladores o clientes.
3. Los consumidores generan tipos, stubs o mocks usando `operationId` como nombre estable.
4. El productor implementa el comportamiento y ejecuta pruebas contra el contrato.
5. El contrato aprobado se publica como artefacto versionado junto con el servicio.

Dentro de `v1` solo se permiten cambios compatibles: agregar operaciones, respuestas o propiedades opcionales. Renombrar/eliminar rutas o campos, cambiar tipos, volver obligatorio un campo opcional o cambiar su semántica requiere una nueva versión mayor (`/api/v2`) y un periodo de convivencia.

La especificación generada dinámicamente en `/v3/api-docs` sirve para detectar divergencias, pero no reemplaza este archivo: el archivo versionado es la fuente de verdad.
