# Guía de integración de la API — Catálogo de Parámetros UcoLab

Esta guía explica cómo consumir las 68 operaciones de los 12 recursos de la API v1. Describe el código actual y complementa el [contrato OpenAPI](catalogo-parametros-v1.yaml), revisión 1.1.0. Los ejemplos son ilustrativos; no son respuestas obtenidas de un servidor en ejecución.

## Inicio rápido

- Host local: `http://localhost:8080`. Sustituirlo por el host de cada despliegue.
- URL base de negocio: `http://localhost:8080/catalogo-parametros/api/v1`.
- Solicitudes con cuerpo: `Content-Type: application/json`.
- Consultas y escrituras: `Accept: application/json`. Eventos: `Accept: text/event-stream`.
- Los controladores actuales no declaran autenticación ni cabeceras de API key. Si el despliegue añade un gateway, aplicar también sus requisitos de acceso.
- El CORS actual permite el origen `http://localhost:4200`. Otro frontend en navegador requiere configurar su origen en el backend; los clientes entre servidores no dependen de CORS.

Los ejemplos de cURL usan sintaxis de Bash. En Windows puede usarse la colección Postman o `curl.exe` adaptando el entrecomillado. Los UUID de ejemplo son marcadores: reemplazarlos por IDs reales, distintos para cada recurso cuando corresponda. No enviar `{id}` literalmente.

## Postman y contrato para otros componentes

Importar [catalogo-parametros.postman_collection.json](catalogo-parametros.postman_collection.json). Contiene las 68 solicitudes agrupadas por recurso. Configurar `baseUrl` (solo host, sin barra final) y las variables `idOrganizacion`, `idAplicacion`, `idModulo`, `idFuncionalidad`, `idParametro`, `idMetadato`, `idTipoParametro`, `idTipoMetadato`, `idAmbiente`, `idEstadoAmbiente`, `idEstadoMetadatoAmbiente` e `idMetadatoAmbiente` con registros reales. No ejecutar toda la colección como un flujo automático: contiene eliminaciones y conexiones SSE continuas.

El YAML OpenAPI puede importarse en herramientas de documentación o generación de clientes. Swagger UI local: `http://localhost:8080/swagger-ui.html`; lee el contrato generado en `/v3/api-docs`. El contrato versionado está en este directorio y se publica en `/openapi/catalogo-parametros-v1.yaml` al empaquetar.

## Flujo de integración y relaciones

1. Consultar los catálogos `/tipos-parametro` y `/tipos-metadato`; conservar sus UUID. No existen endpoints de escritura para estos dos catálogos.
2. Crear una organización, luego una aplicación con `idOrganizacion`, un módulo con `idAplicacion` y una funcionalidad con `idModulo`.
3. Crear un parámetro con `idFuncionalidad` e `idTipoParametro`.
4. Crear un metadato con `idParametro`, `idTipoMetadato` y `valor`. El tipo JSON de `valor` debe corresponder al tipo de metadato consultado; el ejemplo de texto requiere un tipo de texto.
5. Para asociar un parámetro a un ambiente, crear o consultar un ambiente y un estado de metadato de ambiente; enviar sus IDs a `/metadatos-ambiente`.

Las escrituras de organizaciones, aplicaciones, módulos, funcionalidades, parámetros y metadatos no devuelven el registro ni su ID. Tras crear, consultar la colección y localizar el registro por sus datos y relaciones, recorriendo las páginas cuando aplique. El contrato no ofrece un filtro por nombre ni un total; ante escrituras concurrentes, evitar asumir que el último registro es el recién creado. Ambientes, estados de ambiente, estados de metadato de ambiente y metadatos de ambiente sí devuelven el registro creado o actualizado.

`MetadatoAmbiente` relaciona parámetro, ambiente y estado de metadato de ambiente. No contiene `valor` ni `idTipoMetadato`. El catálogo `EstadoAmbiente` es independiente: el DTO actual de ambiente no incluye una referencia a él.

## Reglas comunes

