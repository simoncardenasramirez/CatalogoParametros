# OpenAPI del Catálogo de Parámetros UcoLab

El archivo [catalogo-parametros-v1.yaml](catalogo-parametros-v1.yaml) documenta las **68 operaciones de negocio, 38 rutas y 12 recursos** implementados por los controladores actuales. Usa OpenAPI 3.0.3; la revisión del contrato es 1.1.0.

## Inventario de operaciones

Prefijo común: `/catalogo-parametros/api/v1`.

| Recurso | Ruta de colección | Operaciones |
| --- | --- | --- |
| Organizaciones | `/organizaciones` | Listar, consultar por ID, crear, actualizar, eliminar, SSE |
| Aplicaciones | `/aplicaciones` | Listar, consultar por ID, crear, actualizar, eliminar, cambiar estado, SSE |
| Módulos | `/modulos` | Listar, consultar por ID, crear, actualizar, eliminar, cambiar estado, SSE |
| Funcionalidades | `/funcionalidades` | Listar, consultar por ID, crear, actualizar, eliminar, cambiar estado, SSE |
| Parámetros | `/parametros` | Listar, consultar por ID, crear, actualizar, eliminar, cambiar estado, SSE |
| Metadatos | `/metadatos` | Listar o filtrar por parámetro, consultar por ID, crear, actualizar, eliminar, SSE |
| Tipos de parámetro | `/tipos-parametro` | Listar, consultar por ID |
| Tipos de metadato | `/tipos-metadato` | Listar, consultar por ID |
| Ambientes | `/ambientes` | Listar, consultar por ID, crear, actualizar, eliminar, SSE |
| Estados de ambiente | `/estados-ambiente` | Listar, consultar por ID, crear, actualizar, eliminar, SSE |
| Estados de metadato de ambiente | `/estados-metadato-ambiente` | Listar, consultar por ID, crear, actualizar, eliminar, SSE |
| Metadatos de ambiente | `/metadatos-ambiente` | Listar, consultar por ID, crear, actualizar, eliminar, SSE |

Convenciones:

- `GET /recurso`: consultar la colección.
- `POST /recurso`: crear; responde 201.
- `GET /recurso/{id}`, `PUT /recurso/{id}`, `DELETE /recurso/{id}`: consultar, actualizar y eliminar; responden 200 cuando tienen éxito.
- `POST /recurso/{id}/cambiarestado`: disponible únicamente para aplicaciones, módulos, funcionalidades y parámetros; recibe `{"activo": false}`.
- `GET /recurso/events`: flujo `text/event-stream`, excepto los dos catálogos de tipos, que solo tienen consultas.

## Consultas y contratos

Las listas paginadas reciben `page` (predeterminado 1) y `pageSize` (predeterminado 10). El backend normaliza los valores menores que uno a uno. Las respuestas contienen la colección y `mensajes`; no incluyen un total de registros.

`GET /metadatos` no está paginado: acepta el filtro opcional `idParametro` (UUID). Sin filtro consulta todos los metadatos. Los catálogos de tipos tampoco están paginados.

Las fechas de organizaciones, aplicaciones, módulos y funcionalidades son opcionales. Admiten ISO-8601 con desplazamiento horario o una cadena vacía para ausencia de fecha. Si ambas existen, la fecha final no puede ser anterior a la inicial.

Los DTO de creación y actualización de aplicaciones, módulos, funcionalidades y parámetros declaran `activa`/`activo` como cadena `"true"` o `"false"`. El DTO de cambio de estado declara `activo` como booleano JSON.

Los nombres de los cinco recursos originales usan la validación de 3 a 50 caracteres después de recortar espacios. Los tres catálogos nuevos por nombre validan duplicados, pero actualmente no aplican esa validación de longitud ni obligatoriedad; el YAML refleja esa diferencia.

## Ambientes y relaciones

`Ambiente`, `EstadoAmbiente` y `EstadoMetadatoAmbiente` contienen `id` y `nombre`. El contrato actual no tiene una referencia de ambiente a estado de ambiente.

`MetadatoAmbiente` relaciona tres registros existentes:

```json
{
  "idParametro": "123e4567-e89b-12d3-a456-426614174001",
  "idAmbiente": "123e4567-e89b-12d3-a456-426614174002",
  "idEstadoMetadatoAmbiente": "123e4567-e89b-12d3-a456-426614174003"
}
```

No contiene `valor` ni `idTipoMetadato`; esos campos pertenecen a la entidad `Metadato`.

