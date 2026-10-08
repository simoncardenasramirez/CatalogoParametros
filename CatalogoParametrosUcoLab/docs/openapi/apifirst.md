# API First — Diseño de contratos del Catálogo de Parámetros UcoLab

## Propósito y alcance del diseño

Se propone una API que permita administrar una estructura organizacional de aplicaciones, módulos y funcionalidades, centralizar sus parámetros, asignarles valores tipados y relacionarlos con ambientes. El frontend administrativo, otros servicios y usuarios técnicos deberán poder consumir los contratos sin conocer la persistencia ni la organización interna del backend.

Este documento define necesidades, decisiones, entradas, salidas y criterios de aceptación para **68 operaciones, 38 rutas y 12 recursos** bajo `/catalogo-parametros/api/v1`. Se redacta como un diseño de planificación reconstruido a partir de los contratos existentes; no constituye evidencia de que el diseño se elaboró antes de la implementación. El alcance propuesto mantiene compatibilidad con esos contratos.

El [OpenAPI 1.1.0](catalogo-parametros-v1.yaml) será la representación procesable de los contratos. La [guía de integración](GUIA-INTEGRACION.md) explicará su consumo y la [colección Postman](catalogo-parametros.postman_collection.json) ofrecerá solicitudes de ejemplo. API First establecerá por qué se necesita cada operación y qué deberán acordar productor y consumidores.

## Consumidores y necesidades

| Consumidor | Necesidad | Decisión de diseño |
| --- | --- | --- |
| Administrador y frontend | Mantener la estructura y los datos del catálogo | Consultas y operaciones de creación, actualización y eliminación por recurso |
| Servicio consumidor | Localizar parámetros y sus valores tipados | UUID estables, consulta por ID y filtro de metadatos por parámetro |
| Formularios de administración | Seleccionar clasificaciones válidas | Dos catálogos de tipos de solo lectura |
| Componente que trabaja por ambiente | Asociar parámetros con ambiente y estado | Recurso de asociación `metadatos-ambiente` |
| Pantallas sincronizadas | Detectar cambios y refrescar datos | SSE por recurso con escritura, más consultas de reconciliación |
| Operación técnica | Supervisar disponibilidad y explorar contratos | Endpoints de salud, métricas y documentación separados de negocio |

## Modelo de recursos y límites

La organización contendrá aplicaciones; cada aplicación agrupará módulos; cada módulo agrupará funcionalidades; cada funcionalidad tendrá parámetros clasificados por un tipo de parámetro. El metadato vinculará un parámetro, un tipo de metadato y un valor JSON. El metadato de ambiente vinculará un parámetro, un ambiente y un estado de metadato de ambiente; su función será expresar una asociación, sin contener un valor.

`EstadoAmbiente` será un catálogo independiente. No se incluirá `idEstadoAmbiente` en el cuerpo de `Ambiente`. No se deberá deducir una relación que el contrato no expresa.

## Decisiones comunes de contrato

- Se usará HTTP con JSON UTF-8 para operaciones normales: `Accept: application/json` y `Content-Type: application/json` cuando exista cuerpo. El host será configurable; el prefijo de negocio será `/catalogo-parametros/api/v1`.
- Los UUID identificarán recursos y relaciones; el ID del recurso objetivo irá en la ruta, sin incluirlo en el cuerpo de creación. Los ejemplos con UUID serán ilustrativos y deberán sustituirse por registros existentes.
- `POST` creará y responderá 201; `PUT` recibirá todos los campos del contrato de actualización y responderá 200. `DELETE` responderá 200 con JSON. No se diseñará `PATCH` en este alcance.
- Las respuestas JSON incluirán `mensajes: string[]` y una colección de recurso. La consulta por ID también devolverá una colección. Una consulta de lista sin coincidencias devolverá 200 y una colección vacía.
- Las escrituras de organizaciones, aplicaciones, módulos, funcionalidades, parámetros y metadatos devolverán una colección vacía. Las creaciones y actualizaciones de los cuatro recursos de ambientes devolverán una colección con la entidad y su ID. No se acordará una cabecera `Location` ni un ID en las escrituras originales.
- Por compatibilidad, las escrituras de módulos y funcionalidades devolverán `parametros: []`; sus consultas devolverán `modulos` y `funcionalidades`. El consumidor deberá modelar por separado ambas respuestas.
- En crear/actualizar, `activa` (aplicación) y `activo` (módulo, funcionalidad, parámetro) serán cadenas `"true"` o `"false"`; en la entidad de respuesta serán booleanos. El cambio de estado siempre recibirá `activo` como booleano JSON.
- Las fechas serán opcionales: cadena ISO-8601 con zona, cadena vacía o `null`. Si se suministran ambas, `fechaFinal` no precederá a `fechaInicio`. Las respuestas admitirán fechas nulas y desplazamientos horarios normalizados.
- Las listas paginadas admitirán `page` y `pageSize`, predeterminados 1 y 10; valores inferiores a 1 se normalizarán a 1. No se ofrecerán total ni enlaces de navegación. Metadatos y catálogos de tipos no tendrán paginación.
- La API de negocio no definirá autenticación, API key ni autorización por rol en este alcance. El consumidor en navegador requerirá un origen CORS habilitado; el origen local previsto será `http://localhost:4200`. Un despliegue podrá establecer requisitos de acceso adicionales.
- No se ofrecerán filtros por nombre, ordenación configurable, claves de idempotencia ni control de concurrencia mediante ETag. Ante un timeout de creación, el consumidor deberá comprobar los datos antes de repetir.

