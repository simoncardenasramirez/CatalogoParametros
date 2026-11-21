# Eventos Pulsar del Catálogo de Parámetros

Catálogo de Parámetros publica avisos cuando se crea, actualiza o elimina un parámetro o un
metadato. El evento se enruta por la identidad de servicio registrada para la aplicación y el
ambiente del recurso.

## Conexión

Con Docker Compose, un consumidor que se conecte a la misma red puede usar:

```text
pulsar://pulsar-parametros:6650
```

Desde el host, la conexión publicada es:

```text
pulsar://localhost:6652
```

El prefijo de topics es:

```text
persistent://public/default/catalogo-parametros-events
```

El consumidor debe suscribirse al topic de su `client_id`:

```text
persistent://public/default/catalogo-parametros-events-<client_id>
```

El `client_id` es el que Seguridad entrega al crear la identidad de servicio y que queda
asociado en Catálogo a una aplicación y un ambiente. No se publica por defecto un topic común
por ambiente, para evitar que una aplicación reciba eventos de otra.

## Contrato del evento

```json
{
  "source": "catalogo-parametros",
  "eventType": "ParameterUpdated",
  "resource": "parametros",
  "operation": "UPDATED",
  "resourceName": "correo",
  "resourceValue": { "emails": [{ "email": "nuevo@uco.edu.co" }] },
  "resourceId": "uuid-del-recurso",
  "parentId": "uuid-de-la-funcionalidad-o-parametro",
  "applicationId": "uuid-de-la-aplicacion",
  "environmentId": "uuid-del-ambiente",
  "eventId": "uuid-del-evento",
  "schemaVersion": 3,
  "occurredAt": "2026-11-21T16:18:25Z"
}
```

Los tipos publicados son `ParameterCreated`, `ParameterUpdated`, `ParameterDeleted`,
`MetadataCreated`, `MetadataUpdated`, `MetadataDeleted`, `MetadataEnvironmentCreated`,
`MetadataEnvironmentUpdated` y `MetadataEnvironmentDeleted`.

Para un parámetro que tiene metadatos en varios ambientes se publica un aviso por cada ambiente
asociado. Para un metadato se usa directamente su ambiente. El payload no contiene valores
secretos ni el `client_secret`.

## Valor y arranque automÃ¡tico

`resourceValue` contiene el valor nuevo, o el valor que tenÃ­a antes de eliminarse, cuando el
recurso es un parÃ¡metro o metadato. Puede ser `null` si no existe valor. Nunca se incluye el
`client_secret`.

El broker y el publicador se levantan automÃ¡ticamente con el Compose del CatÃ¡logo. Un consumidor
debe iniciar su listener con el `client_id` configurado y reintentar la conexiÃ³n si el broker aÃºn
no estÃ¡ disponible. Pulsar no recibe el secreto M2M: el `client_id` Ãºnicamente determina el topic
aislado que debe consumir.

## Aislamiento

Antes de publicar, el backend resuelve la aplicación propietaria y consulta las identidades
registradas. Solo genera topics cuyas identidades cumplen simultáneamente:

```text
identity.applicationId == event.applicationId
identity.environmentId == event.environmentId
```

Por eso, una identidad de Producción o de otra aplicación no recibe un cambio de Desarrollo.