Las respuestas son `AmbienteResponse`, `EstadoAmbienteResponse`, `EstadoMetadatoAmbienteResponse` y `MetadatoAmbienteResponse`. Sus colecciones se llaman `ambientes`, `estadosAmbiente`, `estadosMetadatoAmbiente` y `metadatosAmbiente`, respectivamente. Crear y actualizar estos cuatro recursos devuelve la entidad en la colección; eliminar devuelve la colección vacía.

## Respuestas de las operaciones originales

Las escrituras de los recursos originales devuelven mensajes y una colección vacía; no devuelven la entidad creada o actualizada.

Actualmente las escrituras y cambios de estado de módulos y funcionalidades usan **ParametroResponse**, con `parametros: []`. Sus consultas sí usan `ModuloResponse` y `FuncionalidadResponse`. El YAML conserva el comportamiento implementado.

Los errores de negocio se clasifican como 400 (validación), 404 (referencia o recurso inexistente) y 409 (conflicto). Los errores técnicos manejados globalmente responden 500. Algunos controladores capturan excepciones y añaden la colección vacía al cuerpo; organización también convierte excepciones no de negocio en 400. Consultar los mensajes devueltos.

## Eventos en tiempo real

| Recurso | Nombre del evento SSE | Propiedad del recurso dentro de data |
| --- | --- | --- |
| Organización | `organizacion` | `organizacion` |
| Aplicación | `aplicacion` | `aplicacion` |
| Módulo | `modulo` | `modulo` |
| Funcionalidad | `funcionalidad` | `funcionalidad` |
| Parámetro | `parametro` | `parametro` |
| Metadato | `metadato` | `metadato` |
| Ambiente | `ambiente` | `ambiente` |
| Estado de ambiente | `estadoambiente` | `estadoAmbiente` |
| Estado de metadato de ambiente | `estadometadatoambiente` | `estadoMetadatoAmbiente` |
| Metadato de ambiente | `metadatoambiente` | `metadatoAmbiente` |

Dentro del JSON, `event` toma los valores `CREATED`, `UPDATED` o `DELETED`. Cada flujo tiene un esquema y un ejemplo propios en el YAML. La extensión `x-event-data-schema` identifica el esquema del JSON contenido en `data`; el cuerpo HTTP sigue siendo un flujo de texto SSE.

Cada publisher conserva sus últimos 100 eventos en memoria y los reproduce al suscribirse. Se combinan tres publishers por recurso: no se garantiza un orden global entre sus historiales. No se emite un identificador SSE ni se procesa `Last-Event-ID`; el historial se pierde al reiniciar. Los controladores envían un comentario inicial `connected`, excepto el de metadatos.

## Documentación y endpoints técnicos

Además de las rutas de negocio, la configuración actual expone los siguientes endpoints de infraestructura. Sus cuerpos dependen de Spring Boot y de los componentes instalados, por lo que no se incorporan a los esquemas del catálogo:

| Método | Ruta | Uso |
| --- | --- | --- |
| GET | `/actuator` | Enlaces a los endpoints de gestión expuestos |
| GET | `/actuator/health` | Estado de salud y detalles de componentes |
| GET | `/actuator/info` | Información de la aplicación |
| GET | `/actuator/prometheus` | Métricas en formato Prometheus |
| GET | `/v3/api-docs` | OpenAPI generado por springdoc |
| GET | `/v3/api-docs.yaml` | Representación YAML generada por springdoc |
| GET | `/v3/api-docs/swagger-config` | Configuración de Swagger UI |
| GET | `/swagger-ui.html` | Entrada a Swagger UI |
| GET | `/openapi/catalogo-parametros-v1.yaml` | Contrato estático publicado al compilar |

Swagger UI está configurado para leer `/v3/api-docs`, no este archivo estático. Editar este YAML actualiza el contrato versionado y el recurso estático del próximo empaquetado.

## Verificación de esta revisión

Se compararon todas las anotaciones de rutas de los controladores, los DTO de entrada (incluida la herencia de MetadatoAmbienteDtoRequest), las 12 entidades, las respuestas, los getters de eventos y los publishers.

Se verificaron la sintaxis YAML, la ausencia de claves duplicadas, la resolución de referencias locales, la unicidad de operationId, los campos de entidades y la igualdad entre las 68 operaciones del código y las del contrato. No fue necesario iniciar Azure, SurrealDB ni el backend para esta comprobación.