## Contrato común de errores

| HTTP | Situación | Comportamiento esperado del consumidor |
| --- | --- | --- |
| 400 | Formato o validación inválidos | Corregir la solicitud y mostrar los mensajes |
| 404 | Recurso o relación requerida inexistente, cuando la operación propague ese error | Revisar UUID y consultar existencia |
| 409 | Conflicto de nombre o uso, cuando la operación propague ese error | Resolver duplicidad o dependencia |
| 500 | Error técnico | Registrar contexto y decidir recuperación según la operación |

El error global tendrá la forma `{"mensajes":["Descripción del error"]}`. Algunos errores podrán incluir además una colección vacía. Los consumidores decidirán por estado HTTP; el texto no será un código de error estable. Los errores de conversión de parámetros gestionados por el framework podrán tener su propio cuerpo y no deberán tratarse como una entidad de negocio.

Para conservar el comportamiento de los contratos, eliminar organizaciones o aplicaciones con dependencias o sin existencia producirá **400** por agrupación de validaciones. Organización también podrá responder 400 ante errores técnicos capturados por su controlador. Estos casos se deberán contemplar al diseñar clientes.

## Diseño por recurso y operación

Las rutas de las fichas serán relativas al prefijo común. Cada ficha definirá la necesidad del consumidor, la entrada, el resultado y un criterio verificable. Los errores comunes se aplicarán según las reglas de cada operación; no se supone que todas las operaciones produzcan todos los estados de error.


### Organizaciones

**Necesidad:** Representar las organizaciones responsables de las aplicaciones y su vigencia.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `organizaciones`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Enviar. Identificar el recurso con un nombre legible. |
| `fechaInicio` | string | Opcional. Definir el comienzo de la vigencia, si se requiere. |
| `fechaFinal` | string | Opcional. Definir el fin de la vigencia, si se requiere. |

**Reglas del recurso:** El nombre se recortará y deberá tener entre 3 y 50 caracteres; se comprobarán duplicados conforme al recurso.

**Modelo de lectura de referencia:**

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


#### GET /organizaciones

**Necesidad del consumidor:** Descubrir los registros de organizaciones para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `organizaciones`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /organizaciones

**Necesidad del consumidor:** Registrar un nuevo elemento de organizaciones con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "UCO",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```


#### GET /organizaciones/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de organizaciones.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `organizaciones` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /organizaciones/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "UCO",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```


#### DELETE /organizaciones/{id}

**Necesidad del consumidor:** Retirar un registro de organizaciones que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado. Inexistencia o dependencia con aplicaciones/módulos, respectivamente: 400.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "organizaciones": []
}
```


#### GET /organizaciones/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en organizaciones.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `organizacion`; JSON de `data` con `organizacion` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: organizacion
data: {"organizacion": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "UCO", "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```


### Aplicaciones

**Necesidad:** Organizar las aplicaciones de una organización y controlar su disponibilidad.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `aplicaciones`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Enviar. Identificar el recurso con un nombre legible. |
| `idOrganizacion` | string (UUID) | Enviar. Vincular una organización existente. |
| `activa` | string | Enviar. Indicar disponibilidad de la aplicación. |
| `fechaInicio` | string | Opcional. Definir el comienzo de la vigencia, si se requiere. |
| `fechaFinal` | string | Opcional. Definir el fin de la vigencia, si se requiere. |