- Los IDs son UUID en texto. El ID del recurso se envía en la ruta al consultar, actualizar o eliminar; no se envía como campo del cuerpo de creación.
- En los cuerpos de los seis recursos originales y de metadatos de ambiente, enviar todos los campos mostrados excepto las fechas opcionales. Para ambientes y los dos catálogos de estados, enviar `nombre`; su validación actual se detalla por separado.
- `PUT` recibe el DTO completo mostrado para el recurso. No hay `PATCH` ni contrato de actualización parcial.
- `activo`/`activa` en crear y actualizar aplicaciones, módulos, funcionalidades y parámetros se envían como cadenas `"true"` o `"false"`. En las respuestas son booleanos. En `/cambiarestado`, enviar `{"activo": false}` como booleano, incluso para aplicaciones.
- Los nombres de organizaciones, aplicaciones, módulos, funcionalidades y parámetros admiten entre 3 y 50 caracteres después de recortar espacios. Los catálogos de ambientes y estados recortan espacios y validan duplicados, pero actualmente no aplican esa restricción de longitud u obligatoriedad; enviar nombres descriptivos.
- `fechaInicio` y `fechaFinal` son opcionales para organizaciones, aplicaciones, módulos y funcionalidades. Enviar ISO-8601 con zona (`2026-10-01T08:00:00-05:00`) o cadena vacía; si existen ambas, la final no puede preceder a la inicial. Las respuestas pueden contener `null` o una fecha normalizada a otra zona.
- Los recursos referenciados deben existir. Las reglas de duplicidad y eliminación pueden producir conflictos. Desactivar mediante `/cambiarestado` y eliminar mediante `DELETE` son operaciones diferentes.

## Paginación y filtros

Las colecciones salvo metadatos y los dos catálogos de tipos aceptan `page=1&pageSize=10`. La primera página es 1; valores inferiores a 1 se normalizan a 1. No hay `total`, `totalPages` ni enlace de siguiente página. Para recorrer registros, incrementar `page` hasta obtener una colección vacía; los cambios concurrentes pueden alterar el recorrido.

`GET /metadatos` devuelve todos sin paginación; permite `?idParametro=<UUID>` para filtrar. Los otros recursos no declaran filtros por sus relaciones: no asumir que `idAplicacion`, `idModulo` o `idAmbiente` funcionan como filtros de consulta.

## Respuestas y errores

Las consultas devuelven un objeto con `mensajes` y una colección, incluso al consultar un solo UUID. Consumir la colección correspondiente; no esperar una entidad directamente en la raíz. Una lista vacía es una consulta válida. Consultar un UUID inexistente produce 404 conforme a las reglas del recurso.

| Código | Interpretación | Acción del consumidor |
| --- | --- | --- |
| 200 | Consulta, actualización, eliminación o cambio de estado exitoso | Leer colección y/o mensajes |
| 201 | Creación exitosa | Obtener ID de la entidad devuelta o consultar colección |
| 400 | Formato inválido o validación | Corregir datos antes de reenviar |
| 404 | Recurso o referencia inexistente | Revisar IDs y existencia de relaciones |
| 409 | Conflicto de negocio | Revisar duplicados o dependencias |
| 500 | Fallo técnico | Registrar contexto y aplicar recuperación según la operación |

Ejemplo ilustrativo de error global:

```json
{"mensajes": ["Descripción de la validación o del error"]}
```

Algunos controladores añaden también una colección vacía en errores. Organización convierte además ciertas excepciones técnicas capturadas en 400. El consumidor debe comprobar el estado HTTP y tolerar ambos formatos. Los mensajes son descriptivos; no existe un código de error estable en el cuerpo para tomar decisiones por texto.

Las escrituras de módulos y funcionalidades devuelven actualmente `parametros: []`, aunque sus consultas devuelven `modulos` y `funcionalidades`. Las otras escrituras originales devuelven su propia colección vacía. `DELETE` responde 200 con cuerpo, no 204. No hay clave de idempotencia declarada: ante un timeout de creación, comprobar si el registro existe antes de repetir.

## Índice de recursos

