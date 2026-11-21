# Consumo de MessageUcoLab con PEP

`CatalogoParametros` es el origen del consumo servidor a servidor y `MessageUcoLab`
es el destino. `BackPrueba` no participa en este flujo.

## Configuración del origen

El starter PEP permanece en la versión `0.1.4`. La identidad técnica se inyecta
por variables de despliegue:

```properties
security.pep.service-identity.enabled=true
security.pep.service-identity.token-uri=${KEYCLOAK_TOKEN_URI}
security.pep.service-identity.client-id=${MESSAGEUCOLAB_CLIENT_ID:${ORIGIN_TO_DESTINATION_CLIENT_ID}}
security.pep.service-identity.client-secret=${MESSAGEUCOLAB_CLIENT_SECRET:${ORIGIN_TO_DESTINATION_CLIENT_SECRET}}
security.pep.service-identity.timeout=3s
security.pep.service-identity.refresh-skew=30s
```

`MessageUcoLabSecurityConfiguration` agrega el Bearer técnico solamente a las
peticiones cuyo origen coincide con `MESSAGEUCOLAB_URL`. En la versión 0.3.0,
`MessageReader` consume la ruta autenticada de servidor a servidor y ya no envía
identificadores de aplicación ni ambiente.

## Consumo desde una pantalla

El navegador no usa `MessageReader`, `createMessageClient` ni recibe el secreto
de la identidad. El backend expone el texto mediante su propia ruta protegida:

```text
GET /catalogo-parametros/api/v1/mensajes/{codigo}
```

Ejemplo desde el frontend:

```javascript
const respuesta = await fetch('/catalogo-parametros/api/v1/mensajes/MSG_100');
const mensaje = await respuesta.json();
mostrar({ title: mensaje.title, content: mensaje.content });
```

El backend consulta `MessageUcoLab` con la identidad de servicio y devuelve el
detalle al navegador. El minifront se conserva únicamente para la pantalla de
administración de mensajes.

## Aprovisionamiento en Security

Para el ambiente `dev`, Security debe entregar a CatalogoParametros una identidad
exclusiva y asignarle el rol técnico que permita consultar el recurso de lectura
de mensajes. La autorización debe registrar el método y ruta exactos publicados
por MessageUcoLab:

```text
GET /messageucolab/v1/messages/consumer
```

`PEP_REGISTRATION_TOKEN` sigue siendo la credencial de CatalogoParametros como
destino y no se reutiliza como identidad del consumo hacia MessageUcoLab.

## Variables que faltan para activar el flujo

Los valores siguientes no se inventan ni se versionan:

```env
SECURITY_PEP_SERVICE_IDENTITY_ENABLED=true
KEYCLOAK_TOKEN_URI=http://keycloak.localhost:19090/realms/security-baseline/protocol/openid-connect/token
MESSAGEUCOLAB_CLIENT_ID=<client-id-tecnico>
MESSAGEUCOLAB_CLIENT_SECRET=<client-secret-tecnico>
MESSAGEUCOLAB_URL=<url-publica-de-messageucolab-protegida-por-pep>
```

## Avisos de cambios por Pulsar

El consumidor opcional se activa en el despliegue del catÃ¡logo con:

```env
PULSAR_ENABLED=true
PULSAR_URL=pulsar://pulsar:6650
MESSAGEUCOLAB_EVENTS_TOPIC_PREFIX=persistent://public/default/message-events
PULSAR_SUBSCRIPTION_NAME=catalogo-parametros
MESSAGEUCOLAB_CLIENT_ID=<client-id-tecnico>
```

El backend se conecta a la red externa `messageucolab_pulsar-network`, escucha
el tÃ³pico `<prefijo>-<MESSAGEUCOLAB_CLIENT_ID>` y publica los avisos recibidos
por SSE en:

```text
GET /catalogo-parametros/api/v1/avisos
```

El evento solo contiene `eventType`, `messageCode`, `environmentId` y `eventId`.
Para obtener el contenido actualizado, el consumidor debe consultar luego
`GET /catalogo-parametros/api/v1/mensajes/{codigo}`.

Mientras Security no entregue esa identidad y el permiso de destino, la
integración permanece desactivada por defecto (`false`) para no arrancar el
backend con secretos inexistentes.

## PublicaciÃ³n de cambios del catÃ¡logo por Pulsar

El catÃ¡logo tambiÃ©n publica invalidaciones cuando se crea, actualiza o elimina
un `parametro` o un `metadato`. El evento no incluye el valor del parÃ¡metro ni
credenciales; el consumidor debe volver a consultar el recurso que tenga
autorizado.

La publicaciÃ³n se activa con `PULSAR_PUBLISH_ENABLED=true` y acepta varios
topics separados por coma. Cada consumidor debe usar una suscripciÃ³n distinta
para recibir su propia copia:

```env
PULSAR_PUBLISH_ENABLED=true
PULSAR_PUBLISH_TOPIC_NAMES=persistent://public/default/catalogo-parametros-messageucolab-dev,persistent://public/default/catalogo-parametros-security-dev
CATALOGO_ENVIRONMENT_ID=<uuid-opcional-para-cambios-de-parametros>
```

El JSON publicado tiene esta forma:

```json
{
  "source": "catalogo-parametros",
  "eventType": "ParameterUpdated",
  "resource": "parametros",
  "operation": "UPDATED",
  "resourceId": "<uuid>",
  "parentId": "<uuid-funcionalidad-o-parametro>",
  "environmentId": "<uuid-o-null>",
  "eventId": "<uuid-unico>"
}
```

Los tipos posibles son `ParameterCreated`, `ParameterUpdated`,
`ParameterDeleted`, `MetadataCreated`, `MetadataUpdated` y `MetadataDeleted`.
Los errores de Pulsar se registran y no deshacen la transacciÃ³n ya confirmada
del catÃ¡logo; los consumidores deben usar `eventId` para idempotencia.