**Reglas del recurso:** El nombre se recortará y deberá tener entre 3 y 50 caracteres; se comprobarán duplicados conforme al recurso.

**Modelo de lectura de referencia:**

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


#### GET /aplicaciones

**Necesidad del consumidor:** Descubrir los registros de aplicaciones para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `aplicaciones`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /aplicaciones

**Necesidad del consumidor:** Registrar un nuevo elemento de aplicaciones con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "UcoLab",
  "idOrganizacion": "{{idOrganizacion}}",
  "activa": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```


#### GET /aplicaciones/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de aplicaciones.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `aplicaciones` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /aplicaciones/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "UcoLab",
  "idOrganizacion": "{{idOrganizacion}}",
  "activa": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```


#### DELETE /aplicaciones/{id}

**Necesidad del consumidor:** Retirar un registro de aplicaciones que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado. Inexistencia o dependencia con aplicaciones/módulos, respectivamente: 400.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```


#### GET /aplicaciones/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en aplicaciones.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `aplicacion`; JSON de `data` con `aplicacion` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: aplicacion
data: {"aplicacion": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "UcoLab", "idOrganizacion": "123e4567-e89b-12d3-a456-426614174000", "activa": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```


#### POST /aplicaciones/{id}/cambiarestado

**Necesidad del consumidor:** Activar o desactivar el registro sin reenviar sus demás datos.

**Entrada:** Ruta `id`: UUID existente. Cuerpo `{"activo": false}` o `{"activo": true}`; booleano obligatorio.

**Salida acordada:** 200 con mensajes y colección vacía; deberá publicarse un evento `UPDATED` en el flujo del recurso.

**Criterio de aceptación:** Solo deberá cambiar el estado; los demás campos deberán conservarse. Un campo `activo` ausente deberá producir 400.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "activo": false
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "aplicaciones": []
}
```


### Módulos

**Necesidad:** Agrupar las capacidades de una aplicación en módulos administrables.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `modulos`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Enviar. Identificar el recurso con un nombre legible. |
| `idAplicacion` | string (UUID) | Enviar. Vincular una aplicación existente. |
| `activo` | string | Enviar. Indicar disponibilidad del recurso. |
| `fechaInicio` | string | Opcional. Definir el comienzo de la vigencia, si se requiere. |
| `fechaFinal` | string | Opcional. Definir el fin de la vigencia, si se requiere. |

**Reglas del recurso:** El nombre se recortará y deberá tener entre 3 y 50 caracteres; se comprobarán duplicados conforme al recurso.

**Modelo de lectura de referencia:**

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


#### GET /modulos

**Necesidad del consumidor:** Descubrir los registros de modulos para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `modulos`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /modulos

**Necesidad del consumidor:** Registrar un nuevo elemento de modulos con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Matriculas",
  "idAplicacion": "{{idAplicacion}}",
  "activo": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /modulos/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de modulos.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `modulos` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /modulos/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Matriculas",
  "idAplicacion": "{{idAplicacion}}",
  "activo": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### DELETE /modulos/{id}

**Necesidad del consumidor:** Retirar un registro de modulos que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /modulos/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en modulos.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `modulo`; JSON de `data` con `modulo` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: modulo
data: {"modulo": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Matriculas", "idAplicacion": "123e4567-e89b-12d3-a456-426614174000", "activo": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```


#### POST /modulos/{id}/cambiarestado

**Necesidad del consumidor:** Activar o desactivar el registro sin reenviar sus demás datos.

**Entrada:** Ruta `id`: UUID existente. Cuerpo `{"activo": false}` o `{"activo": true}`; booleano obligatorio.

**Salida acordada:** 200 con mensajes y colección vacía; deberá publicarse un evento `UPDATED` en el flujo del recurso.

**Criterio de aceptación:** Solo deberá cambiar el estado; los demás campos deberán conservarse. Un campo `activo` ausente deberá producir 400.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "activo": false
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


### Funcionalidades

**Necesidad:** Identificar las funcionalidades de cada módulo que requieren parámetros.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `funcionalidades`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Enviar. Identificar el recurso con un nombre legible. |
| `idModulo` | string (UUID) | Enviar. Vincular un módulo existente. |
| `activo` | string | Enviar. Indicar disponibilidad del recurso. |
| `fechaInicio` | string | Opcional. Definir el comienzo de la vigencia, si se requiere. |
| `fechaFinal` | string | Opcional. Definir el fin de la vigencia, si se requiere. |