- [Organizaciones](#organizaciones)
- [Aplicaciones](#aplicaciones)
- [Módulos](#modulos)
- [Funcionalidades](#funcionalidades)
- [Parámetros](#parametros)
- [Metadatos](#metadatos)
- [Tipos de parámetro](#tipos-parametro)
- [Tipos de metadato](#tipos-metadato)
- [Ambientes](#ambientes)
- [Estados de ambiente](#estados-ambiente)
- [Estados de metadato de ambiente](#estados-metadato-ambiente)
- [Metadatos de ambiente](#metadatos-ambiente)

<a id="organizaciones"></a>

## Organizaciones

Ruta: `/catalogo-parametros/api/v1/organizaciones`. Colección de consulta: `organizaciones`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |
| `fechaInicio` | string | Opcional; fecha con zona o cadena vacía |
| `fechaFinal` | string | Opcional; fecha con zona o cadena vacía |

### Listar — `GET /catalogo-parametros/api/v1/organizaciones`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/organizaciones?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "organizaciones": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "UCO",
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/organizaciones`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/organizaciones" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "UCO", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/organizaciones/{id}`

`id`: UUID del registro de organizaciones que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/organizaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "organizaciones": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "UCO",
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/organizaciones/{id}`

`id`: UUID del registro de organizaciones que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/organizaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "UCO", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/organizaciones/{id}`

`id`: UUID del registro de organizaciones que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/organizaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/organizaciones/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/organizaciones/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `organizacion`. Ver las reglas SSE al final.

```text
event: organizacion
data: {"organizacion": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "UCO", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```

<a id="aplicaciones"></a>

## Aplicaciones

Ruta: `/catalogo-parametros/api/v1/aplicaciones`. Colección de consulta: `aplicaciones`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |
| `idOrganizacion` | string | UUID de un registro existente |
| `activa` | string | Cadena "true" o "false" |
| `fechaInicio` | string | Opcional; fecha con zona o cadena vacía |
| `fechaFinal` | string | Opcional; fecha con zona o cadena vacía |

### Listar — `GET /catalogo-parametros/api/v1/aplicaciones`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "aplicaciones": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "UcoLab",
      "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000",
      "activa": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/aplicaciones`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "UcoLab", "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000", "activa": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/aplicaciones/{id}`

`id`: UUID del registro de aplicaciones que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "aplicaciones": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "UcoLab",
      "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000",
      "activa": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/aplicaciones/{id}`

`id`: UUID del registro de aplicaciones que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "UcoLab", "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000", "activa": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/aplicaciones/{id}`

`id`: UUID del registro de aplicaciones que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/aplicaciones/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `aplicacion`. Ver las reglas SSE al final.

```text
event: aplicacion
data: {"aplicacion": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "UcoLab", "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000", "activa": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```

### Cambiar estado — `POST /catalogo-parametros/api/v1/aplicaciones/{id}/cambiarestado`

`id`: UUID del registro de aplicaciones que se desea operar.

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/aplicaciones/123e4567-e89b-12d3-a456-426614174000/cambiarestado" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"activo": false}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```

<a id="modulos"></a>

## Módulos

Ruta: `/catalogo-parametros/api/v1/modulos`. Colección de consulta: `modulos`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |
| `idAplicacion` | string | UUID de un registro existente |
| `activo` | string | Cadena "true" o "false" |
| `fechaInicio` | string | Opcional; fecha con zona o cadena vacía |
| `fechaFinal` | string | Opcional; fecha con zona o cadena vacía |

### Listar — `GET /catalogo-parametros/api/v1/modulos`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/modulos?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "modulos": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Matriculas",
      "idAplicacion": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/modulos`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/modulos" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Matriculas", "idAplicacion": "123e4567-e89b-12d3-a456-426614174000", "activo": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/modulos/{id}`

`id`: UUID del registro de módulos que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/modulos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "modulos": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Matriculas",
      "idAplicacion": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/modulos/{id}`

`id`: UUID del registro de módulos que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/modulos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Matriculas", "idAplicacion": "123e4567-e89b-12d3-a456-426614174000", "activo": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/modulos/{id}`

`id`: UUID del registro de módulos que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/modulos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/modulos/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/modulos/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `modulo`. Ver las reglas SSE al final.

```text
event: modulo
data: {"modulo": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Matriculas", "idAplicacion": "123e4567-e89b-12d3-a456-426614174000", "activo": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```

### Cambiar estado — `POST /catalogo-parametros/api/v1/modulos/{id}/cambiarestado`

`id`: UUID del registro de módulos que se desea operar.

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/modulos/123e4567-e89b-12d3-a456-426614174000/cambiarestado" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"activo": false}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

<a id="funcionalidades"></a>

## Funcionalidades

Ruta: `/catalogo-parametros/api/v1/funcionalidades`. Colección de consulta: `funcionalidades`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |
| `idModulo` | string | UUID de un registro existente |
| `activo` | string | Cadena "true" o "false" |
| `fechaInicio` | string | Opcional; fecha con zona o cadena vacía |
| `fechaFinal` | string | Opcional; fecha con zona o cadena vacía |

### Listar — `GET /catalogo-parametros/api/v1/funcionalidades`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "funcionalidades": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Crear matricula",
      "idModulo": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/funcionalidades`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Crear matricula", "idModulo": "123e4567-e89b-12d3-a456-426614174000", "activo": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/funcionalidades/{id}`

`id`: UUID del registro de funcionalidades que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "funcionalidades": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Crear matricula",
      "idModulo": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true,
      "fechaInicio": "2026-10-01T08:00:00-05:00",
      "fechaFinal": null
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/funcionalidades/{id}`

`id`: UUID del registro de funcionalidades que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Crear matricula", "idModulo": "123e4567-e89b-12d3-a456-426614174000", "activo": "true", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": ""}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/funcionalidades/{id}`

`id`: UUID del registro de funcionalidades que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/funcionalidades/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `funcionalidad`. Ver las reglas SSE al final.

```text
event: funcionalidad
data: {"funcionalidad": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Crear matricula", "idModulo": "123e4567-e89b-12d3-a456-426614174000", "activo": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```

### Cambiar estado — `POST /catalogo-parametros/api/v1/funcionalidades/{id}/cambiarestado`

`id`: UUID del registro de funcionalidades que se desea operar.

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/funcionalidades/123e4567-e89b-12d3-a456-426614174000/cambiarestado" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"activo": false}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

<a id="parametros"></a>

## Parámetros

Ruta: `/catalogo-parametros/api/v1/parametros`. Colección de consulta: `parametros`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |
| `idFuncionalidad` | string | UUID de un registro existente |
| `idTipoParametro` | string | UUID de un registro existente |
| `activo` | string | Cadena "true" o "false" |

### Listar — `GET /catalogo-parametros/api/v1/parametros`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/parametros?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "parametros": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "idioma",
      "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000",
      "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/parametros`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/parametros" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "idioma", "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000", "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000", "activo": "true"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/parametros/{id}`

`id`: UUID del registro de parámetros que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/parametros/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "parametros": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "idioma",
      "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000",
      "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000",
      "activo": true
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/parametros/{id}`

`id`: UUID del registro de parámetros que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/parametros/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "idioma", "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000", "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000", "activo": "true"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/parametros/{id}`

`id`: UUID del registro de parámetros que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/parametros/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/parametros/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/parametros/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `parametro`. Ver las reglas SSE al final.

```text
event: parametro
data: {"parametro": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "idioma", "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000", "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000", "activo": true}, "event": "CREATED"}

```

### Cambiar estado — `POST /catalogo-parametros/api/v1/parametros/{id}/cambiarestado`

`id`: UUID del registro de parámetros que se desea operar.

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/parametros/123e4567-e89b-12d3-a456-426614174000/cambiarestado" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"activo": false}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```

<a id="metadatos"></a>

## Metadatos

Ruta: `/catalogo-parametros/api/v1/metadatos`. Colección de consulta: `metadatos`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `idParametro` | string | UUID de un registro existente |
| `idTipoMetadato` | string | UUID de un registro existente |
| `valor` | JSON tipado | JSON compatible con el tipo de metadato |

El ejemplo usa texto; no convertir automáticamente números, booleanos u objetos a cadenas si el catálogo exige otro tipo JSON.

### Listar — `GET /catalogo-parametros/api/v1/metadatos`

Sin paginación. Filtro opcional: `idParametro` (UUID).

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos" -H "Accept: application/json"
```

Para consultar solo los metadatos de un parámetro:

```bash
curl "http://localhost:8080/catalogo-parametros/api/v1/metadatos?idParametro=123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "metadatos": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000",
      "valor": "es-CO"
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/metadatos`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/metadatos" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"idParametro": "123e4567-e89b-12d3-a456-426614174000", "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000", "valor": "es-CO"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/metadatos/{id}`

`id`: UUID del registro de metadatos que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "metadatos": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000",
      "valor": "es-CO"
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/metadatos/{id}`

`id`: UUID del registro de metadatos que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/metadatos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"idParametro": "123e4567-e89b-12d3-a456-426614174000", "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000", "valor": "es-CO"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/metadatos/{id}`

`id`: UUID del registro de metadatos que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/metadatos/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/metadatos/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `metadato`. Ver las reglas SSE al final.

```text
event: metadato
data: {"metadato": {"id": "123e4567-e89b-12d3-a456-426614174000", "idParametro": "123e4567-e89b-12d3-a456-426614174000", "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000", "valor": "es-CO"}, "event": "CREATED"}

```

<a id="tipos-parametro"></a>

## Tipos de parámetro

Ruta: `/catalogo-parametros/api/v1/tipos-parametro`. Colección de consulta: `tiposParametro`.


### Listar — `GET /catalogo-parametros/api/v1/tipos-parametro`

Sin paginación ni filtros declarados.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/tipos-parametro" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "tiposParametro": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Ejemplo"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/tipos-parametro/{id}`

`id`: UUID del registro de tipos de parámetro que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/tipos-parametro/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "tiposParametro": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Ejemplo"
    }
  ]
}
```

<a id="tipos-metadato"></a>

## Tipos de metadato

Ruta: `/catalogo-parametros/api/v1/tipos-metadato`. Colección de consulta: `tiposMetadato`.


### Listar — `GET /catalogo-parametros/api/v1/tipos-metadato`

Sin paginación ni filtros declarados.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/tipos-metadato" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "tiposMetadato": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "tipo": "STRING",
      "detalle": "Texto"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/tipos-metadato/{id}`

`id`: UUID del registro de tipos de metadato que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/tipos-metadato/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "tiposMetadato": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "tipo": "STRING",
      "detalle": "Texto"
    }
  ]
}
```

<a id="ambientes"></a>

## Ambientes

Ruta: `/catalogo-parametros/api/v1/ambientes`. Colección de consulta: `ambientes`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |

### Listar — `GET /catalogo-parametros/api/v1/ambientes`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/ambientes?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "ambientes": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Desarrollo"
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/ambientes`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/ambientes" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Desarrollo"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "ambientes": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Desarrollo"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/ambientes/{id}`

`id`: UUID del registro de ambientes que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/ambientes/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "ambientes": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Desarrollo"
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/ambientes/{id}`

`id`: UUID del registro de ambientes que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/ambientes/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Desarrollo"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "ambientes": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Desarrollo"
    }
  ]
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/ambientes/{id}`

`id`: UUID del registro de ambientes que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/ambientes/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "ambientes": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/ambientes/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/ambientes/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `ambiente`. Ver las reglas SSE al final.

```text
event: ambiente
data: {"ambiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Desarrollo"}, "event": "CREATED"}

```

<a id="estados-ambiente"></a>

## Estados de ambiente

Ruta: `/catalogo-parametros/api/v1/estados-ambiente`. Colección de consulta: `estadosAmbiente`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |

### Listar — `GET /catalogo-parametros/api/v1/estados-ambiente`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "estadosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Activo"
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/estados-ambiente`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Activo"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Activo"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/estados-ambiente/{id}`

`id`: UUID del registro de estados de ambiente que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "estadosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Activo"
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/estados-ambiente/{id}`

`id`: UUID del registro de estados de ambiente que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Activo"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Activo"
    }
  ]
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/estados-ambiente/{id}`

`id`: UUID del registro de estados de ambiente que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosAmbiente": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/estados-ambiente/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-ambiente/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `estadoambiente`. Ver las reglas SSE al final.

```text
event: estadoambiente
data: {"estadoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Activo"}, "event": "CREATED"}

```

<a id="estados-metadato-ambiente"></a>

## Estados de metadato de ambiente

Ruta: `/catalogo-parametros/api/v1/estados-metadato-ambiente`. Colección de consulta: `estadosMetadatoAmbiente`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `nombre` | string | Nombre del recurso |

### Listar — `GET /catalogo-parametros/api/v1/estados-metadato-ambiente`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "estadosMetadatoAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Aprobado"
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/estados-metadato-ambiente`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Aprobado"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosMetadatoAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Aprobado"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/estados-metadato-ambiente/{id}`

`id`: UUID del registro de estados de metadato de ambiente que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "estadosMetadatoAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Aprobado"
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/estados-metadato-ambiente/{id}`

`id`: UUID del registro de estados de metadato de ambiente que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"nombre": "Aprobado"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosMetadatoAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "nombre": "Aprobado"
    }
  ]
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/estados-metadato-ambiente/{id}`

`id`: UUID del registro de estados de metadato de ambiente que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosMetadatoAmbiente": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/estados-metadato-ambiente/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/estados-metadato-ambiente/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `estadometadatoambiente`. Ver las reglas SSE al final.

```text
event: estadometadatoambiente
data: {"estadoMetadatoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Aprobado"}, "event": "CREATED"}

```

<a id="metadatos-ambiente"></a>

## Metadatos de ambiente

Ruta: `/catalogo-parametros/api/v1/metadatos-ambiente`. Colección de consulta: `metadatosAmbiente`.

Campos de creación y actualización (el mismo cuerpo para POST y PUT):

| Campo | Tipo JSON | Uso |
| --- | --- | --- |
| `idParametro` | string | UUID de un registro existente |
| `idAmbiente` | string | UUID de un registro existente |
| `idEstadoMetadatoAmbiente` | string | UUID de un registro existente |

### Listar — `GET /catalogo-parametros/api/v1/metadatos-ambiente`

`page` y `pageSize` son opcionales; valores predeterminados 1 y 10.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente?page=1&pageSize=10" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "metadatosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idAmbiente": "123e4567-e89b-12d3-a456-426614174000",
      "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"
    }
  ]
}
```

### Crear — `POST /catalogo-parametros/api/v1/metadatos-ambiente`

```bash
curl -X POST "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"idParametro": "123e4567-e89b-12d3-a456-426614174000", "idAmbiente": "123e4567-e89b-12d3-a456-426614174000", "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"}'
```

Respuesta de ejemplo: **201**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idAmbiente": "123e4567-e89b-12d3-a456-426614174000",
      "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"
    }
  ]
}
```

### Consultar por ID — `GET /catalogo-parametros/api/v1/metadatos-ambiente/{id}`

`id`: UUID del registro de metadatos de ambiente que se desea operar.

```bash
curl -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [],
  "metadatosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idAmbiente": "123e4567-e89b-12d3-a456-426614174000",
      "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"
    }
  ]
}
```

### Actualizar — `PUT /catalogo-parametros/api/v1/metadatos-ambiente/{id}`

`id`: UUID del registro de metadatos de ambiente que se desea operar.

```bash
curl -X PUT "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json" -H "Content-Type: application/json" --data '{"idParametro": "123e4567-e89b-12d3-a456-426614174000", "idAmbiente": "123e4567-e89b-12d3-a456-426614174000", "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"}'
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatosAmbiente": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "idParametro": "123e4567-e89b-12d3-a456-426614174000",
      "idAmbiente": "123e4567-e89b-12d3-a456-426614174000",
      "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"
    }
  ]
}
```

### Eliminar — `DELETE /catalogo-parametros/api/v1/metadatos-ambiente/{id}`

`id`: UUID del registro de metadatos de ambiente que se desea operar.

```bash
curl -X DELETE "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente/123e4567-e89b-12d3-a456-426614174000" -H "Accept: application/json"
```

Respuesta de ejemplo: **200**. Los mensajes e IDs son ilustrativos.

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatosAmbiente": []
}
```

### Escuchar cambios — `GET /catalogo-parametros/api/v1/metadatos-ambiente/events`

```bash
curl -N -X GET "http://localhost:8080/catalogo-parametros/api/v1/metadatos-ambiente/events" -H "Accept: text/event-stream"
```

Respuesta 200, `text/event-stream`; conexión continua. Evento nombrado `metadatoambiente`. Ver las reglas SSE al final.

```text
event: metadatoambiente
data: {"metadatoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "idParametro": "123e4567-e89b-12d3-a456-426614174000", "idAmbiente": "123e4567-e89b-12d3-a456-426614174000", "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"}, "event": "CREATED"}

```

## Consumir eventos SSE

Los diez recursos con escritura ofrecen `GET /events`; los dos catálogos de tipos no. El nombre SSE es singular y en minúsculas: `organizacion`, `aplicacion`, `modulo`, `funcionalidad`, `parametro`, `metadato`, `ambiente`, `estadoambiente`, `estadometadatoambiente`, `metadatoambiente`. Dentro de `data`, la propiedad del recurso usa camelCase (`estadoAmbiente`, `estadoMetadatoAmbiente`, `metadatoAmbiente`); `event` indica `CREATED`, `UPDATED` o `DELETED`.

Ejemplo para un componente web (sustituir el host por el de su ambiente):

```javascript
const stream = new EventSource(
  'http://localhost:8080/catalogo-parametros/api/v1/parametros/events'
);
stream.addEventListener('parametro', (message) => {
  const cambio = JSON.parse(message.data);
  console.log(cambio.event, cambio.parametro);
  // Volver a consultar el recurso y reconciliar el estado de la pantalla.
});
stream.onerror = () => {
  // EventSource intenta reconectar; reconciliar mediante GET al recuperar conexión.
};
// Al destruir el componente o terminar su uso:
// stream.close();
```

Usar `addEventListener` con el nombre del evento; `onmessage` no recibe los eventos nombrados del catálogo. Cerrar la conexión al destruir el componente. Cada publisher conserva sus últimos 100 eventos en memoria y puede reproducirlos al conectar; el flujo combina publishers de creación, actualización y eliminación sin orden global garantizado. No hay ID SSE ni soporte de `Last-Event-ID`. El historial se pierde al reiniciar y no constituye una cola durable. Reconciliar mediante GET y tolerar eventos repetidos. Se envía un comentario inicial `connected`, excepto para metadatos.

## Ejemplo de consumo HTTP desde otro componente

```javascript
async function consultarParametro(baseUrl, idParametro) {
  const response = await fetch(
    `${baseUrl}/catalogo-parametros/api/v1/parametros/${encodeURIComponent(idParametro)}`,
    { headers: { Accept: 'application/json' } }
  );
  const body = await response.json();
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${(body.mensajes ?? []).join('; ')}`);
  }
  return body.parametros[0] ?? null;
}
```

Configurar el host por ambiente en el componente consumidor y tratar timeouts y errores de red además de respuestas HTTP. Para decidir por tipo de fallo, usar el estado HTTP; reservar `mensajes` para presentación o diagnóstico.

## Endpoints técnicos

Estas rutas usan el host directamente, sin `/catalogo-parametros/api/v1`:

| Método | Ruta | Uso |
| --- | --- | --- |
| GET | `/actuator` | Enlaces de gestión |
| GET | `/actuator/health` | Salud del servicio |
| GET | `/actuator/info` | Información de aplicación |
| GET | `/actuator/prometheus` | Métricas para monitoreo |
| GET | `/v3/api-docs` | Contrato generado en JSON |
| GET | `/v3/api-docs.yaml` | Contrato generado en YAML |
| GET | `/v3/api-docs/swagger-config` | Configuración de Swagger UI |
| GET | `/swagger-ui.html` | Interfaz de exploración |
| GET | `/openapi/catalogo-parametros-v1.yaml` | Contrato estático empaquetado |

Ejemplos:

```bash
curl "http://localhost:8080/actuator/health" -H "Accept: application/json"
curl "http://localhost:8080/v3/api-docs" -H "Accept: application/json"
curl "http://localhost:8080/actuator/prometheus" -H "Accept: text/plain"
```

Los cuerpos técnicos dependen de Spring Boot y de los componentes configurados; no tienen el envoltorio `mensajes` de las rutas de negocio.

## Mantener el contrato

Ante un cambio de endpoint, actualizar juntos el contrato OpenAPI, esta guía y la colección Postman. Revisar método, ruta, nombres y tipos JSON, estados HTTP y eventos antes de publicar una versión. El prefijo `/api/v1` es la versión de las rutas; `info.version` en OpenAPI es la revisión del documento. La documentación describe la implementación actual y puede servir como contrato acordado para próximas integraciones.