**Reglas del recurso:** El nombre se recortará y deberá tener entre 3 y 50 caracteres; se comprobarán duplicados conforme al recurso.

**Modelo de lectura de referencia:**

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


#### GET /funcionalidades

**Necesidad del consumidor:** Descubrir los registros de funcionalidades para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `funcionalidades`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /funcionalidades

**Necesidad del consumidor:** Registrar un nuevo elemento de funcionalidades con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Crear matricula",
  "idModulo": "{{idModulo}}",
  "activo": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /funcionalidades/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de funcionalidades.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `funcionalidades` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /funcionalidades/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Crear matricula",
  "idModulo": "{{idModulo}}",
  "activo": "true",
  "fechaInicio": "2026-10-01T08:00:00-05:00",
  "fechaFinal": ""
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### DELETE /funcionalidades/{id}

**Necesidad del consumidor:** Retirar un registro de funcionalidades que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado. Si tiene parámetros asociados: 409; inexistencia: 404.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /funcionalidades/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en funcionalidades.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `funcionalidad`; JSON de `data` con `funcionalidad` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: funcionalidad
data: {"funcionalidad": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Crear matricula", "idModulo": "123e4567-e89b-12d3-a456-426614174000", "activo": true, "fechaInicio": "2026-10-01T08:00:00-05:00", "fechaFinal": null}, "event": "CREATED"}

```


#### POST /funcionalidades/{id}/cambiarestado

**Necesidad del consumidor:** Activar o desactivar el registro sin reenviar sus demás datos.

**Entrada:** Ruta `id`: UUID existente. Cuerpo `{"activo": false}` o `{"activo": true}`; booleano obligatorio.

**Salida acordada:** 200 con mensajes y colección vacía; deberá publicarse un evento `UPDATED` en el flujo del recurso.

**Criterio de aceptación:** Solo deberá cambiar el estado; los demás campos deberán conservarse. Un campo `activo` ausente deberá producir 400.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "activo": false
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


### Parámetros

**Necesidad:** Centralizar parámetros por funcionalidad, con clasificación y estado de uso.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `parametros`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Enviar. Identificar el recurso con un nombre legible. |
| `idFuncionalidad` | string (UUID) | Enviar. Vincular una funcionalidad existente. |
| `idTipoParametro` | string (UUID) | Enviar. Clasificar con un tipo de parámetro existente. |
| `activo` | string | Enviar. Indicar disponibilidad del recurso. |

**Reglas del recurso:** El nombre se recortará y deberá tener entre 3 y 50 caracteres; se comprobarán duplicados conforme al recurso.

**Modelo de lectura de referencia:**

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


#### GET /parametros

**Necesidad del consumidor:** Descubrir los registros de parametros para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `parametros`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /parametros

**Necesidad del consumidor:** Registrar un nuevo elemento de parametros con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "idioma",
  "idFuncionalidad": "{{idFuncionalidad}}",
  "idTipoParametro": "{{idTipoParametro}}",
  "activo": "true"
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /parametros/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de parametros.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `parametros` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /parametros/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "idioma",
  "idFuncionalidad": "{{idFuncionalidad}}",
  "idTipoParametro": "{{idTipoParametro}}",
  "activo": "true"
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### DELETE /parametros/{id}

**Necesidad del consumidor:** Retirar un registro de parametros que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


#### GET /parametros/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en parametros.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `parametro`; JSON de `data` con `parametro` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: parametro
data: {"parametro": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "idioma", "idFuncionalidad": "123e4567-e89b-12d3-a456-426614174000", "idTipoParametro": "123e4567-e89b-12d3-a456-426614174000", "activo": true}, "event": "CREATED"}

```


#### POST /parametros/{id}/cambiarestado

**Necesidad del consumidor:** Activar o desactivar el registro sin reenviar sus demás datos.

**Entrada:** Ruta `id`: UUID existente. Cuerpo `{"activo": false}` o `{"activo": true}`; booleano obligatorio.

**Salida acordada:** 200 con mensajes y colección vacía; deberá publicarse un evento `UPDATED` en el flujo del recurso.

**Criterio de aceptación:** Solo deberá cambiar el estado; los demás campos deberán conservarse. Un campo `activo` ausente deberá producir 400.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "activo": false
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "parametros": []
}
```


### Metadatos

**Necesidad:** Asignar valores tipados a un parámetro para que otros componentes puedan interpretarlos.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `metadatos`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `idParametro` | string (UUID) | Enviar. Vincular un parámetro existente. |
| `idTipoMetadato` | string (UUID) | Enviar. Definir el tipo de interpretación del valor. |
| `valor` | JSON | Enviar. Transportar el valor tipado del parámetro. |

**Reglas del valor:** se rechazará valor ausente, nulo o cadena vacía. Para tipo `json`, se aceptará objeto o arreglo; para `alfanumerico`, cadena; para `date`, cadena `yyyy-MM-dd`. Otros nombres de tipo no tendrán validación específica adicional en esta revisión. Los IDs de parámetro y tipo deberán existir.

**Modelo de lectura de referencia:**

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


#### GET /metadatos

**Necesidad del consumidor:** Descubrir los registros de metadatos para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Filtro opcional `idParametro` como UUID; sin filtro se devolverán todos los metadatos.

**Salida acordada:** 200 con `mensajes` y la colección `metadatos`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /metadatos

**Necesidad del consumidor:** Registrar un nuevo elemento de metadatos con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la colección vacía; el consumidor deberá consultar para recuperar el UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "idParametro": "{{idParametro}}",
  "idTipoMetadato": "{{idTipoMetadato}}",
  "valor": "es-CO"
}
```


**Ejemplo de salida 201:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```


#### GET /metadatos/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de metadatos.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `metadatos` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /metadatos/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y colección vacía.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "idParametro": "{{idParametro}}",
  "idTipoMetadato": "{{idTipoMetadato}}",
  "valor": "es-CO"
}
```


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```


#### DELETE /metadatos/{id}

**Necesidad del consumidor:** Retirar un registro de metadatos que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatos": []
}
```


#### GET /metadatos/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en metadatos.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `metadato`; JSON de `data` con `metadato` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: metadato
data: {"metadato": {"id": "123e4567-e89b-12d3-a456-426614174000", "idParametro": "123e4567-e89b-12d3-a456-426614174000", "idTipoMetadato": "123e4567-e89b-12d3-a456-426614174000", "valor": "es-CO"}, "event": "CREATED"}

```


### Tipos de parámetro

**Necesidad:** Ofrecer una clasificación de parámetros para formularios e integraciones.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `tiposParametro`.


**Decisión:** el catálogo ofrecerá únicamente consulta de lista y por UUID, sin cuerpo de entrada, escritura ni SSE.

**Modelo de lectura de referencia:**

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


#### GET /tipos-parametro

**Necesidad del consumidor:** Descubrir los registros de tipos-parametro para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Sin parámetros de paginación.

**Salida acordada:** 200 con `mensajes` y la colección `tiposParametro`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### GET /tipos-parametro/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de tipos-parametro.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `tiposParametro` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


### Tipos de metadato

**Necesidad:** Permitir que el consumidor conozca el tipo y detalle del valor que debe enviar.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `tiposMetadato`.


**Decisión:** el catálogo ofrecerá únicamente consulta de lista y por UUID, sin cuerpo de entrada, escritura ni SSE.

**Modelo de lectura de referencia:**

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


#### GET /tipos-metadato

**Necesidad del consumidor:** Descubrir los registros de tipos-metadato para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Sin parámetros de paginación.

**Salida acordada:** 200 con `mensajes` y la colección `tiposMetadato`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### GET /tipos-metadato/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de tipos-metadato.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `tiposMetadato` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


### Ambientes

**Necesidad:** Identificar los ambientes en los que se asociarán parámetros.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `ambientes`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Opcional en la validación; se recomienda enviarlo. Identificar el recurso con un nombre legible. |

**Reglas del recurso:** Se recortarán espacios y se comprobarán duplicados. No se acordará como validación existente la longitud 3–50 ni la obligatoriedad de nombre.

**Modelo de lectura de referencia:**

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


#### GET /ambientes

**Necesidad del consumidor:** Descubrir los registros de ambientes para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `ambientes`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /ambientes

**Necesidad del consumidor:** Registrar un nuevo elemento de ambientes con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la entidad creada en `ambientes`, incluido su UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Desarrollo"
}
```


**Ejemplo de salida 201:**

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


#### GET /ambientes/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de ambientes.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `ambientes` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /ambientes/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y la entidad actualizada en `ambientes`.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Desarrollo"
}
```


**Ejemplo de salida 200:**

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


#### DELETE /ambientes/{id}

**Necesidad del consumidor:** Retirar un registro de ambientes que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado. Si existen asociaciones de metadatos de ambiente: 409; inexistencia: 404.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "ambientes": []
}
```


#### GET /ambientes/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en ambientes.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `ambiente`; JSON de `data` con `ambiente` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: ambiente
data: {"ambiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Desarrollo"}, "event": "CREATED"}

```


### Estados de ambiente

**Necesidad:** Administrar un catálogo independiente de estados de ambiente.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `estadosAmbiente`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Opcional en la validación; se recomienda enviarlo. Identificar el recurso con un nombre legible. |

**Reglas del recurso:** Se recortarán espacios y se comprobarán duplicados. No se acordará como validación existente la longitud 3–50 ni la obligatoriedad de nombre.

**Modelo de lectura de referencia:**

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


#### GET /estados-ambiente

**Necesidad del consumidor:** Descubrir los registros de estados-ambiente para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `estadosAmbiente`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /estados-ambiente

**Necesidad del consumidor:** Registrar un nuevo elemento de estados-ambiente con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la entidad creada en `estadosAmbiente`, incluido su UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Activo"
}
```


**Ejemplo de salida 201:**

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


#### GET /estados-ambiente/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de estados-ambiente.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `estadosAmbiente` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /estados-ambiente/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y la entidad actualizada en `estadosAmbiente`.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Activo"
}
```


**Ejemplo de salida 200:**

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


#### DELETE /estados-ambiente/{id}

**Necesidad del consumidor:** Retirar un registro de estados-ambiente que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosAmbiente": []
}
```


#### GET /estados-ambiente/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en estados-ambiente.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `estadoambiente`; JSON de `data` con `estadoAmbiente` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: estadoambiente
data: {"estadoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Activo"}, "event": "CREATED"}

```


### Estados de metadato de ambiente

**Necesidad:** Definir los estados que se asignarán a las asociaciones de parámetros y ambientes.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `estadosMetadatoAmbiente`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `nombre` | string | Opcional en la validación; se recomienda enviarlo. Identificar el recurso con un nombre legible. |

**Reglas del recurso:** Se recortarán espacios y se comprobarán duplicados. No se acordará como validación existente la longitud 3–50 ni la obligatoriedad de nombre.

**Modelo de lectura de referencia:**

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


#### GET /estados-metadato-ambiente

**Necesidad del consumidor:** Descubrir los registros de estados-metadato-ambiente para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `estadosMetadatoAmbiente`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /estados-metadato-ambiente

**Necesidad del consumidor:** Registrar un nuevo elemento de estados-metadato-ambiente con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la entidad creada en `estadosMetadatoAmbiente`, incluido su UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Aprobado"
}
```


**Ejemplo de salida 201:**

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


#### GET /estados-metadato-ambiente/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de estados-metadato-ambiente.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `estadosMetadatoAmbiente` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /estados-metadato-ambiente/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y la entidad actualizada en `estadosMetadatoAmbiente`.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "nombre": "Aprobado"
}
```


**Ejemplo de salida 200:**

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


#### DELETE /estados-metadato-ambiente/{id}

**Necesidad del consumidor:** Retirar un registro de estados-metadato-ambiente que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado. Si existen asociaciones de metadatos de ambiente: 409; inexistencia: 404.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "estadosMetadatoAmbiente": []
}
```


#### GET /estados-metadato-ambiente/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en estados-metadato-ambiente.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `estadometadatoambiente`; JSON de `data` con `estadoMetadatoAmbiente` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: estadometadatoambiente
data: {"estadoMetadatoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "nombre": "Aprobado"}, "event": "CREATED"}

```


### Metadatos de ambiente

**Necesidad:** Relacionar un parámetro con un ambiente y un estado de metadato de ambiente.

**Consumidores previstos:** frontend administrativo y servicios integradores. La colección de consulta será `metadatosAmbiente`.


**Cuerpo acordado para creación y actualización:**

| Campo | Tipo JSON | Obligatoriedad y finalidad |
| --- | --- | --- |
| `idParametro` | string (UUID) | Enviar. Vincular un parámetro existente. |
| `idAmbiente` | string (UUID) | Enviar. Elegir un ambiente existente. |
| `idEstadoMetadatoAmbiente` | string (UUID) | Enviar. Asignar un estado existente a la asociación. |

**Reglas de asociación:** los tres UUID deberán identificar registros existentes. No se impondrá una unicidad de la combinación ni se agregará `valor`, `idTipoMetadato` o `idEstadoAmbiente` al contrato.

**Modelo de lectura de referencia:**

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


#### GET /metadatos-ambiente

**Necesidad del consumidor:** Descubrir los registros de metadatos-ambiente para selección, navegación o lectura por otros componentes.

**Entrada:** Sin cuerpo. Query opcional `page` y `pageSize` (1 y 10 por defecto).

**Salida acordada:** 200 con `mensajes` y la colección `metadatosAmbiente`; lista vacía cuando no haya coincidencias.

**Criterio de aceptación:** La respuesta deberá poder deserializarse con el modelo de lectura y no deberá incluir metadatos de total o paginación no acordados.


**Ejemplo de salida 200:**

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


#### POST /metadatos-ambiente

**Necesidad del consumidor:** Registrar un nuevo elemento de metadatos-ambiente con los datos y relaciones definidos.

**Entrada:** Cuerpo JSON de creación definido para el recurso. El ID lo asignará el servicio.

**Salida acordada:** 201 con mensajes y la entidad creada en `metadatosAmbiente`, incluido su UUID.

**Criterio de aceptación:** Una entrada válida deberá crear el registro y emitir `CREATED`. Las relaciones exigidas deberán existir antes de crear.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "idParametro": "{{idParametro}}",
  "idAmbiente": "{{idAmbiente}}",
  "idEstadoMetadatoAmbiente": "{{idEstadoMetadatoAmbiente}}"
}
```


**Ejemplo de salida 201:**

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


#### GET /metadatos-ambiente/{id}

**Necesidad del consumidor:** Recuperar los datos del registro seleccionado de metadatos-ambiente.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con `mensajes` y `metadatosAmbiente` como colección con el registro; 404 para un recurso inexistente.

**Criterio de aceptación:** Un UUID existente deberá recuperar el recurso identificado; un UUID sin registro deberá producir 404.


**Ejemplo de salida 200:**

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


#### PUT /metadatos-ambiente/{id}

**Necesidad del consumidor:** Modificar los datos del registro seleccionado manteniendo su identificador.

**Entrada:** Ruta `id`: UUID del recurso. Cuerpo completo de actualización definido para el recurso; fechas opcionales.

**Salida acordada:** 200 con mensajes y la entidad actualizada en `metadatosAmbiente`.

**Criterio de aceptación:** La actualización válida deberá conservar el UUID y emitir `UPDATED`; deberán evaluarse existencia, relaciones y reglas aplicables.


**Ejemplo de entrada para acordar con el consumidor:**

```json
{
  "idParametro": "{{idParametro}}",
  "idAmbiente": "{{idAmbiente}}",
  "idEstadoMetadatoAmbiente": "{{idEstadoMetadatoAmbiente}}"
}
```


**Ejemplo de salida 200:**

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


#### DELETE /metadatos-ambiente/{id}

**Necesidad del consumidor:** Retirar un registro de metadatos-ambiente que ya no se requiera.

**Entrada:** Ruta `id`: UUID del recurso. Sin cuerpo.

**Salida acordada:** 200 con mensajes y colección vacía; deberá emitirse `DELETED` con el registro eliminado.

**Criterio de aceptación:** Tras una eliminación exitosa, una nueva consulta por ese UUID deberá devolver 404. No se deberá asumir eliminación en cascada ni comprobaciones de dependencias que este contrato no declare.


**Ejemplo de salida 200:**

```json
{
  "mensajes": [
    "Mensaje de éxito de la operación"
  ],
  "metadatosAmbiente": []
}
```


#### GET /metadatos-ambiente/events

**Necesidad del consumidor:** Mantener pantallas o componentes informados de cambios en metadatos-ambiente.

**Entrada:** Sin cuerpo. Se solicitará `Accept: text/event-stream` y se mantendrá la conexión abierta.

**Salida acordada:** 200 con flujo `text/event-stream`; nombre SSE `metadatoambiente`; JSON de `data` con `metadatoAmbiente` y `event` (`CREATED`, `UPDATED`, `DELETED`).

**Criterio de aceptación:** Una creación, actualización o eliminación exitosa deberá poder observarse en este flujo. El consumidor reconocerá el evento nombrado y reconciliará los datos mediante GET.


**Formato de evento acordado:**

```text
event: metadatoambiente
data: {"metadatoAmbiente": {"id": "123e4567-e89b-12d3-a456-426614174000", "idParametro": "123e4567-e89b-12d3-a456-426614174000", "idAmbiente": "123e4567-e89b-12d3-a456-426614174000", "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174000"}, "event": "CREATED"}

```


## Decisiones de sincronización SSE

Se ofrecerán diez flujos de eventos, uno por recurso con escritura. El componente en navegador deberá usar `addEventListener` con el nombre SSE del recurso y cerrar la conexión al terminar su ciclo de vida. No bastará con `onmessage` para eventos nombrados.

Cada publisher podrá conservar los últimos 100 eventos en memoria y reproducirlos al conectar. La unión de creación, actualización y eliminación no garantizará orden global. No se diseñarán identificadores SSE, reanudación por `Last-Event-ID`, entrega exactamente una vez, cola durable ni heartbeat periódico. El historial desaparecerá tras reiniciar. Se enviará un comentario inicial `connected`, excepto en metadatos. Los consumidores deberán tolerar repetición y consultar de nuevo para reconciliar.

## Escenarios de integración que deberán cubrir los consumidores

1. Consultar tipos de parámetro y metadato para seleccionar UUID y formato de valor válidos.
2. Registrar organización, aplicación, módulo y funcionalidad en ese orden, usando la consulta para obtener los IDs que no devuelvan las escrituras.
3. Registrar el parámetro y sus metadatos; consultar `/metadatos?idParametro=<UUID>` para recuperar los valores asociados.
4. Registrar o consultar ambiente y estado de metadato de ambiente; crear la asociación con los tres UUID.
5. Abrir los flujos SSE necesarios y reconciliar con consultas. Tras reinicios o reconexiones, no confiar exclusivamente en los eventos.
6. Ante duplicidad, referencia inexistente, valor inválido o dependencia de eliminación, evaluar el HTTP y los mensajes de acuerdo con las excepciones documentadas.

Las escrituras originales no permitirán correlacionar inequívocamente el ID recién creado mediante su respuesta. Se recorrerán listas y relaciones, sin asumir que el último registro corresponde a la creación. Esta limitación deberá considerarse al diseñar integraciones concurrentes.

## Contratos técnicos de apoyo

Estas rutas estarán fuera del prefijo de negocio y sus respuestas dependerán de la configuración del servicio; no tendrán el envoltorio `mensajes` del catálogo.

| Método y ruta | Necesidad y resultado previsto |
| --- | --- |
| `GET /actuator` | Descubrir enlaces de gestión disponibles |
| `GET /actuator/health` | Consultar salud para supervisión; el estado HTTP dependerá de la salud de los componentes |
| `GET /actuator/info` | Consultar información técnica expuesta |
| `GET /actuator/prometheus` | Obtener métricas para recolección Prometheus |
| `GET /v3/api-docs` | Recuperar el contrato generado en JSON |
| `GET /v3/api-docs.yaml` | Recuperar el contrato generado en YAML |
| `GET /v3/api-docs/swagger-config` | Consultar configuración de Swagger UI |
| `GET /swagger-ui.html` | Acceder a la exploración interactiva, admitiendo redirección |
| `GET /openapi/catalogo-parametros-v1.yaml` | Obtener el contrato estático publicado al empaquetar |

Swagger UI leerá el contrato generado en `/v3/api-docs`; el YAML versionado será un artefacto separado. La publicación deberá mantener ambos compatibles con los contratos de negocio.

## Evolución y validación del diseño

El productor y los consumidores deberán revisar conjuntamente rutas, tipos JSON, respuestas y errores antes de aceptar un cambio. Los cambios incompatibles requerirán una nueva versión de API o un acuerdo explícito de migración. La revisión documental `1.1.0` será distinta de la versión de rutas `v1`.

La aceptación verificará la cobertura de las 68 operaciones, la correspondencia entre DTO y solicitudes, los nombres de las colecciones, las fechas opcionales, los tipos de estado, las relaciones existentes y el formato SSE. OpenAPI, API First, guía y Postman deberán evolucionar juntos. Los ejemplos serán ilustrativos; una validación de diseño no sustituirá las pruebas contra un despliegue.

Como decisiones para una revisión futura se podrán evaluar devolución uniforme de ID al crear, respuesta propia en escrituras de módulos y funcionalidades, errores con códigos estables, paginación con total, autenticación y reanudación durable de eventos. Ninguna de esas mejoras formará parte de los contratos de esta versión.
